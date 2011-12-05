/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.geno.web.component.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.geno.entity.DecodeMask;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.Marker;
import au.org.theark.core.model.geno.entity.MarkerGroup;
import au.org.theark.core.model.geno.entity.MarkerType;
import au.org.theark.core.model.geno.entity.MetaData;
import au.org.theark.core.model.geno.entity.MetaDataField;
import au.org.theark.core.model.geno.entity.MetaDataType;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.geno.service.Constants;
import au.org.theark.geno.service.IGenoService;

public class TestContainerPanel extends Panel {

	@SpringBean(name = Constants.GENO_SERVICE)
	private IGenoService serviceInterface;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService arkCommonService;

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(TestContainerPanel.class);
	private Long edID = null; 

	public TestContainerPanel(String id) {
		super(id);
		log.info("TestContainerPanel Constructor invoked.");
		Form testForm = new Form("testForm");
		testForm.add(new AjaxButton(au.org.theark.geno.web.Constants.FIRETEST, new StringResourceModel("page.fireTest", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Subject currentUser = SecurityUtils.getSubject();
				Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
				if (studyId == null)
					studyId = new Long(Constants.TEST_STUDY_ID);
				
				log.info("WTF you hit me");
				GenoCollection colEn = new GenoCollection();
		        colEn.setStudy(arkCommonService.getStudy(studyId));
				colEn.setName("New test");
		        serviceInterface.createCollection(colEn);
				MetaData metaData = new MetaData();
				
				Long massMDFId = new Long(43);
				MetaDataField mdf = serviceInterface.getMetaDataField(massMDFId);
		        metaData.setCollection(colEn);
		        metaData.setMetaDataField(mdf);
				log.info("Creating a new MetaData record: " + metaData);
				serviceInterface.createMetaData(metaData);
				
				edID = serviceInterface.newEncodedData(colEn);
			}
		});
		
		testForm.add(new AjaxButton(au.org.theark.geno.web.Constants.WATERTEST, new StringResourceModel("page.waterTest", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.info("Noah save me!");
				if (edID != null)
				{
					readEncodedData(edID);
				}
			}
		});
		
		testForm.add(new AjaxButton(au.org.theark.geno.web.Constants.NOAHTEST, new StringResourceModel("page.noahsRescue", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.info("Noah saves!");
				serviceInterface.testGWASImport();
			}
		});
		
		testForm.add(new AjaxButton(au.org.theark.geno.web.Constants.HELLOTEST, new StringResourceModel("page.helloServlet", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest(); 
				HttpServletRequest request = servletWebRequest.getHttpServletRequest(); 
				WebResponse webResponse = (WebResponse) getRequestCycle().getOriginalResponse(); 
				HttpServletResponse response = webResponse.getHttpServletResponse(); 
				RequestDispatcher dispatcher = ((ServletWebRequest) getRequest()).getHttpServletRequest().getRequestDispatcher(au.org.theark.geno.web.Constants.HELLOSERVLET); 
				GenericServletResponseWrapper wrappedResponse = new GenericServletResponseWrapper(response);

			    try {
					log.info("Forwarding request to Hello Servlet");
					dispatcher.forward(request, wrappedResponse);
				    log.info("response from servlet: " + new String(wrappedResponse.getData()));
			    } catch (ServletException e) {
			    	throw new RuntimeException(e);
			    } catch (IOException e) {
			    	throw new RuntimeException(e);
			    }
				log.info("Returned from forward to Hello Servlet");
			}
		});
		
		/* Servlet */
		WebMarkupContainer formForServlet = new WebMarkupContainer("helloForm") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				// TODO Auto-generated method stub
				super.onComponentTag(tag);
				tag.put("method", "post");
				tag.put("action", "http://localhost:8888/ark/" + au.org.theark.geno.web.Constants.HELLOSERVLET);
			}
		};
		
		TextField<String> firstNameTxt = new TextField<String>("firstName", new Model<String>("TestName"));
		formForServlet.add(firstNameTxt);		
//		formForServlet.add(new AjaxButton("helloDirect", new StringResourceModel("page.helloDirect", this, null)) {
//			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				System.out.println("Hit the Wicket servlet button");
//			}
//			
//			public void onError(AjaxRequestTarget target, Form<?> form) {
//				System.out.println("Error occurred when hitting the Wicket servlet button");
//			}
//		});

//		testForm.add(formForServlet);
		add(testForm);
		add(formForServlet);
	}

	public void createMassMetaDataField() {
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
		if (studyId == null)
			studyId = new Long(Constants.TEST_STUDY_ID);

		MetaDataType mdt = serviceInterface.getMetaDataTypeByName("Number");
        // Create a new MetaDataField
        MetaDataField mdf = new MetaDataField();
        mdf.setName("Mass");
        mdf.setDescription("kg");
        mdf.setStudy(arkCommonService.getStudy(studyId));
        mdf.setMetaDataType(mdt);
		log.info("Creating a new MetaDataField: " + mdf);
        serviceInterface.createMetaDataField(mdf);        
	}
	
	public void unusedEncodedData(GenoCollection col) {
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
		if (studyId == null)
			studyId = new Long(Constants.TEST_STUDY_ID);

    	Status s = serviceInterface.getStatusByName("Active");
    	
        Date dateNow = new Date(System.currentTimeMillis());
    	
    	MarkerType mkType = new MarkerType();
    	mkType.setName("SNP");
    	
    	MarkerGroup mrkGrp = new MarkerGroup();
    	mrkGrp.setStudy(arkCommonService.getStudy(studyId));
    	mrkGrp.setName("pretend dbSNP");
    	mrkGrp.setUserId("test");
    	mrkGrp.setInsertTime(new Date());

    	Marker mrk1 = new Marker();
    	mrk1.setName("BRCA1");
    	mrk1.setMarkerGroup(mrkGrp);
    	mrk1.setChromosome("1");
    	mrk1.setUserId("test");
    	mrk1.setMajorAllele("G");
    	mrk1.setMinorAllele("A");
    	mrk1.setInsertTime(new Date());

    	Marker mrk2 = new Marker();
    	mrk2.setName("BRCA2");
    	mrk2.setMarkerGroup(mrkGrp);
    	mrk2.setChromosome("1");
    	mrk2.setUserId("test");
    	mrk2.setMajorAllele("T");
    	mrk2.setMinorAllele("C");
    	mrk2.setInsertTime(new Date());
    	
    	DecodeMask dm1 = new DecodeMask();
    	dm1.setBitPosition(0);
    	dm1.setMarker(mrk1);
    	dm1.setCollection(col);
    	
    	DecodeMask dm2 = new DecodeMask();
    	dm2.setBitPosition(1);
    	dm2.setMarker(mrk2);
    	dm2.setCollection(col);
    	
    	Vector<Marker> mask = new Vector<Marker>();
    	mask.add(mrk1);
    	mask.add(mrk2);
    	Vector<String> genotypeVec = new Vector<String>();
    	genotypeVec.add("AA");
    	genotypeVec.add("CT");

//    	try {
//    		BitSet[] encodedBitSets = GWASImport.createBitSet(genotypeVec, mask);
//    	} catch (Exception ex) {
//    		System.out.println("Internal code error: " + ex);
//    	}
    }
	
	public void readEncodedData(Long encodedDataId) {
		serviceInterface.getEncodedBit(encodedDataId);
	}
	
	
	//TODO: Are these Generic classes for the servlet required?
	class GenericServletOutputStream extends ServletOutputStream {
		private OutputStream out;

		public GenericServletOutputStream(OutputStream out) {
			this.out = out;
		}

		public void write(int b) throws IOException {
			out.write(b);
		}

		public void write(byte[] b) throws IOException {
			out.write(b);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
		}
	}

	class GenericServletResponseWrapper extends HttpServletResponseWrapper {
		private ByteArrayOutputStream output;

		public GenericServletResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new ByteArrayOutputStream();
		}

		public byte[] getData() {
			return output.toByteArray();
		}

		public ServletOutputStream getOutputStream() {
			return new GenericServletOutputStream(output);
		}

		public PrintWriter getWriter() {
			return new PrintWriter(getOutputStream(), true);
		}
	}

}
