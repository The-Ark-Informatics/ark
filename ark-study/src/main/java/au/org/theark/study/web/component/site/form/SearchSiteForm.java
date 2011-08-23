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
package au.org.theark.study.web.component.site.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.study.web.Constants;

public class SearchSiteForm extends Form<SiteModelVO> {

	private TextField<String>			siteNameTxtFld;
	private DropDownChoice<Person>	siteContactDDC;

	private List<Person>					availableContactList;

	private AjaxButton					searchButton;
	private AjaxButton					newButton;
	private Button							resetButton;

	public SearchSiteForm(String id, CompoundPropertyModel<SiteModelVO> model, List<Person> availablePersons) {

		super(id, model);

		// siteIdTxtFld =new TextField<String>(Constants.STUDY_SITE_KEY);
		siteNameTxtFld = new TextField<String>("siteVo.siteName");
		availableContactList = availablePersons;

		newButton = new AjaxButton(Constants.NEW) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible
				onNew(target);
			}

		};

		searchButton = new AjaxButton(Constants.SEARCH) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible
				onSearch(target);
			}
		};

		resetButton = new Button(Constants.RESET) {
			public void onSubmit() {
				onReset();
			}
		};
		addComponentsToForm();
	}

	private void addComponentsToForm() {
		add(siteNameTxtFld);
		add(searchButton);
		add(resetButton);
		add(newButton);
	}

	protected void onSearch(AjaxRequestTarget target) {
	}

	protected void onNew(AjaxRequestTarget target) {
	}

	// A non-ajax function
	protected void onReset() {
		clearInput();
		updateFormComponentModels();

	}

}
