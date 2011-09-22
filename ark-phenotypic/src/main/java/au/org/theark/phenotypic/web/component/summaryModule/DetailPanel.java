/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.phenotypic.web.component.summaryModule;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.summaryModule.form.ContainerForm;
import au.org.theark.phenotypic.web.component.summaryModule.form.DetailForm;

@SuppressWarnings("serial")
public class DetailPanel extends Panel {
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;

	private DetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	listContainer;
	private WebMarkupContainer	detailsContainer;
	private WebMarkupContainer	searchPanelContainer;
	private ContainerForm		containerForm;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private ModalWindow			selectModalWindow;

	public DetailPanel(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer, WebMarkupContainer searchPanelContainer,
			ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailPanelFormContainer) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", this, listContainer, detailsContainer, containerForm, viewButtonContainer, editButtonContainer, detailPanelFormContainer) {
			protected void onSave(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {
				// TODO Implement try catch for exception handling
				// try {
				if (collectionVo.getPhenoCollection().getId() == null) {
					// Save the Collection
					iPhenotypicService.createCollection(collectionVo.getPhenoCollection());
					this.info("Collection " + collectionVo.getPhenoCollection().getName() + " was created successfully");
					processFeedback(target);
				}
				else {
					// Update the Collection
					iPhenotypicService.updateCollection(collectionVo.getPhenoCollection());
					this.info("Collection " + collectionVo.getPhenoCollection().getName() + " was updated successfully");
					processFeedback(target);
				}

				postSaveUpdate(target);

				// TODO Implement Exceptions in PhentoypicService
				// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
				// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
				// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
			}

			protected void onCancel(AjaxRequestTarget target) {
				PhenoCollectionVO collectionVo = new PhenoCollectionVO();
				containerForm.setModelObject(collectionVo);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				target.add(searchPanelContainer);
				target.add(feedBackPanel);
				target.add(detailsContainer);
			}

			protected void onDelete(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {
				//selectModalWindow.show(target);
				target.add(selectModalWindow);
			}

			// On click of Edit button, allow form to be editable
			protected void onEdit(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {
				detailPanelFormContainer.setEnabled(true);
				editButtonContainer.setVisible(true);
				viewButtonContainer.setVisible(false);
				detailForm.getDeleteButton().setEnabled(true);
				detailForm.getDeleteButton().setVisible(true);

				target.add(feedBackPanel);
				target.add(detailPanelFormContainer);
				target.add(viewButtonContainer);
				target.add(editButtonContainer);
			}

			protected void processFeedback(AjaxRequestTarget target) {
				target.add(feedBackPanel);
			}

			protected void processErrors(AjaxRequestTarget target) {
				target.add(feedBackPanel);
			}
		};

		detailForm.initialiseForm();
		//detailPanelFormContainer.add(initialiseModalWindow());
		add(detailForm);
	}

	private void postSaveUpdate(AjaxRequestTarget target) {
		// Button containers
		// View Collection, thus view container visible
		viewButtonContainer.setVisible(true);
		editButtonContainer.setVisible(false);
		detailPanelFormContainer.setEnabled(false);

		target.add(feedBackPanel);
		target.add(viewButtonContainer);
		target.add(editButtonContainer);
	}

	private ModalWindow initialiseModalWindow() {
		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow") {

			protected void onSelect(AjaxRequestTarget target, String selection) {
				try {
					iPhenotypicService.deleteCollection(containerForm.getModelObject().getPhenoCollection());
					this.info("Collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was deleted successfully");
				}
				catch (ArkSystemException e) {
					this.error(e.getMessage());
				}
				catch (EntityCannotBeRemoved e) {
					this.error(e.getMessage());
				}

				// Display delete confirmation message
				target.add(feedBackPanel);

				// TODO Implement Exceptions in PhentoypicService
				// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
				// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
				// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

				// Close the confirm modal window
				close(target);

				// Move focus back to Search form
				PhenoCollectionVO collectionVo = new PhenoCollectionVO();
				containerForm.setModelObject(collectionVo);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				target.add(searchPanelContainer);
				target.add(detailsContainer);
			}

			protected void onCancel(AjaxRequestTarget target) {
				// Handle Cancel action
				// Close the confirm modal window
				close(target);

				// Go back into Edit mode (and remove feedback, if straight after "New")
				detailPanelFormContainer.setEnabled(true);
				editButtonContainer.setVisible(true);
				viewButtonContainer.setVisible(false);
				detailForm.getDeleteButton().setEnabled(true);
				detailForm.getDeleteButton().setVisible(true);

				target.add(detailPanelFormContainer);
				target.add(viewButtonContainer);
				target.add(editButtonContainer);
			}
		};
		return selectModalWindow;
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm) {
		this.detailForm = detailsForm;
	}
}
