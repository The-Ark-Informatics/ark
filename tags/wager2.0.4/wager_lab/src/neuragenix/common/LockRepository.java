package neuragenix.common;



public interface LockRepository

{



	public boolean lock( String strDomain, String strPrimaryKey, int LockType) throws UnsupportedLockException;

	public boolean lock( LockRecord aLockRecord) throws UnsupportedLockException;
        
	public boolean isValid( LockRecord aLockRecord) throws UnsupportedLockException;
        
        public void renew( LockRecord aLockRecord) throws UnsupportedLockException;

	public void unlock ( String strDomain, String strPrimaryKey, int LockType) throws UnsupportedLockException;

	public void unlock( LockRecord aLockRecord) throws UnsupportedLockException;



	public boolean lockForRead( LockRecord aLockRecord) throws UnsupportedLockException;

	public boolean lockConvert( LockRecord aLockRecord) throws UnsupportedLockException;

	public boolean unlockConvert( LockRecord aLockRecord) throws UnsupportedLockException;



}

