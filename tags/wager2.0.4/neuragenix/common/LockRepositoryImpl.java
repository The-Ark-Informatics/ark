package neuragenix.common;



import java.util.Hashtable;
import org.jasig.portal.concurrency.*;



public class LockRepositoryImpl implements LockRepository

{

	

	private static LockRepositoryImpl instance = new LockRepositoryImpl();

	

	public static LockRepositoryImpl getInstance()

	{

		return instance;

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
                    throw new UnsupportedLockException( "Exception in LockRepositoryImpl::renew(): LockRecord is null...");
            }catch( Exception e){
                throw new UnsupportedLockException( "Exception in LockRepositoryImpl::renew(): " + e.toString());
            }
            
        }

	

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

	

	

	private Hashtable hashCurrentLocks = new Hashtable();

	

	

	private LockRepositoryImpl()

	{

		return;

	}

	

	/** 

	 * @param LockType Use symbolic constants from LockRecord.

	 *

	 */

	public boolean lock( String strDomain, String strPrimaryKey , int LockType) throws UnsupportedLockException

	{

		return manipulateLocks(true, strDomain, strPrimaryKey, LockType);

	}

	

	public boolean lock( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(true, aLockRecord.getDomain(), aLockRecord.getPrimaryKey(), aLockRecord.getLockType());

	}

	

	public boolean lockForRead( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(true, aLockRecord.getDomain(), aLockRecord.getPrimaryKey(), aLockRecord.getLockType());

	}

	

	public boolean lockConvert( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(true, aLockRecord.getDomain(), aLockRecord.getPrimaryKey(), aLockRecord.getLockType());

	}

	

	public void unlock ( String strDomain, String strPrimaryKey, int LockType) throws UnsupportedLockException

	{

		manipulateLocks(false, strDomain, strPrimaryKey, LockType);

	}

	

	public void unlock( LockRecord aLockRecord) throws UnsupportedLockException

	{

		manipulateLocks(false, aLockRecord.getDomain(), aLockRecord.getPrimaryKey(), aLockRecord.getLockType());

	}

	

	public boolean unlockConvert( LockRecord aLockRecord) throws UnsupportedLockException

	{

		return manipulateLocks(false, aLockRecord.getDomain(), aLockRecord.getPrimaryKey(), aLockRecord.getLockType());

	}

	

	private synchronized boolean manipulateLocks(boolean isLockRequest, String strDomain, String strPrimaryKey , int LockType) throws UnsupportedLockException

	{

		int currentlockcount;

		if(isLockRequest)

		{

			if(LockRecord.READ_ONLY == LockType)

			{

				if(hashCurrentLocks.containsKey(strDomain))

				{

					if((  (Hashtable)hashCurrentLocks.get(strDomain)  ).containsKey(strPrimaryKey))

					{

						currentlockcount = (  (Integer)(  (Hashtable)hashCurrentLocks.get(strDomain)  ).get(strPrimaryKey)  ).intValue();

						if (currentlockcount < 0) 

						{

							return false;

						}

						(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(currentlockcount + 1));

						return true;

					}

					else

					{

						(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(1));

						return true;

					}

				}

				else

				{

					hashCurrentLocks.put(strDomain, new Hashtable());

					(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(1));

					return true;

				}

				

			}

			else if(LockRecord.READ_WRITE == LockType)

			{

				if(hashCurrentLocks.containsKey(strDomain))

				{

					if((  (Hashtable)hashCurrentLocks.get(strDomain)  ).containsKey(strPrimaryKey))

					{

						currentlockcount = (  (Integer)(  (Hashtable)hashCurrentLocks.get(strDomain)  ).get(strPrimaryKey)  ).intValue();

						if (currentlockcount != 0) 

						{

							return false;

						}

						(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(-1));

						return true;

					}

					else

					{

						(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(-1));

						return true;

					}

				}

				else

				{

					hashCurrentLocks.put(strDomain, new Hashtable());

					(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(-1));

					return true;

				}

			}

			else

			{

				throw new UnsupportedLockException("Lock Type " + LockType + " is unsupported");

			}

		}

		else

		{

			if(LockRecord.READ_ONLY == LockType)

			{

				if(hashCurrentLocks.containsKey(strDomain))

				{

					if((  (Hashtable)hashCurrentLocks.get(strDomain)  ).containsKey(strPrimaryKey))

					{

						currentlockcount = (  (Integer)(  (Hashtable)hashCurrentLocks.get(strDomain)  ).get(strPrimaryKey)  ).intValue();

						if (currentlockcount > 0) 

						{

							(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(currentlockcount - 1));

						}

					}

				}

			}

			else if(LockRecord.READ_WRITE == LockType)

			{

				if(hashCurrentLocks.containsKey(strDomain))

				{

					if((  (Hashtable)hashCurrentLocks.get(strDomain)  ).containsKey(strPrimaryKey))

					{

						currentlockcount = (  (Integer)(  (Hashtable)hashCurrentLocks.get(strDomain)  ).get(strPrimaryKey)  ).intValue();

						if (currentlockcount == -1) 

						{

							(  (Hashtable)hashCurrentLocks.get(strDomain)  ).put(strPrimaryKey, new Integer(0));

						}

					}

				}

			}

			else

			{

				throw new UnsupportedLockException("Lock Type " + LockType + " is unsupported");

			}

			return true;

		}

	}

	

}

