package neuragenix.common;



import java.util.Date;



public class LockRecord

{

	public static final int READ_ONLY = 1;

	public static final int READ_WRITE = 2;

	

	private Object internalLockObject = null;

	private String strDomain = null;

	private String strPrimaryKey = null;

	private int intLockType = -1;

	private String timestamp;
        
        private int intTimeOut = 0;

	

	public LockRecord()

	{

		timestamp = (new Date()).toString();

		return;

	}

	

	public LockRecord(String domain, String primaryKey, int lockType)

	{

		strDomain = domain;

		strPrimaryKey = primaryKey;

		intLockType = lockType;

		timestamp = (new Date()).toString();

	}
        
        public LockRecord(String domain, String primaryKey, int lockType, int aTimeOut )

	{

		strDomain = domain;

		strPrimaryKey = primaryKey;

		intLockType = lockType;

		timestamp = (new Date()).toString();
                
                intTimeOut = aTimeOut;

	}

	

	public void setDomain(String domain)

	{

		strDomain = domain;

	}

	

	public String getDomain()

	{

		return strDomain;

	}

	

	public void setPrimaryKey(String primaryKey)

	{

		strPrimaryKey = primaryKey;

	}

	

	public String getPrimaryKey()

	{

		return strPrimaryKey;

	}



	public Class getEntityType()

	{

		return strDomain.getClass();

	}



	public String getEntityKey()

	{

		return strDomain + "-" + strPrimaryKey;

	}



	public String getOwner()

	{

		return strDomain + "-" + this.hashCode() + "-" + timestamp;

	}



	public void setLockType(int lockType)

	{

		intLockType = lockType;

	}

	

	public int getLockType()

	{

		return intLockType;

	}
        
        
        public void setTimeOut(int aTimeOut)

	{

		intTimeOut = aTimeOut;

	}

	

	public int getTimeOut()

	{

		return intTimeOut;

	}

	

	public void setInternalLock(Object o)

	{

		internalLockObject = o;

	}



	public Object getInternalLock()

	{

		return internalLockObject;

	}



	

}

