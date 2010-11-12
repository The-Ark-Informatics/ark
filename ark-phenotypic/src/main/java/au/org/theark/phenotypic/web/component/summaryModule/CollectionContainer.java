package au.org.theark.phenotypic.web.component.summaryModule;

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

@SuppressWarnings( { "unchecked", "serial" ,"unused"})
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
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_BUTTON, new StringResourceModel("page.newCollection", this, null))
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
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.SAVE_BUTTON, new StringResourceModel("page.newField", this, null))
		{
			public void onSubmit()
			{
				log.info("Save collection");
			}
		});
		
		collectionForm.add(new Button(au.org.theark.phenotypic.web.Constants.EDIT_BUTTON, new StringResourceModel("page.newField", this, null))
		{
			public void onSubmit()
			{
				log.info("Edit collection");
			}
		});
		add(collectionForm);
	}
}
