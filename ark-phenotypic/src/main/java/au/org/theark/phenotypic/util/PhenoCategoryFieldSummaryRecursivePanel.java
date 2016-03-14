
package au.org.theark.phenotypic.util;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;


public class PhenoCategoryFieldSummaryRecursivePanel extends Panel {

	
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param id  the id of this component
	 * @param list  a list where each element is either a string or another list
	 */
	public PhenoCategoryFieldSummaryRecursivePanel(final String id, List<Object> list) {
		super(id);
		add(new Rows("rows", list));
		setVersioned(false);
	}

	/**
	 * The list class.
	 */
	private static class Rows extends ListView<Object> {
		/**
		 * Construct.
		 * 
		 * @param name- name of the component
		 * @param list- a list where each element is either a string or another list
		 */
		public Rows(String name, List<Object> list) {
			super(name, list);
		}
		/**
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void populateItem(ListItem<Object> listItem) {
			AjaxLink link = null;
			Object modelObject = listItem.getDefaultModelObject();

			if (modelObject instanceof List) {
				List lstmodelObject = (List) modelObject;
				link = createSecondLevelItems(lstmodelObject);

				// Lifestyle optionList
				// link = createLifeStyleoptionListWithList(lstmodelObject);

				// create a panel that renders the sub list
				if (link != null) {
					PhenoCategoryFieldSummaryRecursivePanel nested = new PhenoCategoryFieldSummaryRecursivePanel("nested", (List<Object>) modelObject);
					listItem.add(nested);
					// if the current element is a list, we create a dummy row
					// label
					// element as we have to confirm to our HTML definition, and
					// set
					// it's
					// visibility property to false as we do not want LI tags to
					// be
					// rendered.
					WebMarkupContainer row = new WebMarkupContainer("row");
					row.setVisible(false);
					row.add(link);
					// row.add(new WebMarkupContainer("link"));
					listItem.add(row);
				}
			} else {
				link = createFirstLevelItems(modelObject);
				// Lifestyle optionList
				// link = createLifeStyleoptionList(modelObject);

				// if the current element is not a list, we create a dummy panel
				// element to confirm to our HTML definition, and set this
				// panel's
				// visibility property to false to avoid having the UL tag
				// rendered
				if (link != null) {
					PhenoCategoryFieldSummaryRecursivePanel nested = new PhenoCategoryFieldSummaryRecursivePanel("nested", null);
					nested.setVisible(false);
					listItem.add(nested);
					// add the row (with the LI element attached, and the label
					// withthe row's actual value to display
					WebMarkupContainer row = new WebMarkupContainer("row");
					row.add(link);
					// row.add(new Label("link", modelObject.toString()));
					listItem.add(row);
				}
			}
		}

		/**
		 * 
		 * @param link
		 * @param modelObject
		 * @return
		 */
		private AjaxLink createFirstLevelItems(Object modelObject) {
			AjaxLink link = null;
			/*if (YourOptionType.BPM.equals(modelObject)) {
				link = createLink(new BPMPage(trial), YourOptionType.BPM.getName());
				link.setEnabled(false);
				link.add(new AttributeModifier("style",new Model<String>(){
					public String getObject(){
						return "color:#6493d2";
					}
				}));
			}
			if (YourOptionType.BPM_MASTECTOMY.equals(modelObject)) {
				link = createLink(new BPMPage(trial), YourOptionType.BPM_MASTECTOMY.getName());
			}
			if (YourOptionType.BPM_RRSO.equals(modelObject)) {
				link = createLink(new RRSOPage(trial), YourOptionType.BPM_RRSO.getName());
			}
			if (YourOptionType.RRM.equals(modelObject)) {
				link = new AjaxLink("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						IpreventFunctions Iprfunc = new IpreventFunctions(user);
						if (Iprfunc.isRRMPreManopausal()) {
							setResponsePage(new PreMenopausalPage(trial));
						} else if (Iprfunc.isRRMPostManopausal()) {
							setResponsePage(new PostMenopausalPage(trial));
						}
					}
				};
				link.add(new Label("label", YourOptionType.RRM.getName()));
			}
			if (YourOptionType.SCREENING.equals(modelObject)) {
				ScreeningSummary screeningSummary = new ScreeningSummary(trial);
				link = createLink(new ScreeningPage(screeningSummary), YourOptionType.SCREENING.getName());
			}
			if (YourOptionType.LIFESTYLE.equals(modelObject)) {
				LifeStyleSummary lifeStyleSummary = new LifeStyleSummary(trial);
				link = createLink(new LifeStylePresentationPage(lifeStyleSummary), YourOptionType.LIFESTYLE.getName());
			}*/
			/*if (YourOptionType.EXERCISE.equals(modelObject)) {
				link = createLink(null, YourOptionType.EXERCISE.getName());
				link.setEnabled(false);
			}
			if (YourOptionType.SMOKING.equals(modelObject)) {
				link = createLink(null, YourOptionType.SMOKING.getName());
				link.setEnabled(false);
			}
			if (YourOptionType.ALCOHOL.equals(modelObject)) {
				link = createLink(null, YourOptionType.ALCOHOL.getName());
				link.setEnabled(false);
			}
			if (YourOptionType.WEIGHT.equals(modelObject)) {
				link = createLink(null, YourOptionType.WEIGHT.getName());
				link.setEnabled(false);
			}*/
			link=createLink(null, modelObject.toString());
			link.setEnabled(false);
			return link;
		}

		/**
		 * 
		 * @param link
		 * @param lstmodelObject
		 * @return
		 */
		private AjaxLink createSecondLevelItems(List lstmodelObject) {
			AjaxLink link = null;
			/*if (lstmodelObject.contains(YourOptionType.BPM_MASTECTOMY)) {
				link = createLink(new BPMPage(trial), YourOptionType.BPM_MASTECTOMY.getName());
			}
			if (lstmodelObject.contains(YourOptionType.BPM_RRSO)) {
				link = createLink(new RRSOPage(trial), YourOptionType.BPM_RRSO.getName());
			}*/
			/*if (lstmodelObject.contains(YourOptionType.EXERCISE)) {
				link = createLink(null, YourOptionType.EXERCISE.getName());
				link.setEnabled(false);

			}
			if (lstmodelObject.contains(YourOptionType.SMOKING)) {
				link = createLink(null, YourOptionType.SMOKING.getName());
				link.setEnabled(false);
			}
			if (lstmodelObject.contains(YourOptionType.ALCOHOL)) {
				link = createLink(null, YourOptionType.ALCOHOL.getName());
				link.setEnabled(false);
			}
			if (lstmodelObject.contains(YourOptionType.WEIGHT)) {
				link = createLink(null, YourOptionType.WEIGHT.getName());
				link.setEnabled(false);
			}*/
			link=createLink(null, lstmodelObject.toString());
			link.setEnabled(false);
			return link;
		}

		/**
		 * 
		 * @param webpage
		 * @param linkLabel
		 * @return
		 */
		private AjaxLink createLink(final WebPage webpage, String linkLabel) {
			AjaxLink link = null;
			link = new AjaxLink("link") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					setResponsePage(webpage);
				}
				
			};
			link.add(new Label("label", linkLabel).add(new AttributeAppender("class", new Model("yourOptionNormalLinkText"), " ")));
			return link;
		}
	}
}
