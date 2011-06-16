package au.org.theark.lims.model.vo;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

public class LimsVO
{
	protected LinkSubjectStudy LinkSubjectStudy;  
	protected BioCollection bioCollection;
	protected Biospecimen biospecimen;
	
	/** A List of bioCollection(s) for the linkSubjectStudy in context*/
	protected java.util.List<BioCollection> bioCollectionList;
	
	protected int mode;
	
	public LimsVO()
	{
		this.LinkSubjectStudy = new LinkSubjectStudy();
		this.bioCollection = new BioCollection();
		this.biospecimen = new Biospecimen();
	}

	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy()
	{
		return LinkSubjectStudy;
	}

	/**
	 * @param linkSubjectStudy the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy)
	{
		LinkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the limsCollection
	 */
	public BioCollection getBioCollection()
	{
		return bioCollection;
	}

	/**
	 * @param bioCollection the bioCollection to set
	 */
	public void setBioCollection(BioCollection bioCollection)
	{
		this.bioCollection = bioCollection;
	}

	/**
	 * @return the biospecimen
	 */
	public Biospecimen getBiospecimen()
	{
		return biospecimen;
	}

	/**
	 * @param biospecimen the biospecimen to set
	 */
	public void setBiospecimen(Biospecimen biospecimen)
	{
		this.biospecimen = biospecimen;
	}

	/**
	 * @return the bioCollectionCollection
	 */
	public java.util.List<BioCollection> getLimsCollectionList()
	{
		return bioCollectionList;
	}

	/**
	 * @param bioCollectionList the bioCollectionList to set
	 */
	public void setLimsCollectionList(java.util.List<BioCollection> bioCollectionList)
	{
		this.bioCollectionList = bioCollectionList;
	}

	/**
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	
}
