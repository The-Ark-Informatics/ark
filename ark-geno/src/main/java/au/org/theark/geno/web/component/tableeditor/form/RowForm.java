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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.component.listeditor.RemoveButton;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.geno.model.vo.RowListVO;
import au.org.theark.geno.model.vo.RowVO;
import au.org.theark.geno.service.IArkGenoService;
import au.org.theark.study.service.IStudyService;

public class RowForm extends AbstractDetailForm<RowListVO>{

	private static final long serialVersionUID = 1L;

	static Logger log = LoggerFactory.getLogger(RowForm.class);

	private WebMarkupContainer arkContextMarkupContainer;
	private AbstractListEditor<RowVO> rows;

	private Study study;
	
	@SpringBean(name = "arkGenoService")
	private IArkGenoService											iArkGenoService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	private AbstractDetailModalWindow modalWindow;
	
	public RowForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, Form<RowListVO> containerForm, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
		this.modalWindow = modalWindow;
	}

	@Override
	public void initialiseForm() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		
		List<RowVO> rowVOs = new ArrayList<RowVO>();
		
		for(Row r : iArkGenoService.getGenoTableRows(study)) {
			rowVOs.add(new RowVO(r));
		}
		
		setModel(new Model<RowListVO>(new RowListVO(rowVOs)));
		setModelObject(new RowListVO(rowVOs));

		rows = new AbstractListEditor<RowVO>("listView", new PropertyModel<List<RowVO>>(getModelObject(), "rows")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onPopulateItem(final ListItem<RowVO> item) {
				
				RowVO rowVO = item.getModelObject();
				Row row = rowVO.getRow();
				final Row rowFinal = row;
				
				item.add(new Label("rows.id", (row.getId() == null ? "" : Long.toString(row.getId()))));
				
				item.add(new TextField<String>("rows.name", new PropertyModel<String>(rowVO, "row.name")));
				
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
							iArkGenoService.delete(rowFinal);
							getList().remove(idx);
							getEditor().remove(getItem());
							target.add(form);
							deleteCompleted("Row '" + rowFinal.getName() + "' deleted successfully.", true);
						} catch (Exception e) {
							target.add(form);
							deleteCompleted("Error deleting row '" + rowFinal.getName() + "'. Row has data associated with it.", false);
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
		add(rows);

		add(new AjaxEditorButton(Constants.NEW) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				RowVO rowVO = new RowVO();
				rows.addItem(rowVO);
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
				target.add(form);
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

	@Override
	protected void addDetailFormComponents() {
		add(rows);
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
	protected void onSave(Form<RowListVO> containerForm, AjaxRequestTarget target) {
		getSession().cleanupFeedbackMessages(); 
		List<Row> rowList = new ArrayList<Row>(0);
		
		for(RowVO rowVO : getModelObject().getRows()) {
			boolean failed = false;
			Row row = rowVO.getRow();
			if(row.getStudy() == null) {
				row.setStudy(study);
			}
			if(row.getName() == null || row.getName().isEmpty()) { 
				failed = true;
				this.error("Row needs a name");
			}
			if(rowList.contains(row)) {
				this.error("Column '" + row.getName() + "' already exists in table");
				failed = true;
			}
			if(!failed) rowList.add(row);
		}
		if(!rowList.isEmpty() &&  rowList.size() == getModelObject().getRows().size()) {
			iArkGenoService.createOrUpdateRows(rowList);
			this.updateInformation();
			//this.info("Rows updated successfully. New Row IDs won't appear until page has been reloaded.");
		}
		target.add(feedBackPanel);
	}

}