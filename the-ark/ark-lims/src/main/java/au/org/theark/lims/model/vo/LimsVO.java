/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.lims.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;

public class LimsVO implements Serializable {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 3823264588506863044L;
	protected Study									study;
	protected LinkSubjectStudy						linkSubjectStudy;
	protected BioCollection							bioCollection;
	protected Biospecimen							biospecimen;
	protected BioTransaction						bioTransaction;
	protected InvSite									invSite;
	protected InvTank									invTank;
	protected InvTray									invTray;
	protected InvBox									invBox;
	/* A light weight VO that will represent a read only view of the location details of a Biospecimen */
	protected BiospecimenLocationVO				biospecimenLocationVO;

	/** A List of bioCollection(s) for the linkSubjectStudy in context */
	protected List<BioCollection>	bioCollectionList;

	/** A List of biospecimen(s) for the linkSubjectStudy in context */
	protected List<Biospecimen>		biospecimenList;

	/** A List of bioTransaction(s) for the biospecimen in context */
	protected List<BioTransaction>	bioTransactionList;

	/** A List of invSite(s) for the study in context */
	protected List<InvSite>			invSiteList;

	/** A List of Study(s) for the user in context */
	protected List<Study>			studyList;

	public LimsVO() {
		this.study = new Study();
		this.linkSubjectStudy = new LinkSubjectStudy();
		this.bioCollection = new BioCollection();
		this.biospecimen = new Biospecimen();
		this.bioTransaction = new BioTransaction();
		this.invSite = new InvSite();
		this.invTank = new InvTank();
		this.invTray = new InvTray();
		this.invBox = new InvBox();
		this.bioCollectionList = new ArrayList<BioCollection>(0);
		this.biospecimenList = new ArrayList<Biospecimen>(0);
		this.bioTransactionList = new ArrayList<BioTransaction>(0);
		this.invSiteList = new ArrayList<InvSite>(0);
		this.studyList = new ArrayList<Study>(0);
	}

	/**
	 * @return the study
	 */
	public Study getStudy() {
		return study;
	}

	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	/**
	 * @param linkSubjectStudy the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the bioCollection
	 */
	public BioCollection getBioCollection() {
		return bioCollection;
	}

	/**
	 * @param bioCollection the bioCollection to set
	 */
	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}

	/**
	 * @return the biospecimen
	 */
	public Biospecimen getBiospecimen() {
		return biospecimen;
	}

	/**
	 * @param biospecimen the biospecimen to set
	 */
	public void setBiospecimen(Biospecimen biospecimen) {
		this.biospecimen = biospecimen;
	}

	/**
	 * @return the bioTransaction
	 */
	public BioTransaction getBioTransaction() {
		return bioTransaction;
	}

	/**
	 * @param bioTransaction the bioTransaction to set
	 */
	public void setBioTransaction(BioTransaction bioTransaction) {
		this.bioTransaction = bioTransaction;
	}

	/**
	 * @return the invSite
	 */
	public InvSite getInvSite() {
		return invSite;
	}

	/**
	 * @param invSite the invSite to set
	 */
	public void setInvSite(InvSite invSite) {
		this.invSite = invSite;
	}

	/**
	 * @return the invTank
	 */
	public InvTank getInvTank() {
		return invTank;
	}

	/**
	 * @param invTank the invTank to set
	 */
	public void setInvTank(InvTank invTank) {
		this.invTank = invTank;
	}

	/**
	 * @return the invTray
	 */
	public InvTray getInvTray() {
		return invTray;
	}

	/**
	 * @param invTray the invTray to set
	 */
	public void setInvTray(InvTray invTray) {
		this.invTray = invTray;
	}

	/**
	 * @return the invBox
	 */
	public InvBox getInvBox() {
		return invBox;
	}

	/**
	 * @param invBox the invBox to set
	 */
	public void setInvBox(InvBox invBox) {
		this.invBox = invBox;
	}

	/**
	 * @return the bioCollectionList
	 */
	public List<BioCollection> getBioCollectionList() {
		return bioCollectionList;
	}

	/**
	 * @param bioCollectionList the bioCollectionList to set
	 */
	public void setBioCollectionList(List<BioCollection> bioCollectionList) {
		this.bioCollectionList = bioCollectionList;
	}

	/**
	 * @return the biospecimenList
	 */
	public List<Biospecimen> getBiospecimenList() {
		return biospecimenList;
	}

	/**
	 * @param biospecimenList the biospecimenList to set
	 */
	public void setBiospecimenList(List<Biospecimen> biospecimenList) {
		this.biospecimenList = biospecimenList;
	}

	/**
	 * @return the bioTransactionList
	 */
	public List<BioTransaction> getBioTransactionList() {
		return bioTransactionList;
	}

	/**
	 * @param bioTransactionList the bioTransactionList to set
	 */
	public void setBioTransactionList(List<BioTransaction> bioTransactionList) {
		this.bioTransactionList = bioTransactionList;
	}

	/**
	 * @return the invSiteList
	 */
	public List<InvSite> getInvSiteList() {
		return invSiteList;
	}

	/**
	 * @param invSiteList the invSiteList to set
	 */
	public void setInvSiteList(List<InvSite> invSiteList) {
		this.invSiteList = invSiteList;
	}

	/**
	 * @return the studyList
	 */
	public List<Study> getStudyList() {
		return studyList;
	}

	/**
	 * @param studyList the studyList to set
	 */
	public void setStudyList(List<Study> studyList) {
		this.studyList = studyList;
	}

	public BiospecimenLocationVO getBiospecimenLocationVO() {
		return biospecimenLocationVO;
	}

	public void setBiospecimenLocationVO(BiospecimenLocationVO biospecimenLocationVO) {
		this.biospecimenLocationVO = biospecimenLocationVO;
	}
}
