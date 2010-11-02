package au.org.theark.phenotypic.web.component.collection;

import java.util.Date;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.service.IPhenotypicService;

@SuppressWarnings( { "unchecked", "serial" })
public class CollectionContainer extends Panel
{

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(CollectionContainer.class);

	public CollectionContainer(String id)
	{
		super(id);
		log.info("CollectionContainer Constructor invoked.");
		Form collectionForm = new Form("collectionForm");
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_COLLECTION, new StringResourceModel("page.newCollection", this, null))
		{
			public void onSubmit()
			{
				Date dateNow = new Date(System.currentTimeMillis());
				Collection collection = new Collection();
				
				collection.setStudyId(new Long(100));
				collection.setInsertTime(dateNow);
				collection.setName("New collection record");
				collection.setDescription("Collection description");
				
				log.info("Creating a new collection record");
				serviceInterface.createCollection(collection);
			}
		});
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_FIELD, new StringResourceModel("page.newField", this, null))
		{
			public void onSubmit()
			{
				log.info("Create new field");
				Field field = new Field();
				FieldType fieldType = serviceInterface.getFieldTypeByName("NUMBER");
				field.setName("BMI");
				field.setFieldType(fieldType);
				field.setDescription("Body Mass Index");
				
				serviceInterface.createField(field);
			}
		});
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.VALIDATE_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.validatePhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Validate Phenotypic Data File");
				serviceInterface.validatePhenotypicDataFile();
			}
		});
		
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.IMPORT_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.importPhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Import Phenotypic Data File");
				serviceInterface.importPhenotypicDataFile();
			}
		});
		add(collectionForm);
	}
}
