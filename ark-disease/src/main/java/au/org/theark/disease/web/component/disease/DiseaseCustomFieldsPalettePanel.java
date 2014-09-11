package au.org.theark.disease.web.component.disease;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.web.component.palette.ArkPalette;

public class DiseaseCustomFieldsPalettePanel<DiseaseVO> extends Panel {

	private static final long serialVersionUID = 1L;
	
private static Logger log = LoggerFactory.getLogger(DiseaseCustomFieldsPalettePanel.class);
	
	protected Label diseaseCustomFieldLabel;
	protected ArkPalette<CustomField> diseaseCustomFieldsPalette;
	protected Label diseaseCustomFieldNote;
	
	public DiseaseCustomFieldsPalettePanel(String id, IModel<DiseaseVO> iModel) {
		super(id, iModel);
		initAssociatedGenePalette();
		addComponents();
	}
	
	private void initAssociatedGenePalette() {
		log.info("" + getDefaultModelObject());
		diseaseCustomFieldLabel = new Label("diseaseCustomFieldLabel", "Associated Custom Fields:");
		PropertyModel<List<CustomField>> availableGenesPm = new PropertyModel<List<CustomField>>(getDefaultModelObject(), "availableCustomFields");
		PropertyModel<List<CustomField>> selectedGenesPm = new PropertyModel<List<CustomField>>(getDefaultModelObject(), "selectedCustomFields");
		IChoiceRenderer<CustomField> renderer = new ChoiceRenderer<CustomField>("name", "name");
		
		diseaseCustomFieldsPalette = new ArkPalette("diseaseCustomFieldsPalette", selectedGenesPm, availableGenesPm, renderer, Constants.PALETTE_ROWS, false);
		diseaseCustomFieldNote = new Label("diseaseCustomFieldNote", "");

		setVisible(!availableGenesPm.getObject().isEmpty());
	}
	
	private void addComponents() {
		add(diseaseCustomFieldsPalette);
		add(diseaseCustomFieldLabel);
		add(diseaseCustomFieldNote);
	}

	
}
