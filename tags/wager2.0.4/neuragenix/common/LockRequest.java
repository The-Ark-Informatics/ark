package neuragenix.common;



import neuragenix.security.AuthToken;

import org.jasig.portal.services.LogService;

import org.jasig.portal.concurrency.IEntityLock;
import org.jasig.portal.PropertiesManager;

import java.util.*;



public class LockRequest

{

	private static Hashtable hashLockRequestList = new Hashtable();

	

	//LinkedList llUnlockedWriteLockRecords = new LinkedList();

	//LinkedList llUnlockedReadLockRecords = new LinkedList();

	LinkedList llLockRecords = new LinkedList();

	LinkedList llUnlockRecords = new LinkedList();

	LinkedList llPartialRecords = new LinkedList();

	private int intTimeOut = PropertiesManager.getPropertyAsInt( "neuragenix.genix.security.DefaultLockTimeOut" );
        
	String sessionUID = "null";

	

	public LockRequest()

	{

		super();
                
                
                
                // debugging tool for finding locks without auth or session ids

		//throw new RuntimeException("Why are you still using this deprecated API?");

	}

	

	public LockRequest(String strUniqueID)

	{

		super();

		sessionUID = strUniqueID;
                
                

		registerSession();

	}

	

	public LockRequest(AuthToken authToken)

	{

		super();

		sessionUID = authToken.getSessionUniqueID();

		registerSession();

	}

	

	public void addLock(LockRecord aLockRecord) throws UnsupportedLockException

	{

		llLockRecords.addFirst(aLockRecord);

	}

	

	public void addLock(String domain, String primaryKey, int lockType) throws UnsupportedLockException

	{

		addLock(new LockRecord(domain, primaryKey, lockType, intTimeOut));

	}



	public boolean lockDelayWrite() throws UnsupportedLockException

	{

		boolean blLockGranted;

		LockRecord lrTemp = null;

		while( ! llLockRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llLockRecords.removeLast();

			blLockGranted = LockRepositoryFactory.getLockRepository().lockForRead(lrTemp);

			if(blLockGranted)

			{

				llPartialRecords.addLast(lrTemp);

			}

			else

			{

				llLockRecords.addLast(lrTemp);

				unlock();

				return false;

			}

		}

		return true;

	}



	public boolean lockWrites() throws UnsupportedLockException

	{

		boolean blLockGranted;

		LockRecord lrTemp = null;

		while( ! llPartialRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llPartialRecords.removeLast();

			blLockGranted =  (lrTemp.getLockType() != LockRecord.READ_ONLY) ? LockRepositoryFactory.getLockRepository().lockConvert(lrTemp) : true;

			if(blLockGranted)

			{

				llUnlockRecords.addLast(lrTemp);

			}

			else

			{

				llPartialRecords.addLast(lrTemp);

				unlockWrites();

				return false;

			}

		}

		return true;

	}



	public boolean lock() throws UnsupportedLockException

	{

		boolean blLockGranted;

		LockRecord lrTemp = null;

		while( ! llLockRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llLockRecords.removeLast();

			blLockGranted = LockRepositoryFactory.getLockRepository().lock(lrTemp);

			if(blLockGranted)

			{

				llUnlockRecords.addLast(lrTemp);

			}

			else

			{

				llLockRecords.addLast(lrTemp);

				unlock();

				return false;

			}

		}

		return true;

	}

	

	public void unlock() throws UnsupportedLockException

	{	

		LockRecord lrTemp = null;

		while( ! llPartialRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llPartialRecords.removeLast();

			LockRepositoryFactory.getLockRepository().unlock(lrTemp);

			llLockRecords.addLast(lrTemp);

		}

		while( ! llUnlockRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llUnlockRecords.removeLast();

			LockRepositoryFactory.getLockRepository().unlock(lrTemp);

			llLockRecords.addLast(lrTemp);

		}

	}



	public void unlockWrites() throws UnsupportedLockException

	{

		LockRecord lrTemp = null;

		while( ! llUnlockRecords.isEmpty() )

		{

			lrTemp = (LockRecord)llUnlockRecords.removeLast();

			if(lrTemp.getLockType() != LockRecord.READ_ONLY)

			{

				LockRepositoryFactory.getLockRepository().unlockConvert(lrTemp);

			}

			llPartialRecords.addLast(lrTemp);

		}

	}

	

	public boolean isValid()

	{

		boolean validity = llLockRecords.isEmpty();
                if( !validity ) return false;
                
                Iterator iter = llPartialRecords.iterator();
                
                LockRecord lrTemp;
                
                try{
                
                    while( iter.hasNext() ){
                        lrTemp = (LockRecord) iter.next();
                        //if( lrTemp.getLockType() == LockRecord.READ_WRITE ){
                            
                            
                            if( !LockRepositoryFactory.getLockRepository().isValid( lrTemp ))
                                return false;
                            
                            LockRepositoryFactory.getLockRepository().renew( lrTemp );
                            

                        //}

                    }
                }catch( Exception e ){
                    
                    LogService.log(LogService.ERROR, "LockRequest::isValid() Unknown error while unlocking : ", e);
                }
		return validity;

		

	}
/*
        public void renew(){
                boolean validity = llLockRecords.isEmpty();
                if( !validity ) return;
                
                Iterator iter = llPartialRecords.iterator();
                
                LockRecord lrTemp;
                
                try{
                
                    while( iter.hasNext() ){
                        lrTemp = (LockRecord) iter.next();
                        LockRepositoryFactory.getLockRepository().renew( lrTemp );
                    }
                }catch( Exception e ){
                    
                    LogService.log(LogService.ERROR, "LockRequest::renew() Unknown error while unlocking : ", e);
                }
            
        }*/
	

	public void setTimeout(int timeOutRequested)

	{

		intTimeOut = timeOutRequested;

	}

	

	public int getTimeout()

	{

		return intTimeOut;

	}

	

	public void registerSession()

	{

                
		synchronized(hashLockRequestList)

		{

			if(hashLockRequestList.containsKey(sessionUID))

			{

				if(!((LinkedList)hashLockRequestList.get(sessionUID)).contains(this))

				{

					((LinkedList)hashLockRequestList.get(sessionUID)).add(this);

				}

			}

			else

			{

				LinkedList llTemp = new LinkedList();

				llTemp.add(this);

				hashLockRequestList.put(sessionUID, llTemp);

			}

		}

	}

	

	public void unregisterSession()

	{

		synchronized(hashLockRequestList)

		{

			if(hashLockRequestList.containsKey(sessionUID))

			{

				LinkedList llTemp = (LinkedList) hashLockRequestList.get(sessionUID);

				if(llTemp.contains(this))

				{

					llTemp.remove(this);

					if(llTemp.size() == 0)

					{

						hashLockRequestList.remove(sessionUID);

					}

				}

			}

			else

			{

				

				return;

			}

		}

	}

	

	public static void emptyAllLocks(String sessionID)

	{

		synchronized(hashLockRequestList)

		{

			if(hashLockRequestList.containsKey(sessionID))

			{

				LinkedList llTemp = (LinkedList) hashLockRequestList.get(sessionID);

				while(llTemp.size() > 0)

				{

					LockRequest locReqTemp = (LockRequest)llTemp.removeLast();

					try

					{

						locReqTemp.unlock();

					}

					catch(Exception e)

					{

						LogService.log(LogService.ERROR, "LockRequest::emptyAllLocks() Unknown error while unlocking : ", e);

					}

				}

				hashLockRequestList.remove(sessionID);

			}

			else

			{

				

				return;

			}

		}

	}



}

