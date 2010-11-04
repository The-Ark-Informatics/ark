package au.org.theark.phenotypic.web.component.field;

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
public class FieldContainer extends Panel
{
	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(FieldContainer.class);

	public FieldContainer(String id)
	{
		super(id);
		log.info("FieldContainer Constructor invoked.");
		Form fieldForm = new Form("fieldForm");
		
		fieldForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_BUTTON, new StringResourceModel("page.newField", this, null))
		{
			public void onSubmit()
			{
				log.info("New field");
				Field field = new Field();
				FieldType fieldType = serviceInterface.getFieldTypeByName("NUMBER");
				field.setName("BMI");
				field.setFieldType(fieldType);
				field.setDescription("Body Mass Index");
				
				serviceInterface.createField(field);
			}
		});
		
		fieldForm.add(new Button(au.org.theark.phenotypic.web.Constants.SAVE_BUTTON, new StringResourceModel("page.saveField", this, null))
		{
			public void onSubmit()
			{
				log.info("Save field");
			}
		});
		
		fieldForm.add(new Button(au.org.theark.phenotypic.web.Constants.EDIT_BUTTON, new StringResourceModel("page.editField", this, null))
		{
			public void onSubmit()
			{
				log.info("Edit field");
			}
		});
		
		add(fieldForm);
	}
}
