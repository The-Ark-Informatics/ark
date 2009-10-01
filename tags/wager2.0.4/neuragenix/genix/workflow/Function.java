/**

 * Function.java

 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.

 * Created Date: 18/03/2004

 *

 * Last Modified: (Date\Author\Comments)

 *

 * 

 */



package neuragenix.genix.workflow;



/**

 * Function - Store info for a function 

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class Function implements java.io.Serializable

{

	// Function key
	private String strKey = null;

	// Name
	private String strName = null;

        // Class Name
        private String strClassName = null;
        
        // Function Name
        private String strFunctionName = null;
        
        // Parameter Names
        private String strParameterNames = null;
        
        // Parameter Types
        private String strParameterTypes = null;
        
        // Function Type
        private String strType = null;
        

	/**

	 * Default constructor

	 */

	public Function()

	{

	}



	/**

	 * Full constructor

	 */

	public Function(String strKey,

                        String strName,
                        
                        String strClassName,
                        
                        String strFunctionName,
                        
                        String strParameterNames,
                        
                        String strParameterTypes,
                        
                        String strType)

	{

		this.strKey = strKey;

		this.strName = strName;
                
                this.strClassName = strClassName;

                this.strFunctionName = strFunctionName;

                this.strParameterNames = strParameterNames;

                this.strParameterTypes = strParameterTypes;

                this.strType = strType;

	}



	/**

	 * get the function key 

	 * 

	 * @return the function key 

	 */

	public String getKey()

	{

		return this.strKey;

	}



	/**

	 * set the function key 

	 * 

	 * @param strKey	the task key 

	 */

	public void setKey(String strKey)

	{

		this.strKey = strKey;

	}



	/**

	 * get the name

	 * 

	 * @return the name

	 */

	public String getName()

	{

		return this.strName;

	}



	/**

	 * set the name 

	 * 

	 * @param strName	the name

	 */
	public void setName(String strName)

	{

		this.strName = strName;

	}

        /**
         * set the class name
         * 
         * @param strClassName the class name
         */
        public void setClassName(String strClassName)
        {       
            this.strClassName = strClassName;
        }
        
        /**
         * get the class name
         *
         * @return the class name
         */
        public String getClassName()
        {
            return this.strClassName;
        }

        /**
         * set the function name
         * 
         * @param strFunctionName the function name
         */        
        public void setFunctionName(String strFunctionName)
        {
            this.strFunctionName = strFunctionName;
        }
        
        /**
         * get the function name
         *
         * @return the function name
         */
        public String getFunctionName()
        {
            return this.strFunctionName;
        }

        /**
         * set the parameter names
         * 
         * @param strParameterNames the parameter names
         */        
        public void setParameterNames(String strParameterNames)
        {            
            this.strParameterNames = strParameterNames;
        }
        
        /** 
         * get the parameter names
         *
         * @return the parameter names
         */
        public String getParameterNames()
        {
            return this.strParameterNames;
        }
        
        /**
         * set the parameter types
         * 
         * @param strParameterTypes the parameter types
         */
        public void setParameterTypes(String strParameterTypes)
        {
            this.strParameterTypes = strParameterTypes;
        }
        
        /** 
         * set the parameter types
         *
         * @return the parameter types
         */
        public String getParameterTypes()
        {
            return this.strParameterTypes;
        }

        /**
         * set the function type
         * 
         * @param strType the function type
         */
        public void setType(String strType)
        {
            this.strType = strType;
        }
        
        /**
         * get the function type
         *
         * @return the function type
         */
        public String getType()
        {
            return this.strType;
        }


	/**
	 * get the function name
	 * 
	 * @return the  function name
	 */
	public String toString()
	{
            return this.strName;
	}
}

