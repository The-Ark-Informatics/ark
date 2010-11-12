package au.org.theark.phenotypic.web.component.reportContainer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.phenotypic.service.IPhenotypicService;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class ReportContainerPanel extends Panel
{
	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(ReportContainerPanel.class);

	public ReportContainerPanel(String id)
	{
		super(id);
		log.info("ReportContainerPanel Constructor invoked.");
		Form reportForm = new Form("reportForm");
		reportForm.add(new Button(au.org.theark.phenotypic.web.Constants.NEW_BUTTON, new StringResourceModel("page.reportButton1", this, null))
		{
			public void onSubmit()
			{
				log.info("Report button 1 test");
			}
		});
		reportForm.add(new Button(au.org.theark.phenotypic.web.Constants.SAVE_BUTTON, new StringResourceModel("page.reportButton2", this, null))
		{
			public void onSubmit()
			{
				log.info("Report button 2 test");
			}
		});
		reportForm.add(new Button(au.org.theark.phenotypic.web.Constants.EDIT_BUTTON, new StringResourceModel("page.reportButton3", this, null))
		{
			public void onSubmit()
			{
				log.info("Report button 3 test");
			}
		});
		add(reportForm);
	}
}
