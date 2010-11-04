/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.field.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchForm extends AbstractSearchForm<FieldVO>
{
	// @SpringBean(name = Constants.STUDY_SERVICE)
	// private IStudyService studyService;

	private TextField<String>					fieldIdTxtFld;
	private TextField<String>					fieldNameTxtFld;
	private DropDownChoice<FieldType>		fieldTypeDdc;
	private CompoundPropertyModel<FieldVO>	cpmModel;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<FieldVO> model)
	{
		super(id, model);
		this.cpmModel = model;
		initialiseFieldForm();
	}

	// private void initVitalStatusDdc()
	// {
	// CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
	// PropertyModel<Person> personPm = new PropertyModel<Person>(subjectCpm, Constants.PERSON);
	//
	// PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm, Constants.VITAL_STATUS);
	// Collection<VitalStatus> vitalStatusList = studyService.getVitalStatus();
	// ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.STATUS_NAME, Constants.ID);
	// vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS, vitalStatusPm, (List) vitalStatusList, vitalStatusRenderer);
	// }

	public void initialiseFieldForm()
	{
		fieldIdTxtFld = new TextField<String>(Constants.FIELD_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELD_NAME);

		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(fieldIdTxtFld);
		add(fieldNameTxtFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}

}
