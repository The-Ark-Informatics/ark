/*
 * DocDetails.java
 *
 * Created on March 2, 2004, 2:23 PM
 */

package neuragenix.utils;

import java.util.Vector;
/** This class is used to maintain details of the document saved
 * @author renny
 */
public class DocDetails {
    
    private String author;
    private String link;
    private boolean saved = false;
    private Vector vnoValues = null;
    /** Creates a new instance of DocDetails */
    public DocDetails() 
    {
        vnoValues = null;
    }
    /** This saves the author of the document which is the same as the login name
     * @param athr same as the login name
     */    
    public void setAuthor(String athr)
    {
        this.author = athr;
    }
    /** returns the author
     * @return returns the author of the document
     */    
    public String getAuthor()
    {
        return author;
    }
    /** sets the location of the document
     * @param lnk the name of the saved document file
     */    
    public void setLink(String lnk)
    {
        this.link = lnk;
    }
    /** returns the location of the document
     * @return name of the file
     */    
    public String getLink()
    {
        return link;
    }
    /** sets the flag to say that the document has been saved
     * @param flag true if the document is saved
     */    
    public void setFlag(boolean flag)
    {
        this.saved = flag;
    }
    /** returns the flag
     * @return returns true is the document is created otherwise false
     */    
    public boolean getFlag()
    {
        return saved;
    }
    
    public void populateVector(String key)
    {
        if(this.vnoValues == null)
        {
            this.vnoValues = new Vector();
        }
        if(!this.vnoValues.contains(key))
        {
            this.vnoValues.add(key);
        }
    }
    public Vector getVector()
    {
        return this.vnoValues;
    }
}
