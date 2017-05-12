package au.org.theark.lims.web.component.inventory.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import wickettree.ITreeProvider;

/**
 * A provider of {@link Biospecimen}s.
 * @author cellis
 *
 */
public class BiospecimenProvider implements ITreeProvider<Biospecimen>
{

	private static final long serialVersionUID = 1L;
	private ILimsService iLimsService;
	private CompoundPropertyModel<LimsVO> cpModel;

	/**
	 * All root {@link Biospecimen}s.
	 */
	private static List<Biospecimen> roots = new ArrayList<Biospecimen>();


	public BiospecimenProvider(ILimsService iLimsService, CompoundPropertyModel<LimsVO> cpModel)
	{
		this.iLimsService = iLimsService;
		this.cpModel = cpModel;
	}

	/**
	 * Nothing to do.
	 */
	public void detach()
	{
	}

	public Iterator<Biospecimen> getRoots()
	{
		Biospecimen b =cpModel.getObject().getBiospecimen();
		b.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
		b.setParent(null);
		b.setParentUid(null);
		roots = iLimsService.searchPageableBiospecimens(b, 0, Integer.MAX_VALUE);
		return roots.iterator();
	}

	public boolean hasChildren(Biospecimen biospecimen)
	{
		return biospecimen.getParent() == null || !biospecimen.getChildren().isEmpty();
	}

	public Iterator<Biospecimen> getChildren(final Biospecimen biospecimen)
	{
		Biospecimen childBiospecimen = new Biospecimen();
		childBiospecimen.setParent(biospecimen);
		return iLimsService.searchPageableBiospecimens(childBiospecimen, 0, 100).iterator();
	}
	
	/**
	 * Creates a {@link BiospecimenModel}.
	 */
	public IModel<Biospecimen> model(Biospecimen biospecimen)
	{
		return new Model<Biospecimen>(biospecimen);
	}
}

