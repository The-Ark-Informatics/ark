/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.field.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"serial", "unchecked"})
public class SearchForm extends AbstractSearchForm<FieldVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;

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

	 private void initFieldTypeDdc()
	 {
		 java.util.Collection<FieldType> fieldTypeCollection = phenotypicService.getFieldType();
		 CompoundPropertyModel<FieldVO> fieldCpm = cpmModel;
		 PropertyModel<Field> fieldPm = new PropertyModel<Field>(fieldCpm, au.org.theark.phenotypic.web.Constants.FIELD);
		 PropertyModel<FieldType> fieldTypePm = new PropertyModel<FieldType>(fieldPm, "fieldType");
		 //au.org.theark.phenotypic.web.Constants.FIELD_TYPE
		 ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FIELD_TYPE_NAME, au.org.theark.phenotypic.web.Constants.FIELD_TYPE_ID);
		 fieldTypeDdc = new DropDownChoice<FieldType>(au.org.theark.phenotypic.web.Constants.FIELD_TYPE, fieldTypePm, (List) fieldTypeCollection, fieldTypeRenderer);
	 }

	public void initialiseFieldForm()
	{
		fieldIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_ID);
		fieldNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_NAME);
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(fieldIdTxtFld);
		add(fieldNameTxtFld);
		add(fieldTypeDdc);
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
