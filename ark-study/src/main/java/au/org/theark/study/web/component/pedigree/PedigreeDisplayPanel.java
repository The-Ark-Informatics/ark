package au.org.theark.study.web.component.pedigree;

import static au.org.theark.study.web.Constants.MADELINE_PEDIGREE_TEMPLATE;
import static au.org.theark.study.web.Constants.PEDIGREE_TEMPLATE_EXT;

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
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import au.org.theark.core.jni.ArkMadelineProxy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

import com.itextpdf.text.Rectangle;
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

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService					studyService;

	private byte[]					pngOutPutArray;

	private DownloadLink			downloadLink;

	private DownloadLink			pdfLink;

	public PedigreeDisplayPanel(String id) {
		super(id);
		this.setOutputMarkupId(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		if (subjectUID == null) {
			this.error("There is no subject selected. Please select a subject.");
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
		final StringBuffer sb = new StringBuffer();
		sb.setLength(0);

		String familyId = null;
		StringBuffer columnList = new StringBuffer("IndividualId");
		Study study = iArkCommonService.getStudy(studyId);
		StudyPedigreeConfiguration config = study.getPedigreeConfiguration();

		if (config != null && config.isDobAllowed()) {
			columnList.append(" DOB");
		}

		if (config != null && config.isAgeAllowed()) {
			columnList.append(" Age");
		}

		RelativeCapsule[] relatives = studyService.generateSubjectPedigreeImageList(subjectUID, studyId);

		if (relatives.length > 2) {
			StringWriter out = null;
			familyId = relatives[0].getFamilyId();

			try {
				Theme theme = new Theme();
				Chunk chunk = theme.makeChunk(MADELINE_PEDIGREE_TEMPLATE, PEDIGREE_TEMPLATE_EXT);
				chunk.set("relatives", relatives);
				out = new StringWriter();
				chunk.render(out);
			}
			catch (IOException io) {
				io.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			ArkMadelineProxy madeline = new ArkMadelineProxy();
			String madelineOutput = null;

			try {
				madelineOutput = madeline.generatePedigree(out.toString(), columnList.toString());
				log.info(" ----------------- Pedigree generated successfully -------------------------");
			}
			catch (Error e) {
				e.printStackTrace();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
			finally {
				try {
					out.flush();
					out.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Execute madeline by runtime
			// Madeline execute independent of the Ark program
			// Not recommend this option because this may causes unexpected errors. Ex: FileNotFound Exception
			// try{
			//
			// File file = new File("/tmp");
			// Runtime.getRuntime().exec("madeline2 /tmp/cs_009.data",null, file);
			//
			// }catch(IOException ioe){
			// ioe.printStackTrace();
			// }

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new InputSource(new StringReader(madelineOutput)));

				XPath xpath = XPathFactory.newInstance().newXPath();

				String xPathExpression = "//text";

				NodeList nodes = (NodeList) xpath.evaluate(xPathExpression, doc, XPathConstants.NODESET);

				for (int idx = 0; idx < nodes.getLength(); idx++) {
					String nodeText = nodes.item(idx).getTextContent();
					if (nodeText != null) {
						// Replace family id and dummy subject uids by blank text
						if (nodeText.startsWith("_F") || nodeText.startsWith("!")) {
							nodes.item(idx).setTextContent("");
						}

						// Replace half generated Indiv.. by UID
						if (nodeText.startsWith("Indiv")) {
							nodes.item(idx).setTextContent("UID");
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
					String pedFileName = "Ark_" + subjectUID + ".png";
					tempFile = new File(tmpDir, pedFileName);
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
		downloadLink.setOutputMarkupId(true);

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

					com.itextpdf.text.Image pedigreeImg = PngImage.getImage(pngOutPutArray);

					Rectangle pageSize = new Rectangle(pedigreeImg.getWidth(), pedigreeImg.getHeight());

					com.itextpdf.text.Document document = new com.itextpdf.text.Document(pageSize, 0f, 0f, 0f, 0f);
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(f));
					writer.setStrictImageSequence(true);
					document.open();
					document.add(pedigreeImg);
					document.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}
		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);

		pdfLink.setOutputMarkupId(true);

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
