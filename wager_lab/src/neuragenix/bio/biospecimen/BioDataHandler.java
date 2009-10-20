package neuragenix.bio.biospecimen;


import java.util.*;
import java.util.regex.Pattern;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.jasig.portal.ChannelRuntimeData;

public class BioDataHandler {
	
	public XMLDocument writeFieldDatatoXML() {
		XMLDocument d= new XMLDocument();
		XML bioform = new XML("bioform");
		d.addElement(bioform);
	for (int i = 0; i < 5; i++) {
		XML fieldset = new XML("fieldset").addXMLAttribute("name","fieldset_"+i);
		bioform.addElement(fieldset);
		for (int j=0; j < 10; j++) {
			XML field = new XML("field");
			fieldset.addElement(field);
			
		}
		
	}
		System.out.println(d.toString());
		
		bioform.addElement(new XML("fieldset").addAttribute("name", "crap"));
		System.out.println(d.toString());
		return d;
	}

	public void processForm(ChannelRuntimeData rd) {
		
		Hashtable fieldParameters = (Hashtable) rd.getParameters();
		HashSet allFieldNames = (HashSet) fieldParameters.keySet();
		HashSet bioFieldNames = getBioFields(allFieldNames);
		
	}
	
	
	
	private void writeRecord(int fieldkey, int biospecimenkey,String value) {
		
	}
	
	
	 public static HashSet getBioFields( Collection c) {
		    HashSet       subset = new HashSet();
		    Iterator  iterator = c.iterator();
		    String    currentField;

		    while( iterator.hasNext() ) {
		      currentField = (String) iterator.next();
		      
		      if( Pattern.matches("field_(\\d)+",currentField) )
		        c.add( currentField );
		    }

		    return subset;
		  }

	 
	
	
}
