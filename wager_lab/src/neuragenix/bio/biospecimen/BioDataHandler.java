package neuragenix.bio.biospecimen;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.jasig.portal.ChannelRuntimeData;
import org.wager.lims.biodata.*;


public class BioDataHandler {
	private FieldDAO fd;
	private DataDAO dd;
	private GroupDAO gd;
	private static final Log log = LogFactory.getLog(BioDataHandler.class);
	
	public BioDataHandler() {
		fd = new FieldDAO();
		dd = new DataDAO();
		gd = new GroupDAO();
	}
	
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

	public void processForm(ChannelRuntimeData rd, int biokey) {

		HashMap fieldParameters = (HashMap) rd.getParameters();
		Set allFieldNames = fieldParameters.keySet();
		HashSet<Field> bioFieldNames = getBioFields(allFieldNames);
		Iterator<Field> it = bioFieldNames.iterator();
		Field f;
		
		while (it.hasNext()) {
			f = it.next();
			String data = rd.getParameter("field_"+f.getFieldkey());
			Data d = new Data();
			d.setBiospecimenkey(new BigDecimal(biokey));
			d.setField(f);
			d.setStringValue(data); 
			d.setDateCollected(new Date());
			dd.persist(d);
		}
	}

	private void writeRecord(int fieldkey, int biospecimenkey, String value) {

	}

	public  HashSet<Field> getBioFields(Collection c) {
		HashSet<String> subset = new HashSet<String>();
		HashSet<Field> fields = new HashSet<Field>();
		Iterator<String> iterator = c.iterator();
		String currentField;
	
		log.debug("getBioFields: Entering.....");
		while (iterator.hasNext()) {
			currentField =  iterator.next();
			Pattern fieldIdPattern = Pattern.compile("field_(\\d+)");
			
			if (Pattern.matches("field_(\\d)+", currentField)) {
				Matcher m = fieldIdPattern.matcher(currentField);
				String s = null;
				if (m.find())
					s = m.group(1);
				if (s != null ) {
					BigDecimal fieldkey = new BigDecimal(s);
					
					Field f = fd.findById(fieldkey);
					log.debug("Adding field: " + f.getFieldname());
					if (f != null) {
						fields.add(f);
					}
					
				}
			}
		}

		return fields;
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
	

	
	private XML fieldDataToXML(Field f, Data d) {
		
		XML fieldXML = new XML("field");
		System.out.println("Data for field: " + f.getFieldname());
		
		String strValue = "";
		if (d != null) {
		if (d.getStringValue() != null) {
			strValue = d.getStringValue();
			System.out.println("its value is " + strValue);
		}
		else System.out.println("...and it's null");
		}
		else System.out.println("...and it's object is null");
			
		fieldXML.addElement(new XML("key").addElement(f.getFieldkey().toString()));
		fieldXML.addElement(new XML("label").addElement(f.getFieldname()));
		
		if (f.getLovtype() == null) {
			fieldXML.addElement(new XML("value").addElement(strValue));
		fieldXML.addElement(new XML("type").addElement(f.getType().getTypename()));
		}
		else {
			fieldXML.addElement(new XML("type").addElement("dropdown"));
		
			
			XML fields;
			try {
				log.debug("Running build LOV XML with strValue="+strValue);
				fields = fd.buildLOVXML(f, strValue, 0);
				fieldXML.addElement(fields);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
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
	
	/*public String getBioData(int biospecimenKey, Group g) {
		DataHome d = new DataHome();
		XMLDocument doc = new XMLDocument();
		XML bioform = new XML("bioform");
		for (Data dat : dataList) {
			log.debug("Data element " + dat.getField().getFieldname());
			bioform.addElement(dataToXML(dat));
		}
		doc.addElement(bioform);
		log.debug("XML output is : " + doc.toString());
		return doc.toString();
	}*/
	
	public XML findFieldsinGroupWithData(int biokey, Group g) {
		
		List<Object[]> data = gd.findFieldsinGroupWithData(biokey, g);
		XML groupXML = new XML("group");
		groupXML.addXMLAttribute("name", g.getGroupName());
		for (Object[] o : data) {
			Field f = (Field) o[0];
			Data d = (Data) o[1];
			groupXML.addElement(fieldDataToXML(f,d));
			
		}
		return groupXML;
	}
	public String getBioFields(List<Group> groups) {
		XMLDocument doc = new XMLDocument();
		XML bioform = new XML("bioform");
		for (Group g : groups) {
		if (g.getFields().size() > 0)
			bioform.addElement(grouptoXML(g));
		
		}
		doc.addElement(bioform);
		log.debug("XML output is : " + doc.toString());
		return doc.toString();
	}
	
	
	public String getBioData(List<Group> groups, int biokey) {
		XMLDocument doc = new XMLDocument();
		XML bioform = new XML("bioform");
		for (Group g : groups) {
		if (g.getFields().size() > 0)
			bioform.addElement(findFieldsinGroupWithData( biokey, g));
		
		}
		doc.addElement(bioform);
		log.debug("XML output is : " + doc.toString());
		return doc.toString();
	}
	
	
	
	

}
