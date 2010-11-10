package au.org.theark.phenotypic.web.component.field;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.field.form.DetailForm;

@SuppressWarnings("serial")
public class Detail extends Panel
{
	//private CompoundPropertyModel<FieldVO>	cpm;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private DetailForm							detailForm;

	private FeedbackPanel						feedBackPanel;
	private WebMarkupContainer					listContainer;
	private WebMarkupContainer					detailsContainer;
	private WebMarkupContainer					searchPanelContainer;
	private ContainerForm						containerForm;
	private Study									study;

	public Detail(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer, WebMarkupContainer searchPanelContainer,
			ContainerForm containerForm)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
	}

	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm", this, listContainer, detailsContainer, containerForm)
		{
			protected void onSave(FieldVO fieldVo, AjaxRequestTarget target)
			{
				// Save or update the Field
				// try {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				study = iArkCommonService.getStudy(studyId);
				
				// Force uppercase field name
				fieldVo.getField().setName(fieldVo.getField().getName().toUpperCase());
				
				// Set Study, field type
				fieldVo.getField().setStudy(study);
				fieldVo.getField().setFieldType(fieldVo.getFieldType());
				
				if (fieldVo.getField().getId() == null)
				{
					phenotypicService.createField(fieldVo.getField());
					this.info("Field " + fieldVo.getField().getName() + " was created successfully");
					processFeedback(target);
				}
				else
				{
					phenotypicService.updateField(fieldVo.getField());
					this.info("Field " + fieldVo.getField().getName() + " was updated successfully");
					processFeedback(target);

				}

				//TODO Implement Exceptions in PhentoypicService
				//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
				//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
				//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
				 

			}

			protected void onCancel(AjaxRequestTarget target)
			{
				FieldVO fieldVo = new FieldVO();
				containerForm.setModelObject(fieldVo);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				detailForm.setVisible(false);
				target.addComponent(searchPanelContainer);
				target.addComponent(feedBackPanel);
				target.addComponent(detailsContainer);
			}

			protected void processFeedback(AjaxRequestTarget target)
			{
				target.addComponent(feedBackPanel);
			}
		};

		detailForm.initialiseForm();
		add(detailForm);
	}

	public DetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm)
	{
		this.detailForm = detailsForm;
	}
}