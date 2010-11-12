/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.summaryModule.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.CollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"serial", "unchecked"})
public class SearchForm extends AbstractSearchForm<CollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;

	private CompoundPropertyModel<CollectionVO>	cpmModel;
	private TextField<String>					phenoCollectionIdTxtFld;
	private TextField<String>					phenoCollectionNameTxtFld;
	private TextArea<String>					phenoCollectionDescriptionTxtAreaFld;
	private DropDownChoice<Status>			statusDdc;
	private TextField<String>					phenoCollectionStartDateFld;
	private TextField<String>					phenoCollectionExpiryDateFld;
	
	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<CollectionVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	 private void initFieldTypeDdc()
	 {
		 java.util.Collection<Status> statusCollection = phenotypicService.getStatus();
		 CompoundPropertyModel<CollectionVO> phenoCollectionCpm = cpmModel;
		 PropertyModel<Collection> phenoCollectionPm = new PropertyModel<Collection>(phenoCollectionCpm, au.org.theark.phenotypic.web.Constants.COLLECTION);
		 PropertyModel<Status> statusPm = new PropertyModel<Status>(phenoCollectionPm, au.org.theark.phenotypic.web.Constants.STATUS);
		 ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		 statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.STATUS, statusPm, (List) statusCollection, fieldTypeRenderer);
	 }

	public void initialiseFieldForm()
	{
		phenoCollectionIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_ID);
		phenoCollectionNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_NAME);
		phenoCollectionDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_DESCRIPTION);
		phenoCollectionStartDateFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_START_DATE);
		phenoCollectionExpiryDateFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_EXPIRY_DATE);
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(phenoCollectionIdTxtFld);
		add(phenoCollectionNameTxtFld);
		add(statusDdc);
		add(phenoCollectionDescriptionTxtAreaFld);
		add(phenoCollectionStartDateFld);
		add(phenoCollectionExpiryDateFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		
	}
}