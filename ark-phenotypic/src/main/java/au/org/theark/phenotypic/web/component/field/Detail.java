package au.org.theark.phenotypic.web.component.field;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.field.form.DetailForm;

@SuppressWarnings("serial")
public class Detail extends Panel
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	private DetailForm							detailForm;
	private FeedbackPanel						feedBackPanel;
	private WebMarkupContainer					listContainer;
	private WebMarkupContainer					detailsContainer;
	private WebMarkupContainer					searchPanelContainer;
	private ContainerForm						containerForm;

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
				
				//TODO Implement try catch for exception handling
				// try {
				
				if (fieldVo.getField().getId() == null)
				{
					// Save the Field
					phenotypicService.createField(fieldVo.getField());
					this.info("Field " + fieldVo.getField().getName() + " was created successfully");
					processFeedback(target);
				}
				else
				{
					// Update the Field
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
				target.addComponent(searchPanelContainer);
				target.addComponent(feedBackPanel);
				target.addComponent(detailsContainer);
			}

			protected void processFeedback(AjaxRequestTarget target)
			{
				target.addComponent(feedBackPanel);
			}
			
			protected void processErrors(AjaxRequestTarget target)
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