/**
 * DBField.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 22/08/2003
 */ 

package neuragenix.dao;

/**
 * Class to model a database field
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

public class DBField 
{
    
    /** Field's domain name
     */
    private String strDomain;
    
    /** Field's name for INSERT & UPDATE
     */
    private String strNameForUpdate;
    
    /** Field's internal name
     */
    private String strInternalName;
    
    /** Field's short internal name
     */
    private String strShortInternalName;
    
    /** Field's external name
     */
    private String strExternalName;
    
    /** Field label used in forms
     */
    private String strLabelInForm;
    
    /** Field label used when displaying in tabular fashion
     */
    private String strLabelInColumn;
    
    /** Data type
     */
    private int intDataType;
    
    /** Field length
     */
    private int intLength;
    
    /** Indicate if the field is unique
     */
    private boolean blUnique;
    
    /** Indicate if the field is required
     */
    private boolean blRequired;
    
    /** Indicate LOV type of the field
     */
    private String strLOVType = null;
    
     /** whether the date input is drop down box or text
     */
    private String strDisplayType="dropdown";
    
    /** Indicate if the field is a dropdown field in search form
     */
    private boolean blDropdown = false;
    
    /** Keep the default date
     */
    private String strDefaultDate = null;
    
    
    /* Regular expression for the validation pattern */
    private String strValidationPattern = null;
    
    /** Creates a new instance of DBField 
     */
    public DBField() {
        this(null, null, null, null, null, null, -1, 0, false, false);
    }
    
    public DBField(String strADomain,
                   String strANameForUpdate,
                   String strAInternalName,
                   String strAExternalName,
                   String strALabelInForm,
                   String strALabelInColumn,
                   int intADataType,
                   int intALength,
                   boolean blAUnique,
                   boolean blARequired) 
    {
        strDomain = strADomain;
        strNameForUpdate = strANameForUpdate;
        strInternalName = strAInternalName;
        strExternalName = strAExternalName;
        strLabelInForm = strALabelInForm;
        strLabelInColumn = strALabelInColumn;
        intDataType = intADataType;
        intLength = intALength;
        blUnique = blAUnique;
        blRequired = blARequired;
    }
    
    public DBField(String strADomain,
                   String strANameForUpdate,
                   String strAInternalName,
                   String strAShortInternalName,
                   String strAExternalName,
                   String strALabelInForm,
                   String strALabelInColumn,
                   int intADataType,
                   int intALength,
                   boolean blAUnique,
                   boolean blARequired) 
    {
        strDomain = strADomain;
        strNameForUpdate = strANameForUpdate;
        strInternalName = strAInternalName;
        strShortInternalName = strAShortInternalName;
        strExternalName = strAExternalName;
        strLabelInForm = strALabelInForm;
        strLabelInColumn = strALabelInColumn;
        intDataType = intADataType;
        intLength = intALength;
        blUnique = blAUnique;
        blRequired = blARequired;
    }
    
    public String getDomain() {
        return strDomain;
    }
    
    public String getNameForUpdate() {
        return strNameForUpdate;
    }
    
    public String getInternalName() {
        return strInternalName;
    }
    
    public String getShortInternalName() {
        return strShortInternalName;
    }
    
    public String getExternalName() {
        return strExternalName;
    }
    
    public String getLabelInForm() {
        return strLabelInForm;
    }
    
    public String getLabelInColumn() {
        return strLabelInColumn;
    }
    
    public int getDataType() {
        return intDataType;
    }
    
    public int getLength() {
        return intLength;
    }
    
    public boolean isUnique() {
        return blUnique;
    }
    
    public boolean isRequired() {
        return blRequired;
    }
    
    public String getLOVType() {
        return strLOVType;
    }
    
    public boolean isDropdown() {
        return blDropdown;
    }
    
    public String getDefaultDate() {
        return strDefaultDate;
    }
        
    public String getValidationPattern() {
        return strValidationPattern;
    }    
    
    public void setDomain(String strADomain) {
        strDomain = strADomain;
    }
    
    public void setNameForUpdate(String strANameForUpdate) {
        strNameForUpdate = strANameForUpdate;
    }
    
    public void setInternalName(String strAInternalName) {
        strInternalName = strAInternalName;
    }
    
    public void setShortInternalName(String strAShortInternalName) {
        strShortInternalName = strAShortInternalName;
    }
    
    public void setExternalName(String strAExternalName) {
        strExternalName = strAExternalName;
    }
    
    public void setLabelInForm(String strALabelInForm) {
        strLabelInForm = strALabelInForm;
    }
    
    public void setLabelInColumn(String strALabelInColumn) {
        strLabelInColumn = strALabelInColumn;
    }
    
    public void setDataType(int intADataType) {
        intDataType = intADataType;
    }
    
    public void setLength(int intALength) {
        intLength = intALength;
    }
    
    public void setUnique(boolean blAUnique) {
        blUnique = blAUnique;
    }
    
    public void setRequired(boolean blARequired) {
        blRequired = blARequired;
    }
    
    public void setLOVType(String strALOVType) {
        strLOVType = strALOVType;
    }
    
    public void setDropdown(boolean blADropdown) {
        blDropdown = blADropdown;
    }
    
    public void setDefaultDate(String strADefaultDate) {
        strDefaultDate = strADefaultDate;
    }

    public void setValidationPattern(String strAValidationPattern) {
        strValidationPattern = strAValidationPattern;
    }

    /** Function returns as a string the contents of the class DBField. Debug purposes only.
     */
    public String displayDBField()
    {
        String debug;
        debug = "\n\t[DBField] Domain: " + strDomain + "\tNameForUpdate: " + strNameForUpdate +
                "\n\tInternalName: " + strInternalName + "\tShortInternalName: " + strShortInternalName +
                "\n\tExternalName: " + strExternalName + "\tLabelInForm: " + strLabelInForm +
                "\n\tLabelInColumn: " + strLabelInColumn + "\tDataType: " + intDataType +
                "\n\tLength: " + intLength + "\tNameForUpdate: " + blUnique +
                "\n\tRequired: " + blRequired + 
                "\n\tValidation Pattern: " + strValidationPattern;
        return debug;
    }
    
    public void setDisplayType(String strADisplayType){
        strDisplayType = strADisplayType;
    }
    
    public String getDisplayType(){
        return strDisplayType; 
    }
}