package neuragenix.bio.biospecimen;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.jasig.portal.ChannelRuntimeData;
import org.wager.lims.biodata.*;


public class BioDataHandler {
	private static final Log log = LogFactory.getLog(BioDataHandler.class);
	public XMLDocument writeFieldDatatoXML() {
		XMLDocument d = new XMLDocument();
		XML bioform = new XML("bioform");
		d.addElement(bioform);
		for (int i = 0; i < 5; i++) {
			XML fieldset = new XML("fieldset").addXMLAttribute("name",
					"fieldset_" + i);
			bioform.addElement(fieldset);
			for (int j = 0; j < 10; j++) {
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

	private void writeRecord(int fieldkey, int biospecimenkey, String value) {

	}

	public static HashSet getBioFields(Collection c) {
		HashSet subset = new HashSet();
		Iterator iterator = c.iterator();
		String currentField;

		while (iterator.hasNext()) {
			currentField = (String) iterator.next();

			if (Pattern.matches("field_(\\d)+", currentField))
				c.add(currentField);
		}

		return subset;
	}

	private XML fieldToXML(Field f) {
		XML fieldXML = new XML("field");
		log.debug("Loading field: " + f.getFieldname());
		String strValue = "";
		fieldXML.addElement(new XML("key").addElement(f.getFieldkey().toString()));
		fieldXML.addElement(new XML("label").addElement(f.getFieldname()));
		fieldXML.addElement(new XML("type").addElement(f.getType().getTypename()));
		return fieldXML;
	}
	
	private XML dataToXML(Data d) {
		Field f = d.getField();
		XML fieldXML = new XML("field");
		String strValue = "";
		if (d.getStringValue() != null) {
			strValue = d.getStringValue();
		}
		fieldXML.addElement(new XML("key").addElement(f.getFieldkey().toString()));
		fieldXML.addElement(new XML("label").addElement(f.getFieldname()));
		fieldXML.addElement(new XML("type").addElement(f.getType().getTypename()));
		fieldXML.addElement(new XML("value").addElement(strValue));
		return fieldXML;

	}
	
	private XML grouptoXML(Group g) {
	XML groupXML = new XML("group");
	groupXML.addXMLAttribute("name", g.getGroupName());
	List<Field> fields = g.getFields();
	log.debug("Field list size is : "+ fields.size());
	for (Field f : fields) {
		groupXML.addElement(fieldToXML(f));
	}
	
	return groupXML;
	}
	
	public String getBioData(int biospecimenKey) {
		DataHome d = new DataHome();
		List<Data> dataList = d.findAllData(biospecimenKey);
		XMLDocument doc = new XMLDocument();
		XML bioform = new XML("bioform");
		for (Data dat : dataList) {
			log.debug("Data element " + dat.getField().getFieldname());
			bioform.addElement(dataToXML(dat));
		}
		doc.addElement(bioform);
		log.debug("XML output is : " + doc.toString());
		return doc.toString();
	}
	
	public String getBioData(List<Group> groups) {
		XMLDocument doc = new XMLDocument();
		XML bioform = new XML("bioform");
		for (Group g: groups) {
			bioform.addElement(grouptoXML(g));
		}
		doc.addElement(bioform);
		log.debug("XML output is : " + doc.toString());
		return doc.toString();
	}
	
	

}
