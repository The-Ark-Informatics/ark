package au.org.theark.study.web.component.mydetails.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.ConfigField;
import au.org.theark.core.model.config.entity.UserConfig;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UserConfigListVO;
import au.org.theark.core.vo.UserConfigVO;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.study.service.IUserService;

public class MyConfigsForm extends Form<UserConfigListVO>{

	private FeedbackPanel	feedbackPanel;

	private ModalWindow	modalWindow;

	private static final long	serialVersionUID	= 1L;

	private transient static Logger log = LoggerFactory.getLogger(MyConfigsForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = "userService")
	private IUserService					iUserService;

	private ArkUser currentUser = new ArkUser();

	private AjaxButton	saveButton;
	private AjaxButton	closeButton;
	private AjaxEditorButton newbutton;

	private AbstractListEditor<UserConfigVO>	listEditor;

	private DropDownChoice<ConfigField>	configFieldsDdc;

	protected TextField<String>	valueTxtFld;

	protected WebMarkupContainer container = new WebMarkupContainer("container");


	public MyConfigsForm(String id, IModel<UserConfigListVO> model, final FeedbackPanel feedbackPanel, ModalWindow modalWindow) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;

		setMultiPart(true);

		try {
			currentUser = iArkCommonService.getArkUser((String)SecurityUtils.getSubject().getPrincipal());
		}
		catch (EntityNotFoundException e) {
			log.info("Entity not found");
			e.printStackTrace();
		}

		model.getObject().setUserConfigVOs(iArkCommonService.getUserConfigVOs(currentUser));
	}

	@SuppressWarnings({ "unchecked" })
	public void initialiseForm() {
		
		saveButton = new AjaxButton("confsave") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processFeedback(target, feedbackPanel);
			}
		};

		closeButton = new AjaxButton("confclose") {

			private static final long	serialVersionUID	= 1L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processFeedback(target, feedbackPanel);
			}
		};
		closeButton.setDefaultFormProcessing(false);

		container.add(buildListEditor());

		newbutton = new AjaxEditorButton("new") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				UserConfigVO userConf = new UserConfigVO();
				UserConfig uc = new UserConfig();
				uc.setArkUser(currentUser);
				uc.setConfigField(new ConfigField());
				userConf.setUserConfig(uc);
				listEditor.addItem(userConf);
				refreshNewButton();
				target.add(container);
			}
		};
		newbutton.setDefaultFormProcessing(false);
		refreshNewButton();
		
		container.add(newbutton);

		container.setOutputMarkupId(true);
		add(container);
		add(saveButton);
		add(closeButton);
	}
	
	protected void refreshNewButton() {
		if(getModelObject().getUserConfigVOs().size() >= iArkCommonService.getAllConfigFields().size()) {
			newbutton.setEnabled(false);
		} else {
			newbutton.setEnabled(true);
		}
	}

	protected void onSave(AjaxRequestTarget target) {
		List<UserConfig> userConfigList = new ArrayList<UserConfig>();
		for(UserConfigVO ucvo : getModelObject().getUserConfigVOs()) {
			ucvo.getUserConfig().setArkUser(currentUser);
			userConfigList.add(ucvo.getUserConfig());
		}

		try {
			iArkCommonService.createUserConfigs(userConfigList);
		}
		catch (ArkSystemException e) {
			e.printStackTrace();
		}
		refreshNewButton();
		processFeedback(target, feedbackPanel);
	}

	protected void processFeedback(AjaxRequestTarget target, FeedbackPanel feedbackPanel) {
		target.add(feedbackPanel);
	}

	public AbstractListEditor<UserConfigVO> buildListEditor() {
		listEditor = new AbstractListEditor<UserConfigVO>("userConfigVOs", new PropertyModel(getModelObject(), "userConfigVOs")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPopulateItem(final ListItem<UserConfigVO> item) {
				item.setOutputMarkupId(true);
				item.add(new Label("row", ""+(item.getIndex()+1)));

				initConfigFieldDdc(item);
				PropertyModel pm = new PropertyModel(item.getModelObject(), "userConfig.value");
				valueTxtFld = new TextField<String>("value", new PropertyModel<String>(item.getModelObject(), "userConfig.value"));
				valueTxtFld.setRequired(true);
				item.add(valueTxtFld);
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					private static final long	serialVersionUID	= -8887455455175404701L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

		};
		return listEditor;
	}

	protected Collection<ConfigField> getModelConfigs() {
		Collection<ConfigField> configFieldList = new ArrayList<ConfigField>();
		for(UserConfigVO vo : getModelObject().getUserConfigVOs()) {
			configFieldList.add(vo.getUserConfig().getConfigField());
		}
		return configFieldList;
	}

	protected boolean contains(ConfigField cf, Collection<ConfigField> fields) {
		for(ConfigField c : fields) {
			if(c.getName().equalsIgnoreCase(cf.getName())) {
				return true;
			}
		}
		return false;
	}

	protected Collection<ConfigField> removeAll(Collection<ConfigField> listone, Collection<ConfigField> listtwo) {
		Collection<ConfigField> result = new ArrayList<ConfigField>();
		for(Iterator<ConfigField> iter = listone.iterator(); iter.hasNext();) {
			ConfigField current = iter.next();
			if(!contains(current, listtwo)) {
				result.add(current);
			}
		}

		return result;
	}

	protected void initConfigFieldDdc(final ListItem<UserConfigVO> item) {
		Collection<ConfigField> configFieldList = new ArrayList<ConfigField>();

		ChoiceRenderer<ConfigField> choiceRenderer = new ChoiceRenderer<ConfigField>("description", "id");
		if(item.getModelObject().getUserConfig().getValue() == null) {
			configFieldList = removeAll(iArkCommonService.getAllConfigFields(), getModelConfigs());
			configFieldsDdc = new DropDownChoice<ConfigField>("configField", 
					new PropertyModel<ConfigField>(item.getModelObject().getUserConfig(), "configField"),
					(List<ConfigField>) configFieldList, choiceRenderer);
		} else {
			configFieldList.add(item.getModelObject().getUserConfig().getConfigField());
			configFieldsDdc = new DropDownChoice<ConfigField>("configField", 
					new PropertyModel<ConfigField>(item.getModelObject().getUserConfig(), "configField"),
					(List<ConfigField>) configFieldList, choiceRenderer);
		}

		configFieldsDdc.setRequired(true);

		item.add(new AjaxEditorButton(Constants.DELETE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				iArkCommonService.deleteUserConfig(item.getModelObject().getUserConfig());
				listEditor.removeItem(item);
				refreshNewButton();
				target.add(container);
			}
		}.setDefaultFormProcessing(false).setVisible(item.getIndex()>=0));

		item.add(configFieldsDdc);
	}

}