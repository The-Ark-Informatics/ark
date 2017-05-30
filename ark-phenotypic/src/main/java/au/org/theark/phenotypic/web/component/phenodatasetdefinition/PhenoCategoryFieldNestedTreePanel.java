package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.NestedTree;
import wickettree.content.StyledLinkLabel;
import au.org.theark.core.model.pheno.entity.LinkPhenoDataSetCategoryField;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

public class PhenoCategoryFieldNestedTreePanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(PhenoCategoryFieldNestedTreePanel.class);
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService												iPhenotypicService;
	protected CompoundPropertyModel<PhenoDataSetFieldGroupVO>				cpModel;
	private ITreeProvider<Object> provider;
	private IModel<Set<Object>> state;
	public AbstractTree<Object> tree;

	public PhenoCategoryFieldNestedTreePanel(String id, CompoundPropertyModel<PhenoDataSetFieldGroupVO>	cpModel, ITreeProvider<Object> provider, final IModel<Set<Object>> state) {
		super(id);
		setOutputMarkupId(true);
		this.cpModel = cpModel;
		this.provider = provider;
		this.state = state;
		tree = createTree();
		add(tree);
	}

	@SuppressWarnings("unchecked")
	private AbstractTree<Object> createTree() {
		return new NestedTree("tree", provider, state){
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Component newContentComponent(String id, IModel model) {
				if(model.getObject() instanceof PickedPhenoDataSetCategory) {
					PickedPhenoDataSetCategory pickedPhenoDataSetCategory = (PickedPhenoDataSetCategory)model.getObject();
					return new Label(id, pickedPhenoDataSetCategory.getPhenoDataSetCategory().getName());
				}else if(model.getObject() instanceof LinkPhenoDataSetCategoryField) {
					final LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField = (LinkPhenoDataSetCategoryField) model.getObject();
					StyledLinkLabel<String> styledLink = new StyledLinkLabel<String>(id, new Model<String>(linkPhenoDataSetCategoryField.getPhenoDataSetField().getName())) {
						private static final long	serialVersionUID	= 1L;
						@Override
						protected String getStyleClass() {
							return Constants.CSS_STYLE_CLASS_NAME_COLOR_LINK;
						}
						@Override
						protected boolean isClickable() {
							return false;
						}
					};
					
					return styledLink;
				}
				return null;
			}
		};
	}
	public void setProvider(ITreeProvider<Object> provider) {
		this.provider = provider;
	}

	public ITreeProvider<Object> getProvider() {
		return provider;
	}

	public AbstractTree<Object> getTree() {
		return tree;
	}

	public void setTree(AbstractTree<Object> tree) {
		this.tree = tree;
	}
	
}
