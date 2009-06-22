/**
 * DialogConstants.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 10/02/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * Class to define constants that used for building dialogs
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

public class DialogConstants {
    
    public static final int TOPLEFT_X = 20;
    
    public static final int TOPLEFT_Y = 20;
    
    public static final int LINE_HEIGHT = 30;
    
    public static final int GAP = 10;
    
    public static final int TEXTFIELD_WIDTH = 300;
    
    public static final int MED_TEXTFIELD_WIDTH = 200;
    
    public static final int SHORT_TEXTFIELD_WIDTH = 100;
    
    public static final int TEXTFIELD_HEIGHT = 22;
    
    public static final int TEXTAREA_WIDTH = 300;
    
    public static final int MED_TEXTAREA_WIDTH = 200;
    
    public static final int TEXTAREA_HEIGHT = 100;
    
    public static final int LABEL_WIDTH = 100;
    
    public static final int LABEL_HEIGHT = 22;
    
    public static final int BUTTON_WIDTH = 80;
    
    public static final int SHORT_BUTTON_WIDTH = 50;
    
    public static final int BUTTON_HEIGHT = 22;
    
    public static final String[] PRIORITY_OPTIONS = {"Low", "Med", "High"};
    
    public static final String[] IFFAIL_OPTIONS = {"", "Retry", "Stop", "Continue"};
    
    public static final String[] IFRETRYFAIL_OPTIONS = {"", "Stop", "Continue"};
    
    public static final String[] YESNO_OPTIONS = {"Yes", "No"};
    
    public static final String[] NOYES_OPTIONS = {"No", "Yes"};
    
    public static final String[] CONNECTION_OPTIONS = {"", "AND", "OR"};
    
    public static final String[] OPERATION_OPTIONS = {"", "=", "!=", "<", "<=", ">", ">="};
    
    public static final String[] ACTIVITY_TYPE_OPTIONS = {"Human", "System", "Start", "Stop", "Sub workflow"};
    
    public static final String[] PERFORMER = {"", "Anita", "Agus", "Hayden M", "Hayden F", "Huy", "Long", "Michael", "Shendon", "Steve", "System"};
    
    public static final String[] PACKAGE = {"Commonweath bank", "Victorian work authority", "Children hospital at Westmead"};
    
    /** Trigger period options
     */
    public static final String[] TRIGGER_PERIOD_OPTIONS = {"", "Minute", "Hour", "Day", "Week", "Month", "Year"};
    
    /** Interval period
     */
    public static final String[] INTERVAL_PERIOD_OPTIONS = {"Daily", "Weekly", "Monthly", "Yearly"};
    
    /** Hours
     */
    public static final String[] HOURS = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    
    /** Minutes
     */
    public static final String[] MINUTES = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                                            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                                            "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
                                            "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51",
                                            "52", "53", "54", "55", "56", "57", "58", "59"};
                                      
    /** AMPM
     */
    public static final String[] AMPM = {"am", "pm"};
    
    /** Data type
     */
    public static final String[] DATA_TYPES = {"String", "Date", "Integer", "Float"};
    
    /** Transition types
     */
    public static final String[] TRANSITION_TYPES = {"Normal", "Reject"};
}
