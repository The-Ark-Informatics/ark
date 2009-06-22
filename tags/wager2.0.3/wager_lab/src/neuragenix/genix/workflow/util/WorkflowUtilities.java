/**
 * WorkflowUtilities.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 21/05/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 
 */

package neuragenix.genix.workflow.util;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.services.LogService;

/**
 * This is a common class to store workflow utilities
 * based static methods
 * @author Agustian Agustian 
 */

public class WorkflowUtilities
{
    /**
	 * Default constructor
	 */
    public WorkflowUtilities()
	{
    }


	/**
	 * prepare runtime data, concate data from static Data (e.g. the user name) and portal event into runtimedata
	 *
	 * @param runtimedata	the runtime data
	 * @param staticData	the static data 
	 * @param portalEvent	the portal event
	 *
	 * @return the concatenated runtime data
	 */
	public static ChannelRuntimeData prepareRuntimeData(ChannelRuntimeData runtimeData, 
														ChannelStaticData staticData, 
														PortalEvent portalEvent)
	{
		try
		{
			String strUserName = staticData.getPerson().getAttribute("username").toString();

			runtimeData.setParameter("strSystemUserName", strUserName);
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowUtilities::prepareRuntimeData - " + e.toString(), e);
        }

		return runtimeData;
	}
}
