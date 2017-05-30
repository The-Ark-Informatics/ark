package au.org.theark.geno.web.component.tableeditor.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.geno.model.vo.BeamListVO;
import au.org.theark.geno.model.vo.BeamVO;
import au.org.theark.geno.service.IArkGenoService;
import au.org.theark.study.service.IStudyService;

public class BeamForm extends AbstractDetailForm<BeamListVO>{

	private static final long serialVersionUID = 1L;

	static Logger log = LoggerFactory.getLogger(BeamForm.class);

	private AbstractListEditor<BeamVO> beams;

	private Study study;
	
	@SpringBean(name = "arkGenoService")
	private IArkGenoService											iArkGenoService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	private AbstractDetailModalWindow modalWindow;
	
	public BeamForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, Form<BeamListVO> containerForm, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.modalWindow = modalWindow;
	}

	@Override
	public void initialiseForm() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		
		List<BeamVO> beamVOs = new ArrayList<BeamVO>();
		
		for(Beam b : iArkGenoService.getGenoTableBeam(study)) {
			beamVOs.add(new BeamVO(b));
		}
		
		setModel(new Model<BeamListVO>(new BeamListVO(beamVOs)));
		setModelObject(new BeamListVO(beamVOs));
		
		beams = new AbstractListEditor<BeamVO>("listView", new PropertyModel<List<BeamVO>>(getModelObject(), "Beams")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onPopulateItem(final ListItem<BeamVO> item) {
				BeamVO beamVO = item.getModelObject();
				Beam beam = beamVO.getBeam();
				final Beam beamFinal = beam; 
				
				item.add(new Label("beams.id", (beam.getId() == null ? "" : Long.toString(beam.getId()))));			
				item.add(new TextField<String>("beams.name", new PropertyModel<String>(beamVO, "beam.name")));
				item.add(new TextArea<String>("beams.encoded", new PropertyModel<String>(beamVO, "beam.encodedValues")));
				item.add(new TextField<String>("beams.default", new PropertyModel<String>(beamVO, "beam.defaultValue")));
				item.add(new AjaxListDeleteButton(Constants.DELETE, new Model<String>("Are you sure?"), new Model<String>("Delete")) {
					
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
						int idx = getItem().getIndex();
						for (int i = idx + 1; i < getItem().getParent().size(); i++) {
							ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
							item.setIndex(item.getIndex() - 1);
						}
						try {
							iArkGenoService.delete(beamFinal);
							getList().remove(idx);
							getEditor().remove(getItem());
							target.add(form);
							deleteCompleted("Column '" + beamFinal.getName() + "' deleted successfully.", true);
						} catch (Exception e) {
							target.add(form);
							deleteCompleted("Error deleting column '" + beamFinal.getName() + "'. Column has data associated with it.", false);
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
		
		add(beams);
		
		add(new AjaxEditorButton(Constants.NEW) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				BeamVO BeamVO = new BeamVO();
				beams.addItem(BeamVO);
				target.add(form);
			}
		}.setDefaultFormProcessing(false));

		add(new AjaxButton(Constants.SAVE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
		
		add(new AjaxButton("close"){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}						
		});
	}

	@Override
	protected void attachValidators() {
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
	}

	@Override
	protected boolean isNew() {
		return false;
	}

	protected void deleteCompleted(String feedback, boolean successful) {
		if(successful) { 
			this.deleteInformation();
			//this.info(feedback);
		} else {
			this.error(feedback);
		}
	}
	
	@Override
	protected void onSave(Form<BeamListVO> containerForm, AjaxRequestTarget target) {
		getSession().cleanupFeedbackMessages();

		List<Beam> BeamList = new ArrayList<Beam>(0);
		
 		for(BeamVO beamVO : getModelObject().getBeams()) {
			boolean failed = false;
			Beam beam = beamVO.getBeam();
			if(beam.getStudy() == null) {
				beam.setStudy(study);
			}
			if(beam.getName() == null || beam.getName().isEmpty()) {
				this.error("Column requires a name");
				failed = true;
			}
			if(beam.getEncodedValues() == null || beam.getEncodedValues().isEmpty()) {
				this.error("Column requires encoded values");
				failed = true;
			}
			if(BeamList.contains(beam)) {
				this.error("Column '" + beam.getName() + "' already exists in table");
				failed = true;
			}
			if(!failed) BeamList.add(beam);
		}
		if(!BeamList.isEmpty() && BeamList.size() == getModelObject().getBeams().size()) {
			iArkGenoService.createOrUpdateBeams(BeamList);
			this.updateInformation();
			//this.info("Columns updated successfully. New Column IDs won't appear until page has been reloaded.");
		}
		target.add(feedBackPanel);
	}

	@Override
	protected void addDetailFormComponents() {
	}

}