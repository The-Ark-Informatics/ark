/**
 * 
 */
package au.org.theark.admin.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author cellis
 *
 */
public class AdminVO implements Serializable 
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3939245546324873647L;
	
	private ArkRole arkRole;
	private ArkModule arkModule;
	private ArkFunction arkFunction;
	private ArkRolePolicyTemplate arkRolePolicyTemplate;
	private Study study;
	private List<Study> studyList;
	private List<ArkModule> arkModuleList;

	public AdminVO() 
	{
		this.arkRole = new ArkRole();
		this.arkModule = new ArkModule();
		this.arkFunction = new ArkFunction();
		this.arkRolePolicyTemplate = new ArkRolePolicyTemplate();
		this.study = new Study();
		this.studyList = new ArrayList<Study>(0);
		this.arkModuleList = new ArrayList<ArkModule>(0);
	}
	
	/**
	 * @return the arkRole
	 */
	public ArkRole getArkRole()
	{
		return arkRole;
	}

	/**
	 * @param arkRole the arkRole to set
	 */
	public void setArkRole(ArkRole arkRole)
	{
		this.arkRole = arkRole;
	}

	/**
	 * @return the arkModule
	 */
	public ArkModule getArkModule()
	{
		return arkModule;
	}

	/**
	 * @param arkModule the arkModule to set
	 */
	public void setArkModule(ArkModule arkModule)
	{
		this.arkModule = arkModule;
	}

	/**
	 * @return the arkFunction
	 */
	public ArkFunction getArkFunction()
	{
		return arkFunction;
	}

	/**
	 * @param arkFunction the arkFunction to set
	 */
	public void setArkFunction(ArkFunction arkFunction)
	{
		this.arkFunction = arkFunction;
	}

	/**
	 * @param arkRolePolicyTemplate the arkRolePolicyTemplate to set
	 */
	public void setArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		this.arkRolePolicyTemplate = arkRolePolicyTemplate;
	}

	/**
	 * @return the arkRolePolicyTemplate
	 */
	public ArkRolePolicyTemplate getArkRolePolicyTemplate()
	{
		return arkRolePolicyTemplate;
	}

	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study)
	{
		this.study = study;
	}

	/**
	 * @return the study
	 */
	public Study getStudy()
	{
		return study;
	}

	/**
	 * @param studyList the studyList to set
	 */
	public void setStudyList(List<Study> studyList)
	{
		this.studyList = studyList;
	}

	/**
	 * @return the studyList
	 */
	public List<Study> getStudyList()
	{
		return studyList;
	}

	/**
	 * @param arkModuleList the arkModuleList to set
	 */
	public void setArkModuleList(List<ArkModule> arkModuleList)
	{
		this.arkModuleList = arkModuleList;
	}

	/**
	 * @return the arkModuleList
	 */
	public List<ArkModule> getArkModuleList()
	{
		return arkModuleList;
	}
}
