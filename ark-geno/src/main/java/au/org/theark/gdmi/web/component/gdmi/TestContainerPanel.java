package au.org.theark.gdmi.web.component.gdmi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Blob;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.Date;
import java.util.Vector;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.gdmi.model.entity.Collection;
import au.org.theark.gdmi.model.entity.DecodeMask;
import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.model.entity.MetaDataField;
import au.org.theark.gdmi.model.entity.MetaDataType;
import au.org.theark.gdmi.model.entity.Status;
import au.org.theark.gdmi.service.IGDMIService;

public class TestContainerPanel extends Panel{

	@SpringBean(name = "gwasService")
	private IGDMIService serviceInterface;

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(TestContainerPanel.class);
	private Long edID = null; 

	public TestContainerPanel(String id) {
		super(id);
		log.info("TestContainerPanel Constructor invoked.");
		Form testForm = new Form("testForm");
		testForm.add(new Button(au.org.theark.gdmi.web.Constants.FIRETEST, new StringResourceModel("page.fireTest", this, null))
		{
			public void onSubmit()
			{
				log.info("WTF you hit me");
				Collection colEn = new Collection();
		        colEn.setStudyId(new Long(100));
		        serviceInterface.createCollection(colEn);
				MetaData metaData = new MetaData();
				
				Subject currentUser = SecurityUtils.getSubject();
				Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
				Long massMDFId = new Long(43);
				MetaDataField mdf = serviceInterface.getMetaDataField(massMDFId);
		        metaData.setCollection(colEn);
		        metaData.setMetaDataField(mdf);
				log.info("Creating a new MetaData record: " + metaData);
				serviceInterface.createMetaData(metaData);
				
				edID = serviceInterface.newEncodedData(colEn);
			}
		});
		testForm.add(new Button(au.org.theark.gdmi.web.Constants.WATERTEST, new StringResourceModel("page.waterTest", this, null))
		{
			public void onSubmit()
			{
				log.info("Noah save me!");
				if (edID != null)
				{
					readEncodedData(edID);
				}
			}
		});
		testForm.add(new Button(au.org.theark.gdmi.web.Constants.NOAHTEST, new StringResourceModel("page.noahsRescue", this, null))
		{
			public void onSubmit()
			{
				log.info("Noah saves!");
				serviceInterface.testGWASImport();
			}
		});
		add(testForm);
	}

	public void createMassMetaDataField() {
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		if (studyId == null)
			studyId = new Long(1);

		MetaDataType mdt = serviceInterface.getMetaDataTypeByName("Number");
        // Create a new MetaDataField
        MetaDataField mdf = new MetaDataField();
        mdf.setName("Mass");
        mdf.setDescription("kg");
        mdf.setStudyId(studyId);
        mdf.setMetaDataType(mdt);
		log.info("Creating a new MetaDataField: " + mdf);
        serviceInterface.createMetaDataField(mdf);        
	}
	
	public void unusedEncodedData(Collection col) {
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

    	Status s = serviceInterface.getStatusByName("Active");
    	
        Date dateNow = new Date(System.currentTimeMillis());
    	
    	MarkerType mkType = new MarkerType();
    	mkType.setName("SNP");
    	
    	MarkerGroup mrkGrp = new MarkerGroup();
    	mrkGrp.setStudyId(studyId);
    	mrkGrp.setUploadId(new Long(0));
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
	
}
