package au.org.theark.phenotypic.web.component.TestContainer;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.service.IPhenotypicService;

@SuppressWarnings( { "unchecked", "serial" })
public class TestContainerPanel extends Panel
{

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(TestContainerPanel.class);

	public TestContainerPanel(String id)
	{
		super(id);
		log.info("TestContainerPanel Constructor invoked.");
		Form testForm = new Form("testForm");
		testForm.add(new Button(au.org.theark.phenotypic.web.Constants.FIRETEST, new StringResourceModel("page.fireTest", this, null))
		{
			public void onSubmit()
			{
				log.info("WTF you hit me");
				Collection colEn = new Collection();
				colEn.setStudyId(new Long(100));
				serviceInterface.createCollection(colEn);
				FieldData fieldData = new FieldData();
				Date dateNow = new Date(System.currentTimeMillis());

				Subject currentUser = SecurityUtils.getSubject();
				log.info("Using StudyID: " + currentUser.getSession().getAttribute("studyId"));
				
				Long massFieldId = new Long(43);
				Field f = serviceInterface.getField(massFieldId);
				fieldData.setCollection(colEn);
				fieldData.setField(f);
				fieldData.setDateCollected(dateNow);
				
				log.info("Creating a new field record: " + fieldData);
				serviceInterface.createFieldData(fieldData);
			}
		});
		testForm.add(new Button(au.org.theark.phenotypic.web.Constants.WATERTEST, new StringResourceModel("page.waterTest", this, null))
		{
			public void onSubmit()
			{
				log.info("Noah save me!");
			}
		});
		testForm.add(new Button(au.org.theark.phenotypic.web.Constants.NOAHTEST, new StringResourceModel("page.noahsRescue", this, null))
		{
			public void onSubmit()
			{
				log.info("Noah saves!");
				serviceInterface.testPhenotypicImport();
			}
		});
		add(testForm);
	}

	public void createField()
	{
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		if (studyId == null)
			studyId = new Long(1);

		FieldType fieldType = serviceInterface.getFieldTypeByName("Number");
		// Create a new MetaDataField
		Field field = new Field();
		field.setName("Mass");
		field.setDescription("kg");
		field.setStudyId(studyId);
		field.setFieldType(fieldType);
		log.info("Creating a new Field: " + field);
		serviceInterface.createField(field);
	}
}
