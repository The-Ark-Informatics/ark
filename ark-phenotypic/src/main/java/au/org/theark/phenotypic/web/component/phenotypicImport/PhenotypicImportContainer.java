package au.org.theark.phenotypic.web.component.phenotypicImport;

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
public class PhenotypicImportContainer extends Panel
{

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(PhenotypicImportContainer.class);

	public PhenotypicImportContainer(String id)
	{
		super(id);
		log.info("PhenotypicImportContainer Constructor invoked.");
		Form phenotypicImportForm = new Form("phenotypicImportForm");
		
		phenotypicImportForm.add(new Button(au.org.theark.phenotypic.web.Constants.VALIDATE_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.validatePhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Validate Phenotypic Data File");
				
				java.util.Collection<String> validationMessages = null;
				//TODO Add placeholder to store the validation messages 
				validationMessages = serviceInterface.validatePhenotypicDataFile();
			}
		});
		
		phenotypicImportForm.add(new Button(au.org.theark.phenotypic.web.Constants.IMPORT_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.importPhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Import Phenotypic Data File");
				serviceInterface.importPhenotypicDataFile();
			}
		});

		add(phenotypicImportForm);
	}
}
