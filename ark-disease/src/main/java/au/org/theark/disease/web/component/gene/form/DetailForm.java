package au.org.theark.disease.web.component.gene.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.disease.entity.Position;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.GeneVO;
import au.org.theark.disease.web.component.gene.AssociatedDiseasePalettePanel;

public class DetailForm extends AbstractDetailForm<GeneVO> {

	private static final long serialVersionUID = 1L;

	private WebMarkupContainer arkContextMarkupContainer;

	private TextField<String> name;
	private AssociatedDiseasePalettePanel<GeneVO> associatedDiseasePalettePanel;

	private AbstractListEditor<Position> listEditor;
	private AjaxEditorButton newEditorButton;

	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;

	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
	}

	@Override
	public void onBeforeRender() {
		associatedDiseasePalettePanel = new AssociatedDiseasePalettePanel<GeneVO>("associatedDiseasePalette", containerForm.getModel());
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(associatedDiseasePalettePanel);

		if(!isNew()) deleteButton.setVisible(true);
		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {

		name = new TextField<String>("gene.name");

		IModel<List<Position>> model = new LoadableDetachableModel<List<Position>>() {
			private static final long serialVersionUID = 1L;

			@Override 
			protected List<Position> load() {
				ArrayList<Position> positions = new ArrayList<Position>(containerForm.getModelObject().getGene().getPositions());
				Collections.sort(positions, new Comparator<Position>() {
					public int compare(Position o1, Position o2) {
						return o1.getId().compareTo(o2.getId());
					}
				});
				return positions;
			}
		};

		listEditor = new AbstractListEditor<Position>("listEditor", model) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPopulateItem(final ListItem<Position> item) {
				Position position = item.getModelObject();
				item.add(new Label("id", new PropertyModel<String>(position, "id")));
				item.add(new TextField<String>("name", new PropertyModel<String>(position, "name")));

				item.add(new AjaxListDeleteButton(Constants.DELETE, new Model<String>("Are you sure?"), new Model<String>("Delete")) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
						int idx = getItem().getIndex();
						for (int i = idx + 1; i < getItem().getParent().size(); i++) {
							ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
							item.setIndex(item.getIndex() - 1);
						}
						Position p = item.getModelObject();
						try {
							iArkDiseaseService.delete(p);
							getList().remove(idx);
							getEditor().remove(getItem());
							target.add(form);
							deleteCompleted("Column '" + p.getName() + "' deleted successfully.", true);
						} catch (Exception e) {
							target.add(form);
							deleteCompleted("Error deleting column '" + p.getName() + "'. Column has data associated with it.", false);
						}
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						onDeleteConfirmed(target, form);
						target.add(form);
						target.add(feedBackPanel);
					}

				});

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel<Object>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));

			}
		};

		newEditorButton = new AjaxEditorButton(Constants.NEW) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Position position = new Position();
				position.setGene(containerForm.getModelObject().getGene());
				listEditor.addItem(position);
				target.add(form);
			}
		};

		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}
	
	protected void deleteCompleted(String feedback, boolean successful) {
		if(successful) { 
			//this.info(feedback);
			this.deleteInformation();
		} else {
			this.error(feedback);
		}
	}

	@Override
	protected void attachValidators() {
		name.setRequired(true).setLabel(new StringResourceModel("gene.name.required", this, null));
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(name);
		arkCrudContainerVO.getDetailPanelFormContainer().add(listEditor);
		arkCrudContainerVO.getDetailPanelFormContainer().add(newEditorButton);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		GeneVO geneVO = new GeneVO();
		geneVO.getGene().setStudy(study);
		containerForm.setModelObject(geneVO);
	}

	@Override
	protected void onSave(Form<GeneVO> containerForm, AjaxRequestTarget target) {
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId == null) {
			// No study in context
			this.error("There is no study selected. Please select a study to manage genes.");
			processErrors(target);
		} else {
			Gene gene = containerForm.getModelObject().getGene();
			gene.setStudy(iArkCommonService.getStudy(sessionStudyId));
			gene.setPositions(new HashSet<Position>(listEditor.getModelObject()));
			gene.setDiseases(new HashSet<Disease>(containerForm.getModelObject().getSelectedDiseases()));

			if(isNew()) {
				iArkDiseaseService.save(gene);
			} else {
				iArkDiseaseService.update(gene);
			}
		}

	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		Gene gene = containerForm.getModelObject().getGene();
		if(gene != null) {
			iArkDiseaseService.delete(gene);
		}
		editCancelProcess(target);
		onCancel(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected boolean isNew() {
		return containerForm.getModelObject().getGene().getId() == null;
	}
}