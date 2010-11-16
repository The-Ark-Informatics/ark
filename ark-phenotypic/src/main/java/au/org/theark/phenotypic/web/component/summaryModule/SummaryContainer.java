package au.org.theark.phenotypic.web.component.summaryModule;

import java.util.Date;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.service.IPhenotypicService;

@SuppressWarnings( { "unchecked", "serial" ,"unused"})
public class SummaryContainer extends Panel
{

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(SummaryContainer.class);

	public SummaryContainer(String id)
	{
		super(id);
		log.info("SummaryContainer Constructor invoked.");
		Form summaryForm = new Form("summaryForm");
		
		summaryForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_BUTTON, new StringResourceModel("button.new", this, null))
		{
			public void onSubmit()
			{
				log.info("New pressed");
			}
		});
		
		summaryForm.add(new Button(au.org.theark.phenotypic.web.Constants.SAVE_BUTTON, new StringResourceModel("button.save", this, null))
		{
			public void onSubmit()
			{
				log.info("Save pressed");
			}
		});
		
		summaryForm.add(new Button(au.org.theark.phenotypic.web.Constants.EDIT_BUTTON, new StringResourceModel("button.edit", this, null))
		{
			public void onSubmit()
			{
				log.info("Edit pressed");
			}
		});
		add(summaryForm);
	}
}
