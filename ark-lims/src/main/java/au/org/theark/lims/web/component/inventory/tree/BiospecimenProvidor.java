package au.org.theark.lims.web.component.inventory.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.ITreeProvider;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.lims.service.ILimsService;

/**
 * A provider of {@link Biospecimen}s.
 * @author cellis
 *
 */
public class BiospecimenProvidor implements ITreeProvider<Biospecimen>
{

	private static final long serialVersionUID = 1L;
	private ILimsService iLimsService;

	/**
	 * All root {@link Biospecimen}s.
	 */
	private static List<Biospecimen> roots = new ArrayList<Biospecimen>();


	public BiospecimenProvidor(ILimsService iLimsService)
	{
		this.iLimsService = iLimsService;
		Biospecimen biospecimen = new Biospecimen();
		biospecimen.setBiospecimenUid("SLP#21");
		biospecimen.setParent(null);
		roots = iLimsService.searchPageableBiospecimens(biospecimen, 0, 10);
	}

	/**
	 * Nothing to do.
	 */
	public void detach()
	{
	}

	public Iterator<Biospecimen> getRoots()
	{
		return roots.iterator();
	}

	public boolean hasChildren(Biospecimen Biospecimen)
	{
		return Biospecimen.getParent() == null || !Biospecimen.getChildren().isEmpty();
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

