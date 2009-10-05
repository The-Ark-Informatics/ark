/*
 * SmartformDE.java
 *
 * Created on August 24, 2005, 1:56 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

/**
 *
 * @author renny
 */
import org.jasig.portal.ChannelRuntimeData;

public class SmartformDEClient extends SmartformDE
{
    String strName = null;
    String strResult = null;
    String strType = null;
    String strID = null;
    String strOrder = null;
    String strDENo = null;
    boolean blMandatory = false;
    String strother = null;
    /** Creates a new instance of SmartformDEClient */
    public SmartformDEClient() 
    {
        
    }
    public void setOther(String str_Other)
    {
    	this.strother = str_Other;
    }
    public String getOther()
    {
    	return this.strother;
    }
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
            if(this.strID != null && this.strID.length() > 0)
            {
                strbfrXML.append("<SmartformDE>");
                strbfrXML.append("<strID>" + this.strName + "</strID>");
                if(this.strName != null && this.strName.length() > 0)
                {
                    strbfrXML.append("<strName>" + this.strName + "</strName>");
                }
                if(this.strResult != null && this.strResult.length() > 0)
                {
                    strbfrXML.append("<strResult>" + this.strResult + "</strResult>");
                }
                if(this.strType != null && this.strType.length() > 0)
                {
                    strbfrXML.append("<strType>" + this.strType + "</strType>");
                }
                if(this.strOrder != null && this.strOrder.length() > 0)
                {
                    strbfrXML.append("<strOrder>" + this.strOrder + "</strOrder>");
                }
                if(this.strDENo != null && this.strDENo.length() > 0)
                {
                    strbfrXML.append("<strDENo>" + this.strDENo + "</strDENo>");
                }
                if(this.strother != null && this.strother.length() > 0)
                {
                	strbfrXML.append("<strOther>" + this.strother + "</strother>");
                }
                if(this.blMandatory)
                {
                    strbfrXML.append("<blMandatory>" + this.blMandatory + "</blMandatory>");
                }
                strbfrXML.append("</SmartformDE>");
            }
        return strbfrXML.toString();
    }
    public ChannelRuntimeData toRuntimeData(ChannelRuntimeData rd)
    {
        return rd;
    }
}
