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
	public void setStudyContextLabel(AjaxRequestTarget target, String studyName, WebMarkupContainer arkContextMarkup)
	{
		studyLabel =  new Label("studyLabel", new Model("Study: " + studyName));
		arkContextMarkup.addOrReplace(studyLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setSubjectContextLabel(AjaxRequestTarget target, String subjectUid, WebMarkupContainer arkContextMarkup)
	{
		subjectLabel =  new Label("subjectLabel", new Model("SubjectUID: " + subjectUid));
		arkContextMarkup.addOrReplace(subjectLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setPhenoContextLabel(AjaxRequestTarget target, String subjectUid, WebMarkupContainer arkContextMarkup)
	{
		phenoLabel =  new Label("phenoLabel", new Model("Phenotypic Collection: " + subjectUid));
		arkContextMarkup.addOrReplace(phenoLabel);
		target.addComponent(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setGenoContextLabel(AjaxRequestTarget target, String subjectUid, WebMarkupContainer arkContextMarkup)
	{
		genoLabel =  new Label("genoLabel", new Model("Genotypic Collection: " + subjectUid));
		arkContextMarkup.addOrReplace(phenoLabel);
		target.addComponent(arkContextMarkup);
	}
}