package neuragenix.genix.genixadmin;



import java.util.Hashtable;

import neuragenix.dao.DBSchema;



public class ListOfValuesFields

{





	private static Hashtable hashLOVSelector = new Hashtable();

	private static Hashtable hashLOVEdit = new Hashtable();

	

	private static boolean blLoaded = false;

	

	private ListOfValuesFields()

	{

		//do nothing

		return;

	}

	

	

	public static Hashtable getLOVEditFields()

	{

		if(!blLoaded)

		{

			load();

		}

		return hashLOVEdit;

	}



	public static Hashtable getLOVSelectorFields()

	{

		try{

		return DBSchema.getLOVSelectorFields();

		}

		catch (Exception e)

		{

		return new Hashtable();

		}

	}

	

	

	private static void load()

	{

		loadLOVSelector();

		loadLOVEdit();

		blLoaded = true;

	}

	

	

	private static void loadLOVEdit()

	{

		hashLOVEdit.clear();

		hashLOVEdit.put("intLovKey","NA");

		hashLOVEdit.put("strType","Types of Biospecimen");

		hashLOVEdit.put("strValue","Value");

		hashLOVEdit.put("strDescription","Description");

		hashLOVEdit.put("intSortOrder", "Sort Order");

	}

	

	private static void loadLOVSelector()

	{

		return;

	}

	

}

