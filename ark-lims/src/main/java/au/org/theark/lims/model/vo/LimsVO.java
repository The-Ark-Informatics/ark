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

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

public class LimsVO implements Serializable {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 3823264588506863044L;
	protected LinkSubjectStudy						LinkSubjectStudy;
	protected BioCollection							bioCollection;
	protected Biospecimen							biospecimen;
	protected BioTransaction						bioTransaction;
	protected InvSite									invSite;
	protected InvTank									invTank;
	protected InvTray									invTray;
	protected InvBox									invBox;

	/** A List of bioCollection(s) for the linkSubjectStudy in context */
	protected java.util.List<BioCollection>	bioCollectionList;

	/** A List of biospecimen(s) for the linkSubjectStudy in context */
	protected java.util.List<Biospecimen>		biospecimenList;

	/** A List of bioTransaction(s) for the biospecimen in context */
	protected java.util.List<BioTransaction>	bioTransactionList;

	/** A List of invSite(s) for the study in context */
	protected java.util.List<InvSite>			invSiteList;

	protected int										mode;

	public LimsVO() {
		this.LinkSubjectStudy = new LinkSubjectStudy();
		this.bioCollection = new BioCollection();
		this.biospecimen = new Biospecimen();
		this.bioCollectionList = new ArrayList<BioCollection>();
		this.biospecimenList = new ArrayList<Biospecimen>();
		this.bioTransactionList = new ArrayList<BioTransaction>();
		this.invSiteList = new ArrayList<InvSite>();
		this.invSite = new InvSite();
		this.invTank = new InvTank();
		this.invTray = new InvTray();
		this.invBox = new InvBox();
	}

	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy() {
		return LinkSubjectStudy;
	}

	/**
	 * @param linkSubjectStudy
	 *           the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		LinkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the limsCollection
	 */
	public BioCollection getBioCollection() {
		return bioCollection;
	}

	/**
	 * @param bioCollection
	 *           the bioCollection to set
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
	 * @param biospecimen
	 *           the biospecimen to set
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
	 * @param bioTransaction
	 *           the bioTransaction to set
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
	 * @param invSite
	 *           the invSite to set
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
	 * @return the bioCollectionCollection
	 */
	public java.util.List<BioCollection> getBioCollectionList() {
		return bioCollectionList;
	}

	/**
	 * @param bioCollectionList
	 *           the bioCollectionList to set
	 */
	public void setBioCollectionList(java.util.List<BioCollection> bioCollectionList) {
		this.bioCollectionList = bioCollectionList;
	}

	/**
	 * @return the biospecimenList
	 */
	public java.util.List<Biospecimen> getBiospecimenList() {
		return biospecimenList;
	}

	/**
	 * @param biospecimenList
	 *           the biospecimenList to set
	 */
	public void setBiospecimenList(java.util.List<Biospecimen> biospecimenList) {
		this.biospecimenList = biospecimenList;
	}

	/**
	 * @return the bioTransactionList
	 */
	public java.util.List<BioTransaction> getBioTransactionList() {
		return bioTransactionList;
	}

	/**
	 * @param bioTransactionList
	 *           the bioTransactionList to set
	 */
	public void setBioTransactionList(java.util.List<BioTransaction> bioTransactionList) {
		this.bioTransactionList = bioTransactionList;
	}

	/**
	 * @return the invSiteList
	 */
	public java.util.List<InvSite> getInvSiteList() {
		return invSiteList;
	}

	/**
	 * @param invSiteList
	 *           the invSiteList to set
	 */
	public void setInvSiteList(java.util.List<InvSite> invSiteList) {
		this.invSiteList = invSiteList;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

}
