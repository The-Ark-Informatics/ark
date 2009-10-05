package neuragenix.common;



import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;

import org.jasig.portal.concurrency.*;

import java.util.Hashtable;



public class uPortalLockRepositoryImpl implements LockRepository

{

	

	private static final int LOCK_EXACT = 1;

	private static final int LOCK_READ = 2;

	private static final int LOCK_UPGRADE = 3;

	private static final int UNLOCK_EXACT = 4;



	private static uPortalLockRepositoryImpl instance = new uPortalLockRepositoryImpl();

	

	public static uPortalLockRepositoryImpl getInstance() throws UnsupportedLockException

	{

		if (null == instance.ieLS)

		{

			throw new UnsupportedLockException("Could not create LockService...");

		}

		return instance;

	}

	

	

	private int duration = 36000;

	

	private IEntityLockService ieLS = null;

	private Hashtable hashCurrentLocks = new Hashtable();

	
        public boolean isValid( LockRecord aLockRecord) throws UnsupportedLockException{
            
            try{
            
                if( aLockRecord != null ){
                    
                    return ( (IEntityLock) aLockRecord.getInternalLock() ).isValid();
                
                }else
                    throw new UnsupportedLockException( "Exception in uPortalLockRepositoryImpl::isValid(): LockRecord is null...");
            }catch( Exception e){
                throw new UnsupportedLockException( "Exception in uPortalLockRepositoryImpl::isValid(): Cannont lock...");
            }
            
        }
        // renew.
        public void renew( LockRecord aLockRecord) throws UnsupportedLockException{
            
            try{
            
                if( aLockRecord != null ){
                    if( ((IEntityLock) aLockRecord.getInternalLock()).isValid()){
                        ((IEntityLock) aLockRecord.getInternalLock()).renew( aLockRecord.getTimeOut() );
                    }
			//else{
                        //LockRepositoryFactory.getLockRepository().lockForRead(aLockRecord);
                    //}
                
                }else
                    throw new UnsupportedLockException( "Exception in uPortalLockRepositoryImpl::renew(): LockRecord is null...");
            }catch( Exception e){
                throw new UnsupportedLockException( "Exception in uPortalLockRepositoryImpl::renew(): " + e.toString());
            }
            
        }
	

	private uPortalLockRepositoryImpl()

	{

		String eMsg = null;

		String factoryName = PropertiesManager.getProperty("org.jasig.portal.concurrency.IEntityLockServiceFactory");



		if ( factoryName == null )

		{

			eMsg = "EntityLockService.initialize(): No entry for org.jasig.portal.concurrency.IEntityLockServiceFactory in portal.properties.";

			LogService.instance().log(LogService.ERROR, eMsg);

		}



		try

		{

			IEntityLockServiceFactory lockServiceFactory = (IEntityLockServiceFactory)Class.forName(factoryName).newInstance();

			ieLS = lockServiceFactory.newLockService();

		}

		catch (Exception e)

		{

			eMsg = "EntityLockService.initialize(): Problem creating entity lock service... " + e.getMessage();

			LogService.instance().log(LogService.ERROR, eMsg, e);

		}

		

		if(null == ieLS)

		{

			 LogService.instance().log(LogService.ERROR, "LockService Creation error", new UnsupportedLockException("Could not create LockService..."));

		}



		return;

	}

	
       

	/** 

	 * @param LockType Use symbolic constants from LockRecord.

	 *

	 */

	public boolean lock( String strDomain, String strPrimaryKey , int LockType) throws UnsupportedLockException

	{

		return manipulateLocks(LOCK_EXACT, new LockRecord(strDomain, strPrimaryKey, LockType), LockType);

	}

	

	public boolean lock( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(LOCK_EXACT, aLockRecord, aLockRecord.getLockType());

	}

	

	public boolean lockForRead( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(LOCK_READ, aLockRecord, LockRecord.READ_ONLY);

	}



	public boolean lockConvert( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(LOCK_UPGRADE, aLockRecord, aLockRecord.getLockType());

	}



	public void unlock( String strDomain, String strPrimaryKey, int LockType) throws UnsupportedLockException

	{

		manipulateLocks(UNLOCK_EXACT, new LockRecord(strDomain, strPrimaryKey, LockType), LockType);

	}

	

	public void unlock( LockRecord aLockRecord) throws UnsupportedLockException

	{

		manipulateLocks(UNLOCK_EXACT, aLockRecord, aLockRecord.getLockType());

	}

	

	public boolean unlockConvert( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(LOCK_UPGRADE, aLockRecord, LockRecord.READ_ONLY);

	}



	private synchronized boolean manipulateLocks(int operation, LockRecord lrTemp , int intLockType) throws UnsupportedLockException

	{

		int currentlockcount;

		try

		{

			if (LOCK_UPGRADE == operation)

			{

				((IEntityLock)lrTemp.getInternalLock()).convert(getUPortalLockType(intLockType));

				return true;

			}

			else if (LOCK_READ == operation)

			{

				lrTemp.setInternalLock(ieLS.newLock(lrTemp.getEntityType(), lrTemp.getEntityKey(), getUPortalLockType(intLockType), lrTemp.getOwner(), lrTemp.getTimeOut()));

				return true;

			}

			else if (UNLOCK_EXACT == operation)

			{

				((IEntityLock)lrTemp.getInternalLock()).release();

				return true;

			}

			else if(LOCK_EXACT == operation)

			{

				lrTemp.setInternalLock(ieLS.newLock(lrTemp.getEntityType(), lrTemp.getEntityKey(), getUPortalLockType(intLockType), lrTemp.getOwner(), lrTemp.getTimeOut()));

				return true;

			}

			else

			{

				throw new UnsupportedLockException("Unknown operation on Lock: Type " + operation);

			}

		}

		catch(Exception e)

		{

			LogService.instance().log(LogService.ERROR, "Locking Error: " , e);

		}

		return false;

	}

	

	

	private int getUPortalLockType(int intLockType) throws UnsupportedLockException

	{

		switch(intLockType)

		{

			case LockRecord.READ_ONLY: return IEntityLockService.READ_LOCK;

			case LockRecord.READ_WRITE: return IEntityLockService.WRITE_LOCK;

		}

		throw new UnsupportedLockException("Unknown Lock: Type " + intLockType);

	}

	

}

