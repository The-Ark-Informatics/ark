package neuragenix.common;



import neuragenix.utils.Property;

import org.jasig.portal.services.LogService;



public class LockRepositoryFactory

{

	private static LockRepository lrLR;

	

	public static LockRepository getLockRepository()

	{

		if(null==lrLR)

		{

			LogService.instance().log(LogService.WARN, "Creating a LockRepository...");

			makeInstance();

		}

		return lrLR;

	}

	

	public static void makeInstance()

	{

		try

		{

			if(null != Property.getProperty("neuragenix.common.LockRepositoryImplementation"))

			{

				Object objLockRep = Class.forName(Property.getProperty("neuragenix.common.LockRepositoryImplementation")).getMethod("getInstance", null).invoke(null,null);

			//	if (objLockRep instanceof LockRepository)

			//	{

					lrLR = (LockRepository) objLockRep;

					return;

			//	}

			}

		}

		catch(Exception e)

		{

			LogService.instance().log(LogService.WARN, "Unable to get neuragenix.common.LockRepositoryImplementation property", e);

		}

		lrLR = (LockRepository) LockRepositoryImpl.getInstance();	

		return;

	}

}

