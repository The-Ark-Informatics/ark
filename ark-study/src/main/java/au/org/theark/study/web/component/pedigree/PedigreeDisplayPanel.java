package au.org.theark.study.web.component.pedigree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.image.PNGImage;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import au.org.theark.core.jni.MadelineArkProxy;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;
import com.x5.template.Chunk;
import com.x5.template.Theme;

public class PedigreeDisplayPanel extends Panel implements IAjaxIndicatorAware {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	static Logger					log					= LoggerFactory.getLogger(PedigreeDisplayPanel.class);

	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService					studyService;

	private byte[]				pngOutPutArray;

	private DownloadLink			downloadLink;
	
	private DownloadLink 		pdfLink;

	public PedigreeDisplayPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		if (subjectUID == null) {
			this.error("There is no subject in context. Please select a Subject");
			DynamicImageResource emptyImageRes = new DynamicImageResource("png") {
				private static final long	serialVersionUID	= 1L;

				@Override
				public byte[] getImageData(Attributes attributes) {
					return new byte[0];
				}

			};
			addOrReplace(new Image("pedigree", emptyImageRes));
			return;
		}

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionId = getSession().getId();
		// log.info(subjectUID!=null ? "Subject UID -------- "+subjectUID.toString() +
		// "-----------Session Id is -----"+sessionId:"---------------  Subject UID is null ----------------------");

		String filePrefix = subjectUID.toString() + "_" + sessionId;
		final StringBuffer sb = new StringBuffer();
		sb.setLength(0);

		String familyId = null;
		RelativeCapsule[] relatives = studyService.generateSubjectPedigreeImageList(subjectUID, studyId);

		if (relatives.length > 2) {
			File dataFile = null;
			familyId = relatives[0].getFamilyId();

			try {
				Theme theme = new Theme();
				Chunk chunk = theme.makeChunk("pedigree_template", "txt");

				// RelativeCapsule[] relatives={new RelativeCapsule("cs_008","i00045","M",".",".",".",".",".",".",".",".","."),
				// new RelativeCapsule("cs_008","i00046","F",".",".",".",".",".",".",".",".","."),
				// new RelativeCapsule("cs_008","i00047","F","i00045","i00046",".",".",".",".",".",".","."),
				// new RelativeCapsule("cs_008","i00048","F","i00045","i00046",".","Y",".",".",".",".",".")};

				chunk.set("relatives", relatives);

				dataFile = new File(filePrefix + ".data");
				FileWriter out = new FileWriter(dataFile);
				chunk.render(out);
				out.flush();
				out.close();
			}
			catch (IOException io) {
				io.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			MadelineArkProxy madeline = new MadelineArkProxy();
			try {
				madeline.sayHello();
			}
			catch (Error e) {
				e.printStackTrace();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}

			try {
				madeline.generatePedigree(filePrefix + ".data");
			}
			catch (Error e) {
				e.printStackTrace();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}

			// Execute madeline by runtime
			// Madeline execute independent of the Ark program
			// Not recomend this option because this may causes unexpected errors. Ex: FileNotFound Exception
			// try{
			//
			// File file = new File("/tmp");
			// Runtime.getRuntime().exec("madeline2 /tmp/cs_009.data",null, file);
			//
			// }catch(IOException ioe){
			// ioe.printStackTrace();
			// }

			File pedFile = new File(familyId + "ped.xml");

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(pedFile);

				XPath xpath = XPathFactory.newInstance().newXPath();

//				String xPathExpression = "//text[1]";
				String xPathExpression = "//text";
				
				NodeList nodes = (NodeList) xpath.evaluate(xPathExpression, doc, XPathConstants.NODESET);

				
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					String nodeText=nodes.item(idx).getTextContent();
					if(nodeText !=null){
						//Replace family id and dummy subject uids by blank text
						if(nodeText.startsWith("_F") 
								|| nodeText.startsWith("!")){
							nodes.item(idx).setTextContent("");
						}
						
					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new StringWriter());
				transformer.transform(source, result);
				sb.append(result.getWriter().toString());
			}
			catch (Exception e) {
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<svg width=\"640\" height=\"480\" xmlns=\"http://www.w3.org/2000/svg\">\r\n <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->\r\n <g>\r\n  <title>Layer 1</title>\r\n  <text transform=\"rotate(-0.0100589, 317.008, 97.5)\" xml:space=\"preserve\" text-anchor=\"middle\" font-family=\"serif\" font-size=\"24\" id=\"svg_3\" y=\"106\" x=\"317\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#000000\">Pedigree Error</text>\r\n </g>\r\n</svg>");
				e.printStackTrace();
			}

			if (dataFile.delete()) {
				log.info(dataFile.getName() + " is deleted!");
			}
			else {
				log.error(dataFile.getName() + " delete operation is failed.");
			}

			if (pedFile.delete()) {
				log.info(pedFile.getName() + " is deleted!");
			}
			else {
				log.error(pedFile.getName() + " delete operation is failed.");
			}

		}
		else {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<svg width=\"640\" height=\"480\" xmlns=\"http://www.w3.org/2000/svg\">\r\n <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->\r\n <g>\r\n  <title>Layer 1</title>\r\n  <text transform=\"rotate(-0.0100589, 317.008, 97.5)\" xml:space=\"preserve\" text-anchor=\"middle\" font-family=\"serif\" font-size=\"24\" id=\"svg_3\" y=\"106\" x=\"317\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#000000\">No Pedigree History</text>\r\n </g>\r\n</svg>");
		}

		DynamicImageResource svgImageRes = new DynamicImageResource("png") {
			private static final long	serialVersionUID	= 1L;

			@Override
			public byte[] getImageData(Attributes attributes) {
				PNGTranscoder t = new PNGTranscoder();

				try {
					String svg = sb.toString();
					TranscoderInput input = new TranscoderInput(new StringReader(svg));
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					TranscoderOutput output = new TranscoderOutput(os);
					t.transcode(input, output);

					pngOutPutArray = os.toByteArray();

					return pngOutPutArray;

				}
				catch (Exception e) {
					throw new WicketRuntimeException(e);
				}
			}

		};
		
		
		addOrReplace(new Image("pedigreeImg", svgImageRes));

		downloadLink = new DownloadLink("imgLink", new AbstractReadOnlyModel<File>() {

			@Override
			public File getObject() {
				File tempFile = null;
				String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				try {
					String tmpDir = System.getProperty("java.io.tmpdir");
					String pedFileName="Ark_"+subjectUID+".png";
					tempFile = new File(tmpDir,pedFileName);
					InputStream data = new ByteArrayInputStream(pngOutPutArray);
					Files.writeTo(tempFile, data);
				}
				catch (IOException io) {
					io.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return tempFile;
			}
		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);
		
		downloadLink.add(new Behavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to PNG");
			}
		});

		addOrReplace(downloadLink);
		
		pdfLink = new DownloadLink("pdfLink", new AbstractReadOnlyModel<File>() {

			@Override
			public File getObject() {
				File f = null;
				String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				try {
					String tmpDir = System.getProperty("java.io.tmpdir");
					String pedFileName = "Ark_" + subjectUID + ".pdf";
					f = new File(tmpDir, pedFileName);
					com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate());
					PdfWriter.getInstance(document, new FileOutputStream(f));
					document.open();
					com.itextpdf.text.Image pedigreeImg = PngImage.getImage(pngOutPutArray);
					PdfPTable table = new PdfPTable(1);
					table.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.setWidthPercentage(100f);
					PdfPCell c = new PdfPCell(pedigreeImg, true);
					c.setBorder(PdfPCell.NO_BORDER);
					c.setPadding(5);
					c.getImage().scaleToFit(750f, 750f); /* The new line */
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(c);
					document.add(table);
					document.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}
		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);
		
		pdfLink.add(new Behavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to PDF");
			}
		});
		
		addOrReplace(pdfLink);

	}

	public String getAjaxIndicatorMarkupId() {
		// TODO Auto-generated method stub
		return "indicator";
	}

}
