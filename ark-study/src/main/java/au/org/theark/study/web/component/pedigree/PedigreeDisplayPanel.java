package au.org.theark.study.web.component.pedigree;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.jni.MadelineArkProxy;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

import com.x5.template.Chunk;
import com.x5.template.Theme;

public class PedigreeDisplayPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	static Logger	log	= LoggerFactory.getLogger(PedigreeDisplayPanel.class);
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService studyService;

	public PedigreeDisplayPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		String subjectUID= (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		if(subjectUID == null){
			this.error("There is no subject in context. Please select a Subject");
			DynamicImageResource emptyImageRes = new DynamicImageResource("png") {
	         private static final long serialVersionUID = 1L;

	         @Override
	         public byte[] getImageData(Attributes attributes) {	            
	             return new byte[0];
	         }

	     };
			addOrReplace(new Image("pedigree", emptyImageRes));
			return;
		}
		
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionId=getSession().getId();
//		log.info(subjectUID!=null ? "Subject UID -------- "+subjectUID.toString() + "-----------Session Id is -----"+sessionId:"---------------  Subject UID is null ----------------------");
		
		String filePrefix = subjectUID.toString()+"_"+sessionId;
		final StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		
		String familyId=null;
		RelativeCapsule[] relatives = studyService.generateSubjectPedigree(subjectUID,studyId);

		if(relatives.length >2){
			File dataFile =null; 	
			familyId = relatives[0].getFamilyId();
			
			try{
				Theme theme = new Theme();
				Chunk chunk = theme.makeChunk("pedigree_template","txt");
			
			
			
//			RelativeCapsule[] relatives={new RelativeCapsule("cs_008","i00045","M",".",".",".",".",".",".",".",".","."),
//					new RelativeCapsule("cs_008","i00046","F",".",".",".",".",".",".",".",".","."),
//					new RelativeCapsule("cs_008","i00047","F","i00045","i00046",".",".",".",".",".",".","."),
//					new RelativeCapsule("cs_008","i00048","F","i00045","i00046",".","Y",".",".",".",".",".")};
			
				chunk.set("relatives",relatives);
				
				dataFile = new File(filePrefix+".data");
				FileWriter out = new FileWriter(dataFile);
				chunk.render(out);
				out.flush();
				out.close();
			}catch(IOException io){
				io.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			MadelineArkProxy madeline = new MadelineArkProxy();
			try{
				madeline.sayHello();
			}catch(Error e){
				e.printStackTrace();
			}
			catch(Throwable e){
				e.printStackTrace();
			}
	
			try{
				madeline.generatePedigree(filePrefix+".data");
			}catch(Error e){
				e.printStackTrace();
			}
			catch(Throwable e){
				e.printStackTrace();
			}

		//Execute madeline by runtime 
		//Madeline execute independent of the Ark program
		//Not recomend this option because this may causes unexpected errors. Ex: FileNotFound Exception 
//		try{
//			
//			File file = new File("/tmp");			
//			Runtime.getRuntime().exec("madeline2 /tmp/cs_009.data",null, file);
//
//		}catch(IOException ioe){
//			ioe.printStackTrace();
//		}
		
		
			BufferedReader br = null;
			
			File pedFile=new File(familyId+"ped.xml");
			
			try {			
				String sCurrentLine;
				br = new BufferedReader(new FileReader(pedFile));
				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			if(dataFile.delete()){
				log.info(dataFile.getName() + " is deleted!");
	 		}else{
	 			log.error(dataFile.getName() +" delete operation is failed.");
	 		}
			
			if(pedFile.delete()){
				log.info(pedFile.getName() + " is deleted!");
	 		}else{
	 			log.error(pedFile.getName() +" delete operation is failed.");
	 		}
		
		}else{
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<svg width=\"640\" height=\"480\" xmlns=\"http://www.w3.org/2000/svg\">\r\n <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->\r\n <g>\r\n  <title>Layer 1</title>\r\n  <text transform=\"rotate(-0.0100589, 317.008, 97.5)\" xml:space=\"preserve\" text-anchor=\"middle\" font-family=\"serif\" font-size=\"24\" id=\"svg_3\" y=\"106\" x=\"317\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#000000\">No Pedigree History</text>\r\n </g>\r\n</svg>");
		}
		
		DynamicImageResource svgImageRes = new DynamicImageResource("png") {
         private static final long serialVersionUID = 1L;

         @Override
         public byte[] getImageData(Attributes attributes) {
             PNGTranscoder t = new PNGTranscoder();

             try {
                 String svg = sb.toString();                 
                 TranscoderInput input = new TranscoderInput(new StringReader(svg));
                 ByteArrayOutputStream os = new ByteArrayOutputStream();
                 TranscoderOutput output = new TranscoderOutput(os);
                 t.transcode(input, output);
                 return os.toByteArray();
                 
             } catch (Exception e) {
                 throw new WicketRuntimeException(e);
             }
         }

     };

     addOrReplace(new Image("pedigreeImg", svgImageRes));
		
	}
	
}
