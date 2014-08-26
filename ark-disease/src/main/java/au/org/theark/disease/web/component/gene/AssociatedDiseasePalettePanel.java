package au.org.theark.disease.web.component.gene;

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
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.web.component.palette.ArkPalette;

public class AssociatedDiseasePalettePanel<GeneVO> extends Panel {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(AssociatedDiseasePalettePanel.class);
	
	protected Label associatedDiseaseLabel;
	protected ArkPalette<Disease> assignedDiseasePallete;
	protected Label associatedDiseaseNote;
	
	public AssociatedDiseasePalettePanel(String id, IModel<GeneVO> iModel) {
		super(id, iModel);
		initAssociatedDiseasePalette();
		addComponents();
	}
	
	private void initAssociatedDiseasePalette() {
		log.info("" + getDefaultModelObject());
		associatedDiseaseLabel = new Label("associatedDiseaseLabel", "Associated Disease:");
		PropertyModel<List<Disease>> availableDiseasePm = new PropertyModel<List<Disease>>(getDefaultModelObject(), "availableDiseases");
		PropertyModel<List<Disease>> selectedDiseasePm = new PropertyModel<List<Disease>>(getDefaultModelObject(), "selectedDiseases");
		IChoiceRenderer<Disease> renderer = new ChoiceRenderer<Disease>("name", "name");
		
		assignedDiseasePallete = new ArkPalette("assignedDiseasePalette", selectedDiseasePm, availableDiseasePm, renderer, Constants.PALETTE_ROWS, false);
		associatedDiseaseNote = new Label("associatedDiseaseNote", "");

		setVisible(!availableDiseasePm.getObject().isEmpty());
	}
	
	private void addComponents() {
		add(assignedDiseasePallete);
		add(associatedDiseaseLabel);
		add(associatedDiseaseNote);
	}
	
}
