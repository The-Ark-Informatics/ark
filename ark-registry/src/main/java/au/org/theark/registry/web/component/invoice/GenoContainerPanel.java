/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.web.component.invoice;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class GenoContainerPanel extends AbstractContainerPanel<Pipeline> {


	private static final long serialVersionUID = 1L;
	private ContainerForm					containerForm;

	// Panels
	private DetailPanel						detailPanel;
	
	@Override
	protected void onBeforeRender() {
		//arkCrudContainerVO.showDetailPanelInEditMode(AjaxRequestTarget.get());
		super.onBeforeRender();
	}
	
	/**
	 * @param id
	 */
	public GenoContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<Pipeline>(new Pipeline());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
			
		add(containerForm);	
			
	}
	
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailsPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		return arkCrudContainerVO.getDetailPanelContainer();
	}
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}

}
