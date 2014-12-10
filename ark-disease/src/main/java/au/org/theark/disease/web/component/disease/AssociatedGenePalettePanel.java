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
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.web.component.palette.ArkPalette;
public class AssociatedGenePalettePanel<DiseaseVO> extends Panel {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(AssociatedGenePalettePanel.class);

	protected Label associatedGenesLabel;
	protected ArkPalette<Gene> assignedGenesPalette;
	protected Label associatedGenesNote;

	public AssociatedGenePalettePanel(String id, IModel<DiseaseVO> iModel) {
		super(id, iModel);
		initAssociatedGenePalette();
		addComponents();
	}

	private void initAssociatedGenePalette() {
		associatedGenesLabel = new Label("associatedGenesLabel", "Associated Genes:");
		PropertyModel<List<Gene>> availableGenesPm = new PropertyModel<List<Gene>>(getDefaultModelObject(), "availableGenes");
		PropertyModel<List<Gene>> selectedGenesPm = new PropertyModel<List<Gene>>(getDefaultModelObject(), "selectedGenes");
		IChoiceRenderer<Gene> renderer = new ChoiceRenderer<Gene>("name", "name");

		assignedGenesPalette = new ArkPalette("assignedGenePalette", selectedGenesPm, availableGenesPm, renderer, Constants.PALETTE_ROWS, false);
		associatedGenesNote = new Label("associatedGeneNote", "");
	}

	private void addComponents() {
		add(assignedGenesPalette);
		add(associatedGenesLabel);
		add(associatedGenesNote);
	}

}
