package au.org.theark.phenotypic.web.component.summaryModule.form;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.BarChartResult;
import au.org.theark.core.web.component.chart.JFreeChartImage;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.summaryModule.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO>
{
	private PageableListView<PhenoCollection>				listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private DetailPanel											detailPanel;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;
	
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				iPhenotypicService;
	
	private JFreeChart chart;
	private DefaultPieDataset d;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<PhenoCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
		addFieldComponents();
	}

	public void initialiseFieldForm()
	{
		// Study in context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if(sessionStudyId != null)
		{
			Study study = iArkCommonService.getStudy(sessionStudyId);
			
			d = new DefaultPieDataset();
			d.setValue("Fields with Data", new Integer(iPhenotypicService.getCountOfFieldsWithDataInStudy(study)));
			d.setValue("Fields without Data", new Integer(iPhenotypicService.getCountOfFieldsInStudy(study) - iPhenotypicService.getCountOfFieldsWithDataInStudy(study)));
			
			chart = ChartFactory.createPieChart("Phenotypic Field Summary", d,
	                 true,		// Show legend  
	                 true,		// Show tooltips
	                 true);		// Show urls
	        chart.setBackgroundPaint(Color.white);
	        chart.setBorderVisible(false);
			add(new JFreeChartImage("phenoFieldSummaryImage", chart, 400, 400));
			
			d = new DefaultPieDataset();
			d.setValue("Collections with Data", new Integer(iPhenotypicService.getCountOfCollectionsWithDataInStudy(study)));
			d.setValue("Collections without Data", new Integer(iPhenotypicService.getCountOfCollectionsInStudy(study) - iPhenotypicService.getCountOfCollectionsWithDataInStudy(study)));
			
			chart = ChartFactory.createPieChart("Phenotypic Collection Summary", d,
	                 true,		// Show legend  
	                 true,		// Show tooltips
	                 true);		// Show urls
	        chart.setBackgroundPaint(Color.white);
	        chart.setBorderVisible(false);
			add(new JFreeChartImage("phenoPhenoCollectionSummaryImage", chart, 400, 400));
			
			String[] seriesNames = new String[] {"2001", "2002"};
			String[] categoryNames = new String[] {"First Quater",
			                                       "Second Quater"};
			Number[][] categoryData = new Integer[][] {{new Integer(20),
			                                            new Integer(35)},
			                                           {new Integer(40),
			                                            new Integer(60)}
			};
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			List<BarChartResult> resultList = iPhenotypicService.getFieldsWithDataResults(study);
			for (Iterator iterator = resultList.iterator(); iterator.hasNext();)
			{
				BarChartResult barChartResult = (BarChartResult) iterator.next();
				//dataset.setValue(3, "Collection", "ANOTHER");
				//dataset.setValue(6, "Collection", "NEW_COLLECTION");
				dataset.setValue(barChartResult.getValue(), barChartResult.getRowKey(), barChartResult.getColumnKey());
			}
			
			
			chart = ChartFactory.createBarChart(
			                     "Fields With Data", // Title
			                      "Collection Name",              // categoryAxisLabel
			                      "Fields",                 // valueAxisLabel
			                      dataset,         // Dataset
			                      PlotOrientation.VERTICAL ,            			// Orientation
			                      false ,                   // Show legend
			                      true,						  // Show tooltips
			                      false						  // Show urls
			                     );
			add(new JFreeChartImage("phenoCollectionBarChartSummaryImage", chart, 800, 400));
		}
		else
		{
			d = new DefaultPieDataset();
			d.setValue("Fields with Data", new Integer(0));
			d.setValue("Fields without Data", new Integer(0));
			
			chart = ChartFactory.createPieChart("Phenotypic Field Summary", d,
	                 true,		// Show legend  
	                 true,		// Show tooltips
	                 true);		// Show urls
	        chart.setBackgroundPaint(Color.white);
	        chart.setBorderVisible(false);
			add(new JFreeChartImage("phenoFieldSummaryImage", chart, 0, 0).setVisible(false));
			
			chart = ChartFactory.createPieChart("Phenotypic Collection Summary", d,
	                 true,		// Show legend  
	                 true,		// Show tooltips
	                 true);		// Show urls
	        chart.setBackgroundPaint(Color.white);
	        chart.setBorderVisible(false);
			add(new JFreeChartImage("phenoPhenoCollectionSummaryImage", chart, 0, 0).setVisible(false));
		}
	}

	private void addFieldComponents()
	{
		// Add the field components
		// For summary module, disable buttons
		this.viewButtonContainer.setVisible(false);
		this.editButtonContainer.setVisible(false);
	}
	
	protected boolean isSecure(String actionType)
	{
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW))
		{
			flag = false;
		}
		return flag;
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}
}