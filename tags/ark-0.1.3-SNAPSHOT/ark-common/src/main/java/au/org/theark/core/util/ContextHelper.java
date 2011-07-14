package au.org.theark.core.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class ContextHelper
{
	Label studyLabel = null;
	Label subjectLabel = null;
	Label phenoLabel = null;
	Label genoLabel = null;
	
	@SuppressWarnings("unchecked")
	public void resetContextLabel(AjaxRequestTarget target, WebMarkupContainer arkContextMarkup)
	{
		studyLabel =  new Label("studyLabel", new Model(""));
		arkContextMarkup.addOrReplace(studyLabel);
		subjectLabel =  new Label("subjectLabel", new Model(""));
		arkContextMarkup.addOrReplace(subjectLabel);
		phenoLabel =  new Label("phenoLabel", new Model(""));
		arkContextMarkup.addOrReplace(phenoLabel);
		genoLabel =  new Label("genoLabel", new Model(""));
		arkContextMarkup.addOrReplace(genoLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setStudyContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup)
	{
		studyLabel =  new Label("studyLabel", new Model("Study: " + label));
		arkContextMarkup.addOrReplace(studyLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setSubjectContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup)
	{
		subjectLabel =  new Label("subjectLabel", new Model("Subject UID: " + label));
		arkContextMarkup.addOrReplace(subjectLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setPhenoContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup)
	{
		phenoLabel =  new Label("phenoLabel", new Model("Pheno Col.: " + label));
		arkContextMarkup.addOrReplace(phenoLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setGenoContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup)
	{
		genoLabel =  new Label("genoLabel", new Model("Geno Col.: " + label));
		arkContextMarkup.addOrReplace(genoLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setContextLabel(AjaxRequestTarget target, Label label, WebMarkupContainer arkContextMarkup)
	{
		if(label.getId() == "phenoLabel")
		{
			phenoLabel =  new Label("phenoLabel", new Model("Pheno Col.: " + label));
			arkContextMarkup.addOrReplace(phenoLabel);
		}
		
		target.addComponent(arkContextMarkup);
	}
}