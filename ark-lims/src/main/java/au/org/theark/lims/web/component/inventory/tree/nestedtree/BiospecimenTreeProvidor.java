package au.org.theark.lims.web.component.inventory.tree.nestedtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.ITreeProvider;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;

public class BiospecimenTreeProvidor implements ITreeProvider<Object> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static List<LinkSubjectStudy> roots = new ArrayList<LinkSubjectStudy>();
	private ILimsService iLimsService;
	private CompoundPropertyModel<LimsVO> cpModel;
	
	public BiospecimenTreeProvidor(ILimsService iLimsService, CompoundPropertyModel<LimsVO> cpModel)
	{
		this.iLimsService = iLimsService;
		this.cpModel = cpModel;
		roots.clear();
		roots.add(cpModel.getObject().getLinkSubjectStudy());
	}

	public Iterator getChildren(Object obj) {
		if(obj instanceof LinkSubjectStudy) {
			LinkSubjectStudy lss = (LinkSubjectStudy) obj;
			BioCollection bioCollection = cpModel.getObject().getBioCollection();
			bioCollection.setLinkSubjectStudy(lss);
			return iLimsService.searchPageableBioCollections(bioCollection , 0, 100).iterator();
		}
		if(obj instanceof BioCollection) {
			BioCollection bc = (BioCollection)obj;
			return bc.getBiospecimens().iterator();
		}
		else if (obj instanceof Biospecimen) {
			Biospecimen childBiospecimen = new Biospecimen();
			childBiospecimen.setParent((Biospecimen) obj);
			return iLimsService.searchPageableBiospecimens(childBiospecimen, 0, 100).iterator();
		}
		return null;
	}

	public Iterator getRoots() {
		return roots.iterator();
	}

	public boolean hasChildren(Object obj) {
		if(obj instanceof LinkSubjectStudy) {
			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) obj;
			return iLimsService.hasBioCollections(linkSubjectStudy);
		}
		if(obj instanceof BioCollection) {
			BioCollection bc = (BioCollection)obj;
			return !bc.getBiospecimens().isEmpty();
		}
		else if (obj instanceof Biospecimen) {
			Biospecimen biospecimen = (Biospecimen) obj;
			biospecimen = iLimsService.getBiospecimenByUid(biospecimen.getBiospecimenUid(), biospecimen.getStudy());
			return biospecimen.getParent() == null || !biospecimen.getChildren().isEmpty();
		}
		return false;
	}

	public IModel model(Object obj) {
		if(obj instanceof LinkSubjectStudy) {
			return new Model((LinkSubjectStudy) obj);
		}
		if(obj instanceof BioCollection) {
			return new Model((BioCollection) obj);
		}
		else if (obj instanceof Biospecimen) {
			return new Model((Biospecimen) obj);
		}
		return new Model((BioCollection) obj);
	}

	public void detach() {
		// TODO Auto-generated method stub
		
	}

}
