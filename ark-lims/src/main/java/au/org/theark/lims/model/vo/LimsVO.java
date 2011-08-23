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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.vo.SubjectVO;

public class LimsVO implements Serializable {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 3823264588506863044L;
	protected LinkSubjectStudy						LinkSubjectStudy;
	protected BioCollection							bioCollection;
	protected Biospecimen							biospecimen;
	protected BioTransaction						bioTransaction;

	/** A List of bioCollection(s) for the linkSubjectStudy in context */
	protected java.util.List<BioCollection>	bioCollectionList;

	/** A List of biospecimen(s) for the linkSubjectStudy in context */
	protected java.util.List<Biospecimen>		biospecimenList;

	/** A List of bioTransaction(s) for the biospecimen in context */
	protected java.util.List<BioTransaction>	bioTransactionList;

	protected int										mode;

	public LimsVO() {
		this.LinkSubjectStudy = new LinkSubjectStudy();
		this.bioCollection = new BioCollection();
		this.biospecimen = new Biospecimen();
		this.bioCollectionList = new ArrayList<BioCollection>();
		this.biospecimenList = new ArrayList<Biospecimen>();
		this.bioTransactionList = new ArrayList<BioTransaction>();
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
