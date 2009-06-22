/*

 * SurveyBaseChannel.java

 *

 * Created on January 6, 2003, 10:46 AM

 */



package neuragenix.bio.study;

import neuragenix.common.exception.*;

import neuragenix.dao.*;

import neuragenix.security.*;

import java.sql.*;

import org.jasig.portal.services.LogService;

import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import neuragenix.common.Utilities;

import java.util.Calendar;

import neuragenix.security.AuthToken;

import bsh.Interpreter;

import bsh.EvalError;

import bsh.ParseException;



/**

 * This object extends the base channel object.

 * This is done because the survey needs methods

 * that other channel will not. E.g., moveing questions

 * up and down the survey

 * @author Administrator

 */

public class SurveyBaseChannel extends neuragenix.common.BaseChannel {

    

    /** Holds the name of the survey domain

     */    

    public static final String SURVEY_DOMAIN = "SURVEY";

    /** Holds the name of the study domain

     */    

    public static final String STUDY_DOMAIN = "STUDY";

    /** Holds the name of the study survey domain

     */    

    public static final String STUDY_SURVEY_DOMAIN = "STUDY_SURVEY";

    /** Holds the name of the survey patients domain

     */    

    public static final String SURVEY_PATIENTS_DOMAIN = "SURVEY_PATIENTS";

    /** Holds the name of the survey results domain

     */    

    public static final String SURVEY_RESULTS_DOMAIN = "SURVEY_RESULTS";
    
    public static final String BIO_SURVEY_RESULTS_DOMAIN = "BIO_SURVEY_RESULTS";

    /** Holds the name of the questions domain

     */    

    public static final String SURVEY_QUESTIONS_DOMAIN = "SURVEY_QUESTIONS";

    /** Holds the name of the study id

     */    

    public static final String INTERNAL_STUDY_ID = "intStudyID";

    /** Holds the name of the survey id

     */    

    public static final String INTERNAL_SURVEY_ID = "intSurveyID";

    /** Holds the name of the patient id

     */    

    public static final String INTERNAL_PATIENT_ID = "intInternalPatientID";

    /** Holds the name of the participant id

     */    

    public static final String INTERNAL_PARTICIPANT_ID = "intParticipantID";

    /** Holds the name of the results id

     */    

    public static final String INTERNAL_SURVEY_RESULTS_ID = "intSurveyResultsID";

    /** Holds the name of the questions id

     */    

    public static final String INTERNAL_QUESTION_ID = "intQuestionID";

    /** Holds the name of the question type

     */    

    public static final String QUESTION_TYPE = "intQuestionType";

    /** Holds the name of the survey status

     */    

    public static final String SURVEY_STATUS = "intSurveyStatus";

    /** Holds the name of the question order

     */    

    public static final String QUESTION_ORDER = "intQuestionOrder";

    /** Holds the name of the question label

     */    

    public static final String OPTION_LABEL = "strOptionLabel";

    /** Holds the name of the option value

     */    

    public static final String OPTION_VALUE = "strOptionValue";

    /** Holds the name of the option order

     */    

    public static final String OPTION_ORDER = "intOptionOrder";

    /** Holds the name of the data store for text

     */    

    public static final String SURVEY_STORE_DATATEXT = "strDataText";

    /** Holds the name of the data store for choice questions

     */    

    public static final String SURVEY_STORE_DATACHOICES = "strDataChoices";

    /** Holds the name of the data store number

     */    

    public static final String SURVEY_STORE_DATANUMERIC = "intDataNumeric";

    /** Holds the name of the data store date

     */    

    public static final String SURVEY_STORE_DATADATE = "dtDataDate";

    /** Holds the value of the text box type

     */    

    public static final String QUESTION_TYPE_TEXTBOX = "1";

    /** Holds the value of the drop down type

     */    

    public static final String QUESTION_TYPE_DROPDOWN = "2";

    /** Holds the value of the numeric type

     */    

    public static final String QUESTION_TYPE_NUMERIC = "3";

    /** Holds the value of the date type

     */    

    public static final String QUESTION_TYPE_DATE = "4";

    

    private boolean blMoreQuestions = false; 

    private boolean blBackButton = false;

    private int intNextQuestionOrder = 0;

    private int intFirstQuestionOrder = 0;

    private int intStudyID = 0;

    private int intSurveyID = 0;

    private int intPatientID = 0;

    boolean blSurveyDataValid = true;

     

    /** Creates a new instance of SurveyBaseChannel */

    public SurveyBaseChannel() {

        super();

    }

    public SurveyBaseChannel(AuthToken Auth) {

        super(Auth);

    }

    public SurveyBaseChannel(String strActivity, AuthToken authTemp)  {

        super(strActivity, authTemp);

    }

    /** Returns whether the data submitted

     * from a survey data is valid

     * after the validateSurveyData method is run

     * @return true means the survey data passed

     * the validation method

     */    

    public boolean getIsSurveyDataValid()

    {

        return blSurveyDataValid;

    }

    /** Tells the object that the user has selected the back button

     * @param anAnswer True means they have clicked the back button

     */    

    public void setBackButton(boolean anAnswer)

    {

        blBackButton = anAnswer;

    }

    /** Returns if there are more questions to come in the survey

     * @return True indicates that there are more

     * questions to come.

     */    

    public boolean getMoreQuestionsToCome()

    {

        return blMoreQuestions;

    }

    /** Returns the next question order number for a set of question returned in a survey

     * @return Returns the next question order

     */    

    public int getNextQuestionOrder(){

        return intNextQuestionOrder;

    }

    /** Return the first question number for a set of questions returned

     * @return Returns a question order number

     */    

    public int getFirstQuestionOrder(){

        return intFirstQuestionOrder;

    }

    /** Sets the study id

     * @param aStudyID a study id

     */    

    public void setStudyID(int aStudyID)

    {

        intStudyID = aStudyID;

    }

    /** sets the patient id

     * @param aPatientID a patient id

     */    

    public void setPatientID(int aPatientID)

    {

        intPatientID = aPatientID;

    }

    // This method is specific for the survey channel. It allows the movement of the question no and question order up or down a survey.

    /** It allows the movement of the question no and question order up or down a survey.

     * @param intQuestionID The questionid to move

     * @param intSurveyID The surveyID that the question belongs to

     * @param strDirection The direction to move the question

     * @throws BaseChannelInvalidRuntimeData Thrown if the runtime data is

     * not provided

     * @throws BaseChannelException Thrown if an unknown error

     * occurs

     */    

    public void setQuestionNoOrder(int intQuestionID, int intSurveyID, String strDirection) throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        int intNextDoorQuestionID = 0; // The variable to store the question id next to the question being effected

        int intNextDoorQuestionOrder = 0; // The variable to store the question order next to the question being effected

        int intNextDoorQuestionNo = 0; // The variable to store the question number next to the question being effected

        int intCurrentQuestionOrder = 0; // The variable to store the  current question order 

        int intCurrentQuestionNo = 0; // The variable to store the  current question number

        String strNextDoorTimeStamp = "";

        String strCurrentQuestionTimeStamp = "";

        

        boolean blRunUpdate = false; // This flag tells us where we need to execute the update.

        try

        {

            // This query gets all the current questions for that survey so as too adjust the question orders and numbers around the selected question to be changed

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN);

            my_query.setField(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, "");

            my_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());

            my_query.setOrderBy(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);

            // Execute the query

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults(); 

            

            // Keep looping through the questions until we reach the question we are trying to effect

            while(my_resultset.next())

            {

                // We've hit the question we want to effect

                if(intQuestionID == my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID))

                {       

                    intCurrentQuestionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER);

                    intCurrentQuestionNo = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER);

                    

                    // They are moving the question up

                    if(strDirection.equals(neuragenix.bio.study.StudyFormFields.QUESTION_MOVE_UP))

                    {

                        // If we are at the top we don't change anything otherwise change away!

                        if(my_resultset.isFirst() == false)

                        {

                            blRunUpdate = true; // We need to execute the update

                            my_resultset.previous(); // Go back one to get the question id next door so as to change it's variables. E.g to move it up or down

                            // Set the variables of the question next door

                            intNextDoorQuestionID = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID);

                            intNextDoorQuestionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER);

                            intNextDoorQuestionNo = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER);

                            strNextDoorTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN + "_Timestamp");

                            break;

                        }

                        else{blRunUpdate = false;}

                        

                    }

                    // They want to move the question down

                    else if(strDirection.equals(neuragenix.bio.study.StudyFormFields.QUESTION_MOVE_DOWN))

                    { 

                        // If we are at the bottom we don't change anything otherwise change away!

                        if(my_resultset.isLast() == false)

                        {     

                            blRunUpdate = true; // We need to execute update

                            my_resultset.next(); // Go to next question in the resultset so we can change it's values

                            intNextDoorQuestionID = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID);

                            intNextDoorQuestionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER);

                            intNextDoorQuestionNo = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER);

                            strNextDoorTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN + "_Timestamp");

                            break;

                        }

                        else{blRunUpdate = false;}

                    }

                        

                }

            }

            my_query.killResultSet(my_resultset); // Clean up the connection

            

            // Get an update query and change the question order and number!

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN);

            if(blRunUpdate == true) // Do we need to run the update?

            {

                // First change the one found

                

                my_update_query.setTimestamp(strNextDoorTimeStamp);

                

                my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intCurrentQuestionOrder).toString());

                if(intCurrentQuestionNo != 0 && intNextDoorQuestionNo != 0)

                {

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, new Integer(intCurrentQuestionNo).toString());

                }

                my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intNextDoorQuestionID).toString());

                blUpdateRecordOK = my_update_query.execute();

                if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                {

                    blUpdateRecordOK = false;

                }

                // Then update the one passed

                my_update_query.clearFields();

                my_update_query.clearWhere();

                my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intNextDoorQuestionOrder).toString());

                my_update_query.setTimestamp(runtimeData.getParameter(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN + "_Timestamp").toString());

                if(intNextDoorQuestionNo != 0 && intCurrentQuestionNo != 0)

                {

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, new Integer(intNextDoorQuestionNo).toString());

                }

                my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

                blUpdateRecordOK &= my_update_query.execute();

                if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                {

                    blUpdateRecordOK = false;

                }

                if(blUpdateRecordOK)

                {

                    my_update_query.saveChanges(); // if ok save the changes

                }

                else{my_update_query.cancelChanges();} // if there was a problem cancel changes



				//Updating ix_survey_results table

				// Get an update query and change the question order and number!

				Query my_survey_results_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

				my_survey_results_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_RESULTS_DOMAIN);

				// First change the one found

				my_survey_results_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intCurrentQuestionOrder).toString());

				my_survey_results_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intNextDoorQuestionID).toString());

				my_survey_results_update_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());

				blUpdateRecordOK = my_survey_results_update_query.execute();

				// Then update the one passed

				my_survey_results_update_query.clearFields();

				my_survey_results_update_query.clearWhere();

				my_survey_results_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intNextDoorQuestionOrder).toString());

				my_survey_results_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

				my_survey_results_update_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());

				blUpdateRecordOK &= my_survey_results_update_query.execute();

				if(blUpdateRecordOK)
				{
					my_survey_results_update_query.saveChanges(); // if ok save the changes
				}
				else
				{
					my_survey_results_update_query.cancelChanges(); // if there was a problem cancel changes
				} 

				//Updating ix_bio_survey_results table

				// Get an update query and change the question order and number!

				Query my_bio_survey_results_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

				my_bio_survey_results_update_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);

				// First change the one found

				my_bio_survey_results_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intCurrentQuestionOrder).toString());

				my_bio_survey_results_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intNextDoorQuestionID).toString());

				my_bio_survey_results_update_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());


				blUpdateRecordOK = my_bio_survey_results_update_query.execute();

				// Then update the one passed

				my_bio_survey_results_update_query.clearFields();

				my_bio_survey_results_update_query.clearWhere();

				my_bio_survey_results_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intNextDoorQuestionOrder).toString());

				my_bio_survey_results_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

				my_bio_survey_results_update_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());


				blUpdateRecordOK &= my_bio_survey_results_update_query.execute();

				if(blUpdateRecordOK)
				{
					my_bio_survey_results_update_query.saveChanges(); // if ok save the changes
				}

				else{my_bio_survey_results_update_query.cancelChanges();} // if there was a problem cancel changes


                // Clean up
				
				my_survey_results_update_query = null;
				
				my_bio_survey_results_update_query = null;
				
                my_update_query = null;

                my_query = null;

           } 

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            LogService.instance().log(LogService.ERROR, npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to set Question No - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to set Question No - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to set Question No - " + e.toString());

        }

        

    }

     // This method is specific to the survey channel. It deletes the question, but must also adjust the question no and question order for the deleted question

    /** It deletes the question, but must also adjust the question no and question order for the deleted question

     * @param intQuestionID The question to delete

     * @param intSurveyID The survey ID the question belongs to

     * @return True indicates the question has

     * been deleted

     * @throws BaseChannelException Thrown when an unknown

     * error occurs

     */    

    public boolean deleteQuestion(int intQuestionID, int intSurveyID) throws BaseChannelException

    {

        boolean blRunUpdate = false; // Flag to see if we can execute update

        boolean blUpdateRecordOK = false;

        boolean blDeleteOK = false; // Tells the caller of the method if the delete was ok

        int intPreviousQuestionOrder = 0; // Variable to store the previous question order

        int intPreviousQuestionNo = 0; // Variable to store the previous question number

        String strPreviousQuestionTimeStamp = ""; 

        int intQuestionType = 0; // Variable to store the type of question, e.g., page break, drop down

        try

        {

            // The update query

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN);

            // The survey result update query

            Query my_survey_result_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_survey_result_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_RESULTS_DOMAIN);

            // The bio survey result update query

            Query my_bio_survey_result_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_bio_survey_result_update_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);
			
            // The select query

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN);

            my_query.setField(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_TYPE, "");

            my_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intSurveyID).toString());

            my_query.setOrderBy(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);

            

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults();     

            

            while(my_resultset.next())

            {

                // We've found the spot where the question supplied is.

                if(intQuestionID == my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID))

                {

                    blRunUpdate = true; 

                    my_update_query.clearFields();

                    my_update_query.clearWhere();

                    my_update_query.setTimestamp(my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN + "_Timestamp"));

                    my_update_query.setField(DBSchema.DELETED_FIELD, "-1");

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, "0");

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, "0");

                    my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

                    blUpdateRecordOK = my_update_query.execute();

                    if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                    {

                        blUpdateRecordOK = false;

                    }

					//Updating ix_survey_results table

                    my_survey_result_update_query.setField(DBSchema.DELETED_FIELD, "-1");

                    my_survey_result_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, "0");

                    my_survey_result_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

                    blUpdateRecordOK &= my_survey_result_update_query.execute();

					//Updating ix_bio_survey_results table

                    my_bio_survey_result_update_query.setField(DBSchema.DELETED_FIELD, "-1");

                    my_bio_survey_result_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, "0");

                    my_bio_survey_result_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

                    blUpdateRecordOK &= my_bio_survey_result_update_query.execute();



                    intPreviousQuestionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER);

                    intQuestionType = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_TYPE);

                    // Only get the previousQuestion Number if it's not a comment, title, or pagebreak

                    if(neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_COMMENT != intQuestionType && neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_TITLE != intQuestionType && neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_PAGEBREAK != intQuestionType)

                    {

                        intPreviousQuestionNo = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER);

                    }

                    my_resultset.next();

                    

                }

                // Only update if we have found the question we are looking for 

                if(blRunUpdate == true && my_resultset.isAfterLast() == false)

                {

                    strPreviousQuestionTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_QUESTIONS_DOMAIN + "_Timestamp");

                    my_update_query.clearFields();

                    my_update_query.clearWhere();

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intPreviousQuestionOrder).toString());

                    my_update_query.setTimestamp(strPreviousQuestionTimeStamp);

                    my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, my_resultset.getString(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID));

                    

                    intQuestionType = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_TYPE);

                    // Only update change the question no if it's not a comment, title or page break.

                    if(neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_COMMENT != intQuestionType && neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_TITLE != intQuestionType && neuragenix.bio.study.StudyFormFields.QUESTION_TYPE_PAGEBREAK != intQuestionType && intPreviousQuestionNo > 0)

                    {

                         my_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER, new Integer(intPreviousQuestionNo).toString());

                         intPreviousQuestionNo = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_NUMBER);

                        

                    }

                                       

                    blUpdateRecordOK &= my_update_query.execute();

                    if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                    {

                        blUpdateRecordOK = false;

                    }


					//Updating ix_survey_results table
				
                    my_survey_result_update_query.clearFields();

                    my_survey_result_update_query.clearWhere();

                    my_survey_result_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intPreviousQuestionOrder).toString());

                    my_survey_result_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, my_resultset.getString(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID));

                    blUpdateRecordOK &= my_survey_result_update_query.execute();


					//Updating ix_bio_survey_results table	
				
                    my_bio_survey_result_update_query.clearFields();

                    my_bio_survey_result_update_query.clearWhere();

                    my_bio_survey_result_update_query.setField(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER, new Integer(intPreviousQuestionOrder).toString());

                    my_bio_survey_result_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, my_resultset.getString(neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID));

                    blUpdateRecordOK &= my_bio_survey_result_update_query.execute();


                    intPreviousQuestionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.QUESTION_ORDER);

                }

              }

            

            my_query.killResultSet(my_resultset); // Release the connection

            

            // IF all is ok save all changes to the database.

            if(blUpdateRecordOK)

            {

                 blDeleteOK = true;

				 // if ok save the changes
                 my_update_query.saveChanges(); 

                 my_survey_result_update_query.saveChanges();
				 
                 my_bio_survey_result_update_query.saveChanges();


            }

            else
			{
				// if there was a problem cancel changes
				my_update_query.cancelChanges();
		
				my_survey_result_update_query.cancelChanges();	
				
				my_bio_survey_result_update_query.cancelChanges();	
			} 


            // Clean up    

			my_survey_result_update_query = null;
	
			my_bio_survey_result_update_query = null;
			
            my_update_query = null;

            my_query = null;

           

           return blDeleteOK; // Tell the caller of the method of whether the delete was successful

            

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to delete question - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to delete question - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to delete question - " + e.toString());

        }

   }

    // This method is specific for the survey channel. It allows the movement of the question no and question order up or down a survey.

    /** It allows the movement of the question no and question order up or down a survey.

     * @param intOptionID The optionid to move

     * @throws BaseChannelInvalidRuntimeData Thrown if the runtime data is

     * not provided

     * @throws BaseChannelException Thrown if an unknown error

     * occurs

     * @param strDirection Which direction to move the option (up or down!)

     * @param intQuestionID Which question the option relates to

     */       

    public void setOptionOrder(int intOptionID, int intQuestionID, String strDirection) throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        // Variables to store the current option values and also the ones next door

        int intNextDoorOptionID = 0;

        int intNextDoorOptionOrder = 0;

        int intCurrentOptionOrder = 0;

        String strNextDoorTimeStamp = "";

        String strCurrentQuestionTimeStamp = "";

        boolean blRunUpdate = false;

        try

        {

            // A select query to get all the options for a particular question

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            my_query.setField(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, "");

            my_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

            my_query.setOrderBy(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, DBSchema.ORDERBY_ASENDING);

            

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults();     

            // Keep looping until we have found the spot of the option being effect

            while(my_resultset.next())

            {

                // We've found a matching option id.

                if(intOptionID == my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID))

                {

                      intCurrentOptionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.OPTION_ORDER);

                    // They are moving the question up

                    if(strDirection.equals(neuragenix.bio.study.StudyFormFields.OPTION_MOVE_UP))

                    {

                        if(my_resultset.isFirst() == false) // Make sure it's not the first record - if it is do nothing!

                        {

                            blRunUpdate = true; // Set flag to incidate that we need to execute

                            my_resultset.previous(); // Move one record previous

                            // Get the values of the previous option

                            intNextDoorOptionID = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID);

                            intNextDoorOptionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.OPTION_ORDER);

                            strNextDoorTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN + "_Timestamp");

                            break;

                        }

                        else{blRunUpdate = false;}

                        

                    }

                      // The want to move the option down the list!

                    else if(strDirection.equals(neuragenix.bio.study.StudyFormFields.OPTION_MOVE_DOWN))

                    {

                        if(my_resultset.isLast() == false) // Only edit it if it's not the last record

                        {

                            blRunUpdate = true; // Set the flag to execute the update

                            my_resultset.next(); // Move the recordset next door

                            // Store the values of the option next door

                            intNextDoorOptionID = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID);

                            intNextDoorOptionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.OPTION_ORDER);

                            strNextDoorTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN + "_Timestamp");

                            break;

                        }

                        else{blRunUpdate = false;}

                    }

                }

            }

            my_query.killResultSet(my_resultset); // Release the connection

            

            // Get an update query and change the question order and number!

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            if(blRunUpdate == true)

            {

                my_update_query.setTimestamp(strNextDoorTimeStamp);

                // First change the one found

                my_update_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, new Integer(intCurrentOptionOrder).toString());

                my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intNextDoorOptionID).toString());

                blUpdateRecordOK = my_update_query.execute();

                if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                {

                    blUpdateRecordOK = false;

                }

                // Then update the one passed

                my_update_query.clearFields();

                my_update_query.clearWhere();

                my_update_query.setTimestamp(runtimeData.getParameter(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN + "_Timestamp").toString());

                my_update_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, new Integer(intNextDoorOptionOrder).toString());

                my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intOptionID).toString());

                blUpdateRecordOK &= my_update_query.execute();

                if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                {

                    blUpdateRecordOK = false;

                }

                if(blUpdateRecordOK)

                {

                    my_update_query.saveChanges(); // if ok save the changes

                }

                else{my_update_query.cancelChanges();} // if there was a problem cancel changes

                //Clean up

                my_update_query = null;

                my_query = null;

           }    

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to set Question No - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to set Question No - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to set Question No - " + e.toString());

        }

        

    }

    /** Deletes an option and changes the question orders of the options around the deleted option

     * @param intOptionID Which option to delete

     * @param intQuestionID Which question the option relates to

     * @return True means the option was deleted

     * successfully

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public boolean deleteOption(int intOptionID, int intQuestionID) throws BaseChannelException

    {

        boolean blDeleteOK = false; // Tells the caller of the method if the delete was ok

        boolean blRunUpdate = false;

        boolean blUpdateRecordOK = false;

        String strPreviousQuestionTimeStamp = "";

        int intPreviousOptionOrder = 0;

        try

        {

            // The update query

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            

            // The select query

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDomainName(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            my_query.setField(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, "");

            my_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, "");

            my_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_QUESTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intQuestionID).toString());

            my_query.setOrderBy(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, DBSchema.ORDERBY_ASENDING);

            

            blSearchRecordOK = my_query.execute();

            if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

            {

                    blUpdateRecordOK = false;

            }

            ResultSet my_resultset = my_query.getResults();     

            // Loop until we find the spot of the question we are trying to change

            while(my_resultset.next())

            {

                // We've found the spot where the question supplied is.

                if(intOptionID == my_resultset.getInt(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID))

                {

                    blRunUpdate = true; 

                    my_update_query.clearFields();

                    my_update_query.clearWhere();

                    my_update_query.setTimestamp(my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN + "_Timestamp"));

                    my_update_query.setField(DBSchema.DELETED_FIELD, "-1");

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, "0");

                    my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, new Integer(intOptionID).toString());

                    blUpdateRecordOK = my_update_query.execute();

                    intPreviousOptionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.OPTION_ORDER);

                    my_resultset.next();

                    

                }

                // Only update if we have found the question we are looking for 

                if(blRunUpdate == true && my_resultset.isAfterLast() == false)

                {

                    strPreviousQuestionTimeStamp = my_resultset.getString(neuragenix.bio.study.StudyFormFields.SURVEY_OPTIONS_DOMAIN + "_Timestamp");

                    my_update_query.clearFields();

                    my_update_query.clearWhere();

                    my_update_query.setTimestamp(strPreviousQuestionTimeStamp);

                    my_update_query.setField(neuragenix.bio.study.StudyFormFields.OPTION_ORDER, new Integer(intPreviousOptionOrder).toString());

                    my_update_query.setWhere("", neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, my_resultset.getString(neuragenix.bio.study.StudyFormFields.INTERNAL_OPTION_ID));    

                    blUpdateRecordOK &= my_update_query.execute();

                    if(blUpdateRecordOK && my_update_query.getUpdatedRecordCount() == 0)

                    {

                        blUpdateRecordOK = false;

                    }

                    intPreviousOptionOrder = my_resultset.getInt(neuragenix.bio.study.StudyFormFields.OPTION_ORDER);

                }

              }

            

            my_query.killResultSet(my_resultset);

            

            // IF all is ok save all changes to the database.

            if(blUpdateRecordOK)

            {

                blDeleteOK = true;

                my_update_query.saveChanges(); // if ok save the changes
            }

            else{my_update_query.cancelChanges();} // if there was a problem cancel changes

            

            //Clean up

            my_update_query = null;

            my_query = null;

           

           return blDeleteOK; // Tell the caller of the method of whether the delete was successful

            

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to delete question - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to delete question - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to delete question - " + e.toString());

        }

   }

   

     /** This method builds the survey to study relationship table for a participant

      * @throws BaseChannelException Thrown when an unknown error

      * occurs

      */    

    public void buildSurveyPatientTable() throws BaseChannelException

    {

        try{

            String intInternalPatientID = new Integer(intPatientID).toString();

            // This select query gets the surveys for a study

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDomainName(STUDY_SURVEY_DOMAIN);

            my_query.setField(INTERNAL_STUDY_ID, "");

            my_query.setField(INTERNAL_SURVEY_ID, "");

            my_query.setWhere("", INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, new Integer(intStudyID).toString());

        

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults(); 

            

            // This select query gets the surveys for a study

            Query my_patient_survey_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_patient_survey_query.setDomainName(SURVEY_PATIENTS_DOMAIN);

            my_patient_survey_query.setField(INTERNAL_STUDY_ID, "");

            my_patient_survey_query.setField(INTERNAL_SURVEY_ID, "");

            my_patient_survey_query.setWhere("", INTERNAL_PATIENT_ID, DBSchema.EQUALS_OPERATOR, intInternalPatientID);

        

            blSearchRecordOK = my_patient_survey_query.execute();

            ResultSet my_patient_survey_resultset = my_patient_survey_query.getResults(); 

            

            // This insert query will add the selected surveys to the participant to study table

            Query my_insert_query = new Query(Query.INSERT_QUERY, strActivityRequested, authToken);

            my_insert_query.setDomainName(SURVEY_PATIENTS_DOMAIN);

            boolean blInsertNewRecord = true;

            // Loop through the surveys and add them to the table

            while(my_resultset.next())

            {

                blInsertNewRecord = true;

                my_patient_survey_resultset.beforeFirst();

                while(my_patient_survey_resultset.next())

                {

                   // System.err.println("STUDY ID 1 =" + my_resultset.getInt(INTERNAL_STUDY_ID));

                   // System.err.println("STUDY ID 2 =" + my_patient_survey_resultset.getInt(INTERNAL_STUDY_ID));

                   // System.err.println("SURVEY ID 1 =" + my_resultset.getInt(INTERNAL_SURVEY_ID));

                  //  System.err.println("SURVEY ID 2 =" + my_patient_survey_resultset.getInt(INTERNAL_SURVEY_ID));

                    if(my_resultset.getInt(INTERNAL_STUDY_ID) == my_patient_survey_resultset.getInt(INTERNAL_STUDY_ID) && my_resultset.getInt(INTERNAL_SURVEY_ID) == my_patient_survey_resultset.getInt(INTERNAL_SURVEY_ID))

                    {

                     //   System.err.println("WE HAVE A FALSE");

                        blInsertNewRecord = false; // We already have the record in the db

                        my_patient_survey_resultset.last();

                    }

                }

                if(blInsertNewRecord == true)

                {

                  //  System.err.println("EXECUTE");

                    my_insert_query.clearFields();

                    my_insert_query.setField(INTERNAL_SURVEY_ID, my_resultset.getString(INTERNAL_SURVEY_ID));

                    my_insert_query.setField(INTERNAL_STUDY_ID, my_resultset.getString(INTERNAL_STUDY_ID));

                    my_insert_query.setField(INTERNAL_PATIENT_ID, intInternalPatientID);

                    my_insert_query.setField(SURVEY_STATUS, "0");

                

                    boolean blAddRecord = my_insert_query.execute();

       

                    if(blAddRecord)

                    {

                        my_insert_query.saveChanges(); // if ok save the changes        

                    }

                    else{my_insert_query.cancelChanges();} // if there was a problem cancel changes

            

                }

            }

            //Clean up

            my_query.killResultSet(my_resultset); //Release the resultset

            my_patient_survey_query.killResultSet(my_patient_survey_resultset); //Release the resultset

            my_insert_query = null;

            my_query = null;

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to add new record - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to add new record - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to add new record - " + e.toString());

        }

    }

    /** This method build the xml file

     * for the questions of a survey.

     * It builds the display question and

     * also gets any previous results entered

     * for a particular question

     * @throws BaseChannelInvalidRuntimeData Thrown when not runtimeData

     * is supplied for the question

     * it is dealing with

     * @throws BaseChannelException Thrown when an unknown error

     * occurs.

     */    

    public void buildSurveySearchResultsXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = ""; // Used to store the temporary display name

        boolean blFirstTimeAround = true; // This flag to indicate if its the first time around the resultset loop

        boolean blGetOptions = false; // This flag is to indicate whether we need to get the options for a question

        String intQuestionIDToGet = "0"; // Tell the options query which id to get

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

   

        try 

            {

            // Get the essential values for creating the questions

            String intStudyIDTemp = runtimeData.getParameter(INTERNAL_STUDY_ID);

            String intSurveyIDTemp = runtimeData.getParameter(INTERNAL_SURVEY_ID);

            String intInternalPatientIDTemp = runtimeData.getParameter(INTERNAL_PATIENT_ID);

            

            // This is the results query so we can populate the questions with answers already given

            Query my_select_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_select_query.setDomainName(SURVEY_RESULTS_DOMAIN);

            my_select_query.setField(INTERNAL_SURVEY_RESULTS_ID, "");

            my_select_query.setField(INTERNAL_SURVEY_ID, "");

            my_select_query.setField(INTERNAL_STUDY_ID, "");

            my_select_query.setField(INTERNAL_PARTICIPANT_ID, "");

            my_select_query.setField(INTERNAL_QUESTION_ID, "");

            my_select_query.setField(QUESTION_TYPE, "");

            my_select_query.setField(SURVEY_STORE_DATATEXT, "");

            my_select_query.setField(SURVEY_STORE_DATACHOICES, "");

            my_select_query.setField(SURVEY_STORE_DATANUMERIC, "");

            my_select_query.setField(SURVEY_STORE_DATADATE, "");

            my_select_query.setField(QUESTION_ORDER, "");

            my_select_query.setWhere("", INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_STUDY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intStudyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_PARTICIPANT_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intInternalPatientIDTemp);

            //my_select_query.setOrderBy(Study FormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);

            my_select_query.setOrderBy(QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
           // COULD GAIN SOME EFFICIENCES HERE  ---- BUT we can't because we need previous data for the scripting section

            // my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.GREATERTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder"));

            

            blSearchRecordOK = my_select_query.execute();

            ResultSet my_results_resultset= my_select_query.getResults();     

            

            // This part gets options query ready in case we need it

            Query my_options_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_options_query.setDomainName(StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            my_options_query.setField(OPTION_LABEL, "");

            my_options_query.setField(OPTION_VALUE, "");

            my_options_query.setOrderBy(OPTION_ORDER, DBSchema.ORDERBY_ASENDING);

        

            // This is the questions query it only gets the questions greater than the last question order

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDeletedColumn(blDeletedColumn);

            for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

            {

                my_query.setDomainName(vecDomains.get(intCounter).toString());

            }

            while(enumInternalFormFields.hasMoreElements())

            {         

                my_query.setField(enumInternalFormFields.nextElement().toString(), "");

            }

            for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

            {

                my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

            }

            for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

            {

                my_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

            }

            

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults();

                

                

            String strTempInternalFormName; // Stores the internal name of the varible we are dealing with

            String strTempXML = ""; //

            boolean blComplete = false; // This flag indicates whether to stop processing through the question as we have hit a page break or the end of the survey

            

                // If the have hit the back button count back two page breaks and then start the results set

                if(blBackButton == true)

                {                 

                    my_resultset.afterLast();

                    int intPageBreakCounter = 0;

                    // While we haven't countered two page breaks or are at the start of the resultset, 

                    while(intPageBreakCounter != 2 && my_resultset.isBeforeFirst() == false)

                    {

                        my_resultset.previous();

                        if(my_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_PAGEBREAK) // We have a page break!

                        {

                           intPageBreakCounter ++;

                        }

                    }

                }

                // Loop through the survey questions and build the XML file

                while(my_resultset.next())

                {

                    // Get the firstQuestionOrder - Not always the first record in the resultset if they have pressed the back button!

                     if(blFirstTimeAround == true)

                    {

                        intFirstQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER);

                    }

                     

                    blGetOptions = false; // Reset this flag

                    strTempXML = "";

                    

                    // Open the tag

                    strTempXML += "<" + strSearchResultTag + ">"; // Open the tag for this group of variable

                    

                    enumInternalFormFields = hashFormFields.keys();

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        // If it's a page break stop processing   

                        if(strTempInternalFormName.equals(StudyFormFields.QUESTION_TYPE) && my_resultset.getInt(strTempInternalFormName) == StudyFormFields.QUESTION_TYPE_PAGEBREAK)

                        {

                            blComplete = true;

                            // Get the next questionId for the channel

                            intNextQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER);

                            if(my_resultset.isLast()) // If it's the last record there are no more to come

                            {

                                blMoreQuestions = false;   

                            }

                            else

                            {

                                blMoreQuestions = true;

                                my_resultset.last();

                                

                            }

                        }

                        // If it'a a drop down we need the values -- this flag tells us that we need to get options for this question

                        else if(strTempInternalFormName.equals(StudyFormFields.QUESTION_TYPE) && my_resultset.getString(strTempInternalFormName).equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blGetOptions = true;

                            intQuestionIDToGet = my_resultset.getString(INTERNAL_QUESTION_ID);

                        }

                        

                        strTempXML += "<" + strTempInternalFormName + ">"; // Open the tag for this particular variable

                   

                        if(my_resultset.getString(strTempInternalFormName) != null)

                        {

                            // If it's a date convert it to the correct display format 

                            if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                            {

                                strTempXML += Utilities.convertDateForDisplay(my_resultset.getString(strTempInternalFormName));

                            }

                            else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                            {

                                strTempXML += Utilities.convertTimeForDisplay(my_resultset.getString(strTempInternalFormName));

                            }

                            else{

                            

                                  if(hashCutFields != null && hashCutFields.size() > 0)

                                  {

                                          

                                       if(hashCutFields.containsKey(strTempInternalFormName))

                                       {

                                         

                                                // Make sure the length is greater than what you are trying to cut!

                                           if(my_resultset.getString(strTempInternalFormName).length() > ((Integer)hashCutFields.get(strTempInternalFormName)).intValue())

                                           {

                                                strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName).substring(0, ((Integer)hashCutFields.get(strTempInternalFormName)).intValue()) + " ...");

                                            }

                                            else{strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));}

                                       }

                                       else

                                       {

                                               strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                       }

                                    }

                                    else

                                    {

                                             strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                    }

                                                                       

                            }

                        }

                        strTempXML += "</" + strTempInternalFormName + ">"; // Close the tag for this particular variable

                        

                    }

                    // ---------- GET OPTIONS IF NEED FOR THIS QUESTION ----------

   //                 if(blGetOptions == true)

                    if(blGetOptions == true && blComplete == false)

                    {

                        my_options_query.clearWhere();

                        my_options_query.setWhere("", INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, intQuestionIDToGet);

                        blSearchRecordOK = my_options_query.execute();

                        

                        ResultSet my_options_resultset = my_options_query.getResults();

                        

                        while(my_options_resultset.next())

                        {

                            strTempXML += "<options>";

                            strTempXML += "<" + Utilities.cleanForXSL(OPTION_LABEL) + ">" + Utilities.cleanForXSL(my_options_resultset.getString(OPTION_LABEL)) + "</" + Utilities.cleanForXSL(OPTION_LABEL) + ">";

                            strTempXML += "<" + Utilities.cleanForXSL(OPTION_VALUE) + ">" + Utilities.cleanForXSL(my_options_resultset.getString(OPTION_VALUE)) + "</" + Utilities.cleanForXSL(OPTION_VALUE) + ">";

                            strTempXML += "</options>";

                        }

                        

                         my_options_query.killResultSet(my_options_resultset);

                    }

                    // ---------- GET PREVIOUSLY ENTERED RESULTS FOR THIS QUESTION --------

                    strTempXML += "<survey_result>";

                    

                    if(blComplete == false)

                    {

                      // If it's not a script do the normal thing and get any passed results for this question

                      if(my_resultset.getInt(QUESTION_TYPE) != StudyFormFields.QUESTION_TYPE_SCRIPT)

                      {

                        while(my_results_resultset.next())

                        {

                            if(my_results_resultset.getInt(INTERNAL_QUESTION_ID) == my_resultset.getInt(INTERNAL_QUESTION_ID))

                            {

                                strTempXML += "<survey_data>";

                                // Get the data from the correct column depending on type

                                if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_TEXTBOX)

                                {

                                    strTempXML += Utilities.cleanForXSL(my_results_resultset.getString(SURVEY_STORE_DATATEXT));

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DROPDOWN)

                                {

                                    strTempXML += Utilities.cleanForXSL(my_results_resultset.getString(SURVEY_STORE_DATACHOICES));

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_NUMERIC)

                                {

                                    if(my_results_resultset.getString(SURVEY_STORE_DATANUMERIC) != null)

                                    {

                                        strTempXML += my_results_resultset.getString(SURVEY_STORE_DATANUMERIC);

                                    }

                                    else{strTempXML += "";}

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DATE)

                                {

                                    if(my_results_resultset.getString(SURVEY_STORE_DATADATE) != null)

                                    {

                                        strTempXML += Utilities.convertDateForDisplay(my_results_resultset.getString(SURVEY_STORE_DATADATE));

                                    }

                                    else{strTempXML += "";}

                                }

                                strTempXML += "</survey_data>";

                                strTempXML += "<SURVEY_RESULTS_Timestamp>";

                                strTempXML += my_results_resultset.getString(StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp");

                                strTempXML += "</SURVEY_RESULTS_Timestamp>";

                             //   break; // ADDED -  5/3/2003 - HIDBA 

                      //   TAKEN OUT 5/3/2003 - HIDBA         

                                my_results_resultset.last(); // Get out of the loop

                            }

                        }

                      }

                        // IT's a script process the code!

                      else

                      {

                             strTempXML += "<survey_data>" + runQuestionScript(my_resultset.getString(StudyFormFields.QUESTION_SCRIPT), my_results_resultset) +  "</survey_data>";

                     

                        

                      }

                       strTempXML += "</survey_result>";

                    }

               // TAKEN OUT 5/3/2003 - HIDBA  

                    my_results_resultset.beforeFirst(); // Reset the resultset for the next time around!

                    

                    strTempXML += "</" + strSearchResultTag + ">"; // Close off the tag for this set of variables

                    intRecordCount ++;

                    if(blComplete == false)

                    {

                        strXML += strTempXML;

                    }

                    // This flag tells us that the resultset has been around once

                    blFirstTimeAround = false;

                }

                // If the next order number has not been set make it the last record in the recordset because it is the final question set

                if(intNextQuestionOrder == 0 && my_resultset != null)

                {

                    my_resultset.last();

                  // Add one for the next question Order

                    intNextQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER) + 1;

                 }

                        

                // Return the resultsets to be killed

                my_query.killResultSet(my_resultset);

                my_select_query.killResultSet(my_results_resultset);

                

                // Clean up

                my_query = null;

                my_select_query = null;

                my_options_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString());

        }

    }

    

    /** This method saves the answers a set

     * of questions for a survey

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData

     * is provided for a question

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void saveSurveyData() throws BaseChannelInvalidRuntimeData, BaseChannelException

    { 

       try

       {

            // Get the Next question order for the set of questions submitted

            int intHighestQuestionOrder = new Integer(runtimeData.getParameter("intNextQuestionOrder")).intValue();

            

            int intFirstQuestionOrderTemp = new Integer(runtimeData.getParameter("intFirstQuestionOrder")).intValue();

            // Get the study, suryve and patient id

            String intStudyIDTemp = runtimeData.getParameter(INTERNAL_STUDY_ID);

            String intSurveyIDTemp = runtimeData.getParameter(INTERNAL_SURVEY_ID);

            String intInternalPatientIDTemp = runtimeData.getParameter(INTERNAL_PATIENT_ID);

            

            // This query gets the results already added for a participant for a particular survey

            Query my_select_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_select_query.setDomainName(SURVEY_RESULTS_DOMAIN);

            my_select_query.setField(INTERNAL_SURVEY_RESULTS_ID, "");

            my_select_query.setField(INTERNAL_SURVEY_ID, "");

            my_select_query.setField(INTERNAL_STUDY_ID, "");

            my_select_query.setField(INTERNAL_PARTICIPANT_ID, "");

            my_select_query.setField(QUESTION_ORDER, "");

            my_select_query.setWhere("", INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_STUDY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intStudyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_PARTICIPANT_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intInternalPatientIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.LESSTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder"));

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.GREATERTHAN_AND_EQUAL_OPERATOR, runtimeData.getParameter("intFirstQuestionOrder"));

            my_select_query.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_DESCENDING);

            blSearchRecordOK = my_select_query.execute();

            ResultSet my_resultset= my_select_query.getResults();     

            

            // This is the query used in case we need to do a insert

            Query my_insert_query = new Query(Query.INSERT_QUERY, strActivityRequested, authToken);

            my_insert_query.setDomainName(SURVEY_RESULTS_DOMAIN);

            // This is the qurey used if we need to update existing data

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(SURVEY_RESULTS_DOMAIN);

            

            String intQuestionOrder = "";

            String intParticipantIDTemp = "";

            String intSurveyResultsIDTemp ="";

            

            boolean blExecute = false;

            boolean blUpdate = false;

            boolean blAddRecord = true;

            boolean blAddAllRecords = false;

            boolean blUpdateRecordOK = true;

            boolean blUpdateAllRecords = false;

            // Keep looping until you go from the highest order no to the first order number for the page

            for(int intCounter = intHighestQuestionOrder; intCounter > (intFirstQuestionOrderTemp - 1); intCounter --)

            {

                intQuestionOrder = new Integer(intCounter).toString();

                if(runtimeData.getParameter("blDataQuestion_" + intQuestionOrder) != null && runtimeData.getParameter("blDataQuestion_" + intQuestionOrder).equals("true")) // We have a question requiring data capture

                {

                 // TAKEN OUT 050302   

                    my_resultset.beforeFirst();

                    blExecute = false; // Flag indicateing whether we should execute

                    blUpdate = false; // Flag indicateing whether we should run the update query

                                            

                    while(my_resultset.next()) // See if a record already exists ... if it does update don't insert

                    {

                        if(my_resultset.getString(INTERNAL_SURVEY_ID).equals(intSurveyIDTemp) && my_resultset.getString(QUESTION_ORDER).equals(intQuestionOrder) && my_resultset.getString(INTERNAL_STUDY_ID).equals(intStudyIDTemp))

                        {

                            blUpdate = true;

                            intSurveyResultsIDTemp = my_resultset.getString(INTERNAL_SURVEY_RESULTS_ID);

                // TAKEN OUT 050302            

                            my_resultset.last();

                       //     break; // ADDED 050302  

                        }

                     }

                     if(blUpdate == true) // if we have to update, update!

                     {

                    

                        my_update_query.clearFields();

                        my_update_query.setTimestamp(runtimeData.getParameter(intQuestionOrder + "_" + StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp"));

                        my_update_query.setField(INTERNAL_SURVEY_ID, intSurveyIDTemp);

                        

                        my_update_query.setField(INTERNAL_STUDY_ID, intStudyIDTemp);

                        

                        my_update_query.setField(INTERNAL_PARTICIPANT_ID, intInternalPatientIDTemp);

                       

                        my_update_query.setField(QUESTION_ORDER, intQuestionOrder);

                        my_update_query.setField(INTERNAL_QUESTION_ID, runtimeData.getParameter("intQuestionID_" + intQuestionOrder));

                        

                        my_update_query.clearWhere();

                        my_update_query.setWhere("", INTERNAL_SURVEY_RESULTS_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyResultsIDTemp);

                 

                        // Depending on the type store it in the correct column

                        if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_TEXTBOX))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_TEXTBOX);

                            my_update_query.setField(SURVEY_STORE_DATATEXT, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_DROPDOWN);

                            my_update_query.setField(SURVEY_STORE_DATACHOICES, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_NUMERIC))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_NUMERIC);

                            my_update_query.setField(SURVEY_STORE_DATANUMERIC, runtimeData.getParameter(intQuestionOrder));

                            

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DATE))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_DATE);

                            my_update_query.setField(SURVEY_STORE_DATADATE, runtimeData.getParameter(intQuestionOrder));

                            

                        }

                       // execute??? - we don't execute for page break, comments, titles or scripts

                        if(blExecute = true)

                        {

                            if(blUpdateRecordOK == true) // Only attempt to add if we haven't have a problem so far

                            {

                                blUpdateRecordOK = my_update_query.execute();

                            }

                            blUpdateAllRecords = true;

                        }

                    }

                    else // else insert

                    {

                        my_insert_query.clearFields();

                        my_insert_query.setField(INTERNAL_SURVEY_ID, intSurveyIDTemp);

                        my_insert_query.setField(INTERNAL_STUDY_ID, intStudyIDTemp);

                        my_insert_query.setField(INTERNAL_PARTICIPANT_ID, intInternalPatientIDTemp);

                        my_insert_query.setField(QUESTION_ORDER, intQuestionOrder);

                        my_insert_query.setField(INTERNAL_QUESTION_ID, runtimeData.getParameter("intQuestionID_" + intQuestionOrder));

                      // Depending on the type store it in the correct column

                         if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_TEXTBOX))

                        {

                            blExecute = true;

                            my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_TEXTBOX);

                            my_insert_query.setField(SURVEY_STORE_DATATEXT, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blExecute = true;

                            my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_DROPDOWN);

                            my_insert_query.setField(SURVEY_STORE_DATACHOICES, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_NUMERIC))

                        {

                            if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                            {

                                blExecute = true;

                                my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_NUMERIC);

                                my_insert_query.setField(SURVEY_STORE_DATANUMERIC, runtimeData.getParameter(intQuestionOrder));

                            }

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DATE))

                        {

                            if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                            {

                                blExecute = true;

                                my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_DATE);

                                my_insert_query.setField(SURVEY_STORE_DATADATE, runtimeData.getParameter(intQuestionOrder));

                            }

                        }



                    // Execute?? - don't insert for title, comments, page break, script

                        if(blExecute == true)

                        {

                            if(blAddRecord == true) // Only attempt to add if we haven't have a problem so far

                            {

                                blAddRecord = my_insert_query.execute();

                            }

                            blAddAllRecords = true;

                        }

                }

            }

        }

            // Commit final updates and inserts

                if(blAddAllRecords == true)

                {

                     if(blAddRecord == true)

                     {

                               my_insert_query.saveChanges(); // if ok save the changes        

                     }

                     else{my_insert_query.cancelChanges();} // if there was a problem cancel changes

                }

                if(blUpdateAllRecords == true)

                {

                            if(blUpdateRecordOK == true)

                            {

                                my_update_query.saveChanges(); // if ok save the changes

                            }

                            else{my_update_query.cancelChanges();} // if there was a problem cancel changes

                }

                

            // Clean up

             my_select_query.killResultSet(my_resultset);

             my_insert_query = null;

             my_update_query = null;

             my_select_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString());

        }

    }

    /** This method validates the questions submitted

     * to a survey to ensure the data is of the correct

     * type of meets the ranges set.

     */    

    public void validateSurveyData()

    {

        // Define some falgs to inidicate whether we have to display a error message

        boolean blDataTypeOK = true;

        boolean blDataInRange = true;

        String intQuestionOrder = "";

        Vector vecErrorInvalidDataFields = new Vector();

        Vector vecErrorInvalidDataRange = new Vector();

        // Get the Next question order for the set of questions submitted

        int intHighestQuestionOrder = new Integer(runtimeData.getParameter("intNextQuestionOrder")).intValue();

        int intFirstQuestionOrderTemp = new Integer(runtimeData.getParameter("intFirstQuestionOrder")).intValue();

        

        for(int intCounter = intFirstQuestionOrderTemp; intCounter < (intHighestQuestionOrder); intCounter ++)

        {

                // Get the question order for the loop

                intQuestionOrder = new Integer(intCounter).toString();

                

                // Build the xml incase of a problem ... we are looping through anyway!

                strXML += "<searchResult>";

                strXML += "<" + StudyFormFields.QUESTION_TYPE + ">" + runtimeData.getParameter(intQuestionOrder + "_TYPE") + "</" + StudyFormFields.QUESTION_TYPE + ">";

                strXML += "<" + StudyFormFields.INTERNAL_SURVEY_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID) + "</" + StudyFormFields.INTERNAL_SURVEY_ID + ">";

                strXML += "<" + StudyFormFields.QUESTION_ORDER + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_ORDER + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_ORDER + ">";

                strXML += "<" + StudyFormFields.INTERNAL_QUESTION_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_QUESTION_ID + "_" + intQuestionOrder) + "</" + StudyFormFields.INTERNAL_QUESTION_ID + ">";

                strXML += "<" + StudyFormFields.QUESTION_NUMERIC_MAX + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MAX + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_NUMERIC_MAX + ">";

                strXML += "<" + StudyFormFields.QUESTION_NUMERIC_MIN + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MIN + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_NUMERIC_MIN + ">";

                strXML += "<" + StudyFormFields.QUESTION_DATE_MAX + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MAX + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_DATE_MAX + ">";

                strXML += "<" + StudyFormFields.QUESTION_DATE_MIN + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MIN + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_DATE_MIN + ">";

                strXML += "<" + StudyFormFields.QUESTION_NUMBER + ">" + runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION_NUMBER + ">";

                strXML += "<" + StudyFormFields.QUESTION + ">" + runtimeData.getParameter(StudyFormFields.QUESTION + "_" + intQuestionOrder) + "</" + StudyFormFields.QUESTION + ">";

                strXML += "<survey_result><survey_data>" + runtimeData.getParameter(intQuestionOrder) + "</survey_data>";

                strXML += "<" + StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp>" + runtimeData.getParameter(intQuestionOrder + "_" + StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp") + "</" + StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp>";

                strXML += "</survey_result>";

                // Add the option values for the drop down - not all questions have drop drowns!

                if(runtimeData.getParameter(intQuestionOrder + "_OPTIONS") != null)

                {

                    strXML +=  Utilities.cleanForXML(runtimeData.getParameter(intQuestionOrder + "_OPTIONS"));

                }

                // Make sure it is a data type question and not a page break or title

                if(runtimeData.getParameter("blDataQuestion_" + intQuestionOrder) != null && runtimeData.getParameter("blDataQuestion_" + intQuestionOrder).equals("true")) // We have a question requiring data capture

                {

                    // First check the type of data it ok

                    // Check Numeric

                    if(runtimeData.getParameter(intQuestionOrder + "_TYPE").equals(QUESTION_TYPE_NUMERIC))

                    {

                        // Make sure their is some data

                        if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                        {

                            // Make sure it is of type float

                            if(Utilities.validateFloatValue(runtimeData.getParameter(intQuestionOrder)) == false)

                            {

                                blDataTypeOK = false;

                                blSurveyDataValid = false;

                                vecErrorInvalidDataFields.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                            }

                            // Now check that the data is in the specified range

                            else

                            {

                                

                                // Under max allowable?

                                if(runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MAX + "_" + intQuestionOrder) != null && runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MAX + "_" + intQuestionOrder).length() > 0)

                                {

                                    if(new Float(runtimeData.getParameter(intQuestionOrder)).floatValue() > new Float(runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MAX + "_" + intQuestionOrder)).floatValue())

                                    {

                                        blDataInRange = false;

                                        blSurveyDataValid = false;

                                        vecErrorInvalidDataRange.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                                    }

                                }

                                // Greater than min allowable?

                                if(runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MIN + "_" + intQuestionOrder) != null && runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MIN + "_" + intQuestionOrder).length() > 0)

                                {

                                    if(new Float(runtimeData.getParameter(intQuestionOrder)).floatValue() < new Float(runtimeData.getParameter(StudyFormFields.QUESTION_NUMERIC_MIN + "_" + intQuestionOrder)).floatValue())

                                    {

                                        blDataInRange = false;

                                        blSurveyDataValid = false;

                                        vecErrorInvalidDataRange.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                                    }

                                }

                            }  

                          }

                    }

                    // Validate the dates

                    else if(runtimeData.getParameter(intQuestionOrder + "_TYPE").equals(QUESTION_TYPE_DATE))

                    {

                  //      System.err.println("STEP 1");

                        // Make sure their is some data

                        if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                        {

                            // Make sure it is of type float

                            if(Utilities.validateDateValue(runtimeData.getParameter(intQuestionOrder)) == false)

                            {

                                blDataTypeOK = false;

                                blSurveyDataValid = false;

                                vecErrorInvalidDataFields.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                            }

                            // Now check that the data is in the specified range

                            else

                            {

                                Calendar calSupplied = Calendar.getInstance();

                                Calendar calDateSet = Calendar.getInstance();

                                // Under max allowable?



                                if(runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MAX + "_" + intQuestionOrder) != null && runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MAX + "_" + intQuestionOrder).length()>0)

                                {

                                    // Get the time supplied

                                    calSupplied.setTime(Utilities.convertStringToDate(runtimeData.getParameter(intQuestionOrder), "dd/MM/yyyy"));

                                    // Get the time max range set

                                    calDateSet.setTime(Utilities.convertStringToDate(runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MAX + "_" + intQuestionOrder), "dd/MM/yyyy"));

                                

                                    if(calSupplied.after(calDateSet))

                                    {

                                        blDataInRange = false;

                                        blSurveyDataValid = false;

                                        vecErrorInvalidDataRange.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                                    }

                                }

                                // Greater than min allowable?



                                if(runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MIN + "_" + intQuestionOrder) != null && runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MIN + "_" + intQuestionOrder).length()>0)

                                {

                                    // Get the time supplied

                                    calSupplied.setTime(Utilities.convertStringToDate(runtimeData.getParameter(intQuestionOrder), "dd/MM/yyyy"));

                                    // Get the time max range set

                                    calDateSet.setTime(Utilities.convertStringToDate(runtimeData.getParameter(StudyFormFields.QUESTION_DATE_MIN + "_" + intQuestionOrder), "dd/MM/yyyy"));

                                    if(calSupplied.before(calDateSet))

                                    {

                                        blDataInRange = false;

                                        blSurveyDataValid = false;

                                        vecErrorInvalidDataRange.add(runtimeData.getParameter(StudyFormFields.QUESTION_NUMBER + "_" + intQuestionOrder));

                                    }

                                }

                                // Clean up

                                calSupplied = null;

                                calDateSet = null;

                            }

                        }                           

                    }

                 

                }

                strXML += "</searchResult>";

        }

        // -------------------------- CHECK THE DATA IS OK! ------------------------------------------------

          // If a data field is not filled in fill correctly the error tag and return them to the add form

                if(blDataTypeOK == false)

                {

                    strXML += "<strErrorInvalidDataFields>Please enter the correct data type for question number/s  "; 

                    for(int intCounter = 0; intCounter < vecErrorInvalidDataFields.size(); intCounter ++)

                    {

                        strXML += vecErrorInvalidDataFields.get(intCounter).toString();

                        

                        if (vecErrorInvalidDataFields.size() > 1 && (intCounter < (vecErrorInvalidDataFields.size() - 1)))

                        {

                            strXML += ", ";

                        }

                    }

                        strXML += "</strErrorInvalidDataFields>";

                }

                else if(blDataInRange == false)

                {

                 strXML += "<strErrorInvalidDataRange>Please enter the correct data range for question number/s   "; 

                    for(int intCounter=0; intCounter < vecErrorInvalidDataRange.size(); intCounter ++)

                    {

                        strXML += vecErrorInvalidDataRange.get(intCounter).toString();

                        if (vecErrorInvalidDataRange.size() > 1 && (intCounter < (vecErrorInvalidDataRange.size() - 1)))

                        {

                            strXML += ", ";

                        }

                    }

                        strXML += "</strErrorInvalidDataRange>";   

                }

                    

        // Reset the common variables for the page only if there was a problem

        if(blSurveyDataValid == false)

        {

            intNextQuestionOrder = new Integer(runtimeData.getParameter("intNextQuestionOrder")).intValue();

            intFirstQuestionOrder = new Integer(runtimeData.getParameter("intFirstQuestionOrder")).intValue();

            intStudyID = new Integer(runtimeData.getParameter("intStudyID")).intValue();

            intSurveyID =  new Integer(runtimeData.getParameter("intSurveyID")).intValue();

            intPatientID =  new Integer(runtimeData.getParameter("intInternalPatientID")).intValue();

            strXML += "<blNextButton>" + runtimeData.getParameter("blNextButton") + "</blNextButton>";

            strXML += "<blBackButton>" + runtimeData.getParameter("blBackButton") + "</blBackButton>";

        }

    }

    // This method builds the script answer based on a script provided and a set of results provided

    private String runQuestionScript(String strScript, ResultSet rsQuestionResults) throws BaseChannelException

    {

      // This is what gets return

        String strReturn = "";   

        try

        {

            // Get the scripting object

            Interpreter my_scripting_object = new Interpreter(); 

            int intRowNumber = rsQuestionResults.getRow(); // we need this to return the result back to were it was up too in processing

            // Reset the resultSet

            rsQuestionResults.beforeFirst();

            // Loop through

            while(rsQuestionResults.next())

            {

                //Add the question answers to the script object (get the answer depending on the type)

                if(rsQuestionResults.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_TEXTBOX)

                {

                    if(rsQuestionResults.getString(SURVEY_STORE_DATATEXT) != null)

                    {

                        my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  rsQuestionResults.getString(SURVEY_STORE_DATATEXT));

                    }else{my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  "");}

                }

                else if(rsQuestionResults.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DROPDOWN)

                {

                    if(rsQuestionResults.getString(SURVEY_STORE_DATACHOICES) != null)

                    {

                         my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  rsQuestionResults.getString(SURVEY_STORE_DATACHOICES));

                    }else{my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID), "");}

                }

                else if(rsQuestionResults.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_NUMERIC)

                {       

                    if(rsQuestionResults.getString(SURVEY_STORE_DATANUMERIC) != null)

                    {

                        my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  rsQuestionResults.getInt(SURVEY_STORE_DATANUMERIC));

                    }else{my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  0);}

                 }

                 else if(rsQuestionResults.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DATE)

                 {

                     if(rsQuestionResults.getString(SURVEY_STORE_DATADATE) != null)

                     {

                         my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  rsQuestionResults.getDate(SURVEY_STORE_DATADATE));

                     }else{my_scripting_object.set("QID" + rsQuestionResults.getString(StudyFormFields.INTERNAL_QUESTION_ID),  null);}

                 }

                

            }

            // Return the resultset to the same position as we got it!

     //       rsQuestionResults.absolute(intRowNumber);

            // Catch any errors when running the script

            try

            {

                // Run the script

                strReturn = my_scripting_object.eval(strScript).toString();

            }

            catch (ParseException pe) {

                strReturn = "Syntax error in script";

            }

            // "NA" is return if the script does not have all the data it needs or there is a programming error using the data

            catch (EvalError pe) {

                strReturn = "NA";

            }

            catch(Exception e)

            {

                strReturn = "NA";

                System.err.println("Unknown error while trying to run question script - " + e.toString());

                LogService.instance().log(LogService.ERROR, "SurveyBaseChannelException - Unknown error while trying to run question script - " +  e.toString());

            }

            if(strReturn == null){strReturn = "";} // If the return variable is null return no data.

              return strReturn;

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to run question script - " + e.toString());

            LogService.instance().log(LogService.ERROR, "SurveyBaseChannelException - Unknown error while trying to run question script - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to run question script - " + e.toString());

        }

          

    }
    
    /** This method saves the answers a set

     * of questions for a survey

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData

     * is provided for a question

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void saveSurveyDataForBiospecimen() throws BaseChannelInvalidRuntimeData, BaseChannelException

    { 

       try

       {

            // Get the Next question order for the set of questions submitted

            int intHighestQuestionOrder = new Integer(runtimeData.getParameter("intNextQuestionOrder")).intValue();

            

            int intFirstQuestionOrderTemp = new Integer(runtimeData.getParameter("intFirstQuestionOrder")).intValue();

            // Get the study, suryve and patient id and biospecimen id

            String intStudyIDTemp = runtimeData.getParameter(INTERNAL_STUDY_ID);

            String intSurveyIDTemp = runtimeData.getParameter(INTERNAL_SURVEY_ID);

            String intInternalPatientIDTemp = runtimeData.getParameter("intBiospecimenID");

            String intBiospecimenIDTemp = runtimeData.getParameter("intBiospecimenID");
            
            System.err.println(intBiospecimenIDTemp);

            // This query gets the results already added for a participant for a particular survey

            Query my_select_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_select_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);

            my_select_query.setField(INTERNAL_SURVEY_RESULTS_ID, "");

            my_select_query.setField(INTERNAL_SURVEY_ID, "");

            my_select_query.setField(INTERNAL_STUDY_ID, "");

            my_select_query.setField(INTERNAL_PARTICIPANT_ID, "");
            
            //my_select_query.setField("intBiospecimenID", "");

            my_select_query.setField(QUESTION_ORDER, "");

            my_select_query.setWhere("", INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_STUDY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intStudyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_PARTICIPANT_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intInternalPatientIDTemp);
            
            //my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, "intBiospecimenID", neuragenix.dao.DBSchema.EQUALS_OPERATOR, intBiospecimenIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.LESSTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder"));

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.GREATERTHAN_AND_EQUAL_OPERATOR, runtimeData.getParameter("intFirstQuestionOrder"));

            my_select_query.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_DESCENDING);

            blSearchRecordOK = my_select_query.execute();

            ResultSet my_resultset= my_select_query.getResults();     

            

            // This is the query used in case we need to do a insert

            Query my_insert_query = new Query(Query.INSERT_QUERY, strActivityRequested, authToken);

            my_insert_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);

            // This is the qurey used if we need to update existing data

            Query my_update_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            my_update_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);

            

            String intQuestionOrder = "";

            String intParticipantIDTemp = "";

            String intSurveyResultsIDTemp ="";

            

            boolean blExecute = false;

            boolean blUpdate = false;

            boolean blAddRecord = true;

            boolean blAddAllRecords = false;

            boolean blUpdateRecordOK = true;

            boolean blUpdateAllRecords = false;

            // Keep looping until you go from the highest order no to the first order number for the page

            for(int intCounter = intHighestQuestionOrder; intCounter > (intFirstQuestionOrderTemp - 1); intCounter --)

            {

                intQuestionOrder = new Integer(intCounter).toString();

                if(runtimeData.getParameter("blDataQuestion_" + intQuestionOrder) != null && runtimeData.getParameter("blDataQuestion_" + intQuestionOrder).equals("true")) // We have a question requiring data capture

                {

                 // TAKEN OUT 050302   

                    my_resultset.beforeFirst();

                    blExecute = false; // Flag indicateing whether we should execute

                    blUpdate = false; // Flag indicateing whether we should run the update query

                                            

                    while(my_resultset.next()) // See if a record already exists ... if it does update don't insert

                    {

                        if(my_resultset.getString(INTERNAL_SURVEY_ID).equals(intSurveyIDTemp) && my_resultset.getString(QUESTION_ORDER).equals(intQuestionOrder) && my_resultset.getString(INTERNAL_STUDY_ID).equals(intStudyIDTemp))

                        {

                            blUpdate = true;

                            intSurveyResultsIDTemp = my_resultset.getString(INTERNAL_SURVEY_RESULTS_ID);

                // TAKEN OUT 050302            

                            my_resultset.last();

                       //     break; // ADDED 050302  

                        }

                     }

                     if(blUpdate == true) // if we have to update, update!

                     {

                    

                        my_update_query.clearFields();

                        my_update_query.setTimestamp(runtimeData.getParameter(intQuestionOrder + "_" + StudyFormFields.SURVEY_RESULTS_DOMAIN + "_Timestamp"));

                        my_update_query.setField(INTERNAL_SURVEY_ID, intSurveyIDTemp);

                        

                        my_update_query.setField(INTERNAL_STUDY_ID, intStudyIDTemp);

                        

                        my_update_query.setField(INTERNAL_PARTICIPANT_ID, intInternalPatientIDTemp);

                        
                        
                       // my_update_query.setField("intBiospecimenID", intBiospecimenIDTemp);

                       

                        my_update_query.setField(QUESTION_ORDER, intQuestionOrder);

                        my_update_query.setField(INTERNAL_QUESTION_ID, runtimeData.getParameter("intQuestionID_" + intQuestionOrder));

                        

                        my_update_query.clearWhere();

                        my_update_query.setWhere("", INTERNAL_SURVEY_RESULTS_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyResultsIDTemp);

                 

                        // Depending on the type store it in the correct column

                        if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_TEXTBOX))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_TEXTBOX);

                            my_update_query.setField(SURVEY_STORE_DATATEXT, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_DROPDOWN);

                            my_update_query.setField(SURVEY_STORE_DATACHOICES, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_NUMERIC))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_NUMERIC);

                            my_update_query.setField(SURVEY_STORE_DATANUMERIC, runtimeData.getParameter(intQuestionOrder));

                            

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DATE))

                        {

                            blExecute = true;

                            my_update_query.setField(QUESTION_TYPE, QUESTION_TYPE_DATE);

                            my_update_query.setField(SURVEY_STORE_DATADATE, runtimeData.getParameter(intQuestionOrder));

                            

                        }

                       // execute??? - we don't execute for page break, comments, titles or scripts

                        if(blExecute = true)

                        {

                            if(blUpdateRecordOK == true) // Only attempt to add if we haven't have a problem so far

                            {

                                blUpdateRecordOK = my_update_query.execute();

                            }

                            blUpdateAllRecords = true;

                        }

                    }

                    else // else insert

                    {

                        my_insert_query.clearFields();

                        my_insert_query.setField(INTERNAL_SURVEY_ID, intSurveyIDTemp);

                        my_insert_query.setField(INTERNAL_STUDY_ID, intStudyIDTemp);

                        my_insert_query.setField(INTERNAL_PARTICIPANT_ID, intInternalPatientIDTemp);
                        
                        //my_update_query.setField("intBiospecimenID", intBiospecimenIDTemp);

                        my_insert_query.setField(QUESTION_ORDER, intQuestionOrder);

                        my_insert_query.setField(INTERNAL_QUESTION_ID, runtimeData.getParameter("intQuestionID_" + intQuestionOrder));

                      // Depending on the type store it in the correct column

                         if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_TEXTBOX))

                        {

                            blExecute = true;

                            my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_TEXTBOX);

                            my_insert_query.setField(SURVEY_STORE_DATATEXT, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blExecute = true;

                            my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_DROPDOWN);

                            my_insert_query.setField(SURVEY_STORE_DATACHOICES, runtimeData.getParameter(intQuestionOrder));

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_NUMERIC))

                        {

                            if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                            {

                                blExecute = true;

                                my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_NUMERIC);

                                my_insert_query.setField(SURVEY_STORE_DATANUMERIC, runtimeData.getParameter(intQuestionOrder));

                            }

                        }

                        else if(runtimeData.getParameter(intQuestionOrder + "_" + "TYPE") != null && runtimeData.getParameter(intQuestionOrder + "_" + "TYPE").equals(QUESTION_TYPE_DATE))

                        {

                            if(runtimeData.getParameter(intQuestionOrder).length() > 0)

                            {

                                blExecute = true;

                                my_insert_query.setField(QUESTION_TYPE, QUESTION_TYPE_DATE);

                                my_insert_query.setField(SURVEY_STORE_DATADATE, runtimeData.getParameter(intQuestionOrder));

                            }

                        }



                    // Execute?? - don't insert for title, comments, page break, script

                        if(blExecute == true)

                        {

                            if(blAddRecord == true) // Only attempt to add if we haven't have a problem so far

                            {

                                blAddRecord = my_insert_query.execute();

                            }

                            blAddAllRecords = true;

                        }

                }

            }

        }

            // Commit final updates and inserts

                if(blAddAllRecords == true)

                {

                     if(blAddRecord == true)

                     {

                               my_insert_query.saveChanges(); // if ok save the changes        

                     }

                     else{my_insert_query.cancelChanges();} // if there was a problem cancel changes

                }

                if(blUpdateAllRecords == true)

                {

                            if(blUpdateRecordOK == true)

                            {

                                my_update_query.saveChanges(); // if ok save the changes

                            }

                            else{my_update_query.cancelChanges();} // if there was a problem cancel changes

                }

                

            // Clean up

             my_select_query.killResultSet(my_resultset);

             my_insert_query = null;

             my_update_query = null;

             my_select_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString());

        }

    }

    
    public void buildBioSurveySearchResultsXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = ""; // Used to store the temporary display name

        boolean blFirstTimeAround = true; // This flag to indicate if its the first time around the resultset loop

        boolean blGetOptions = false; // This flag is to indicate whether we need to get the options for a question

        String intQuestionIDToGet = "0"; // Tell the options query which id to get

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

   

        try 

            {

            // Get the essential values for creating the questions

            String intStudyIDTemp = runtimeData.getParameter(INTERNAL_STUDY_ID);

            String intSurveyIDTemp = runtimeData.getParameter(INTERNAL_SURVEY_ID);

            String intInternalPatientIDTemp = runtimeData.getParameter("intBiospecimenID");

            

            // This is the results query so we can populate the questions with answers already given

            Query my_select_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_select_query.setDomainName(BIO_SURVEY_RESULTS_DOMAIN);

            my_select_query.setField(INTERNAL_SURVEY_RESULTS_ID, "");

            my_select_query.setField(INTERNAL_SURVEY_ID, "");

            my_select_query.setField(INTERNAL_STUDY_ID, "");

            my_select_query.setField(INTERNAL_PARTICIPANT_ID, "");

            my_select_query.setField(INTERNAL_QUESTION_ID, "");

            my_select_query.setField(QUESTION_TYPE, "");

            my_select_query.setField(SURVEY_STORE_DATATEXT, "");

            my_select_query.setField(SURVEY_STORE_DATACHOICES, "");

            my_select_query.setField(SURVEY_STORE_DATANUMERIC, "");

            my_select_query.setField(SURVEY_STORE_DATADATE, "");

            my_select_query.setField(QUESTION_ORDER, "");

            my_select_query.setWhere("", INTERNAL_SURVEY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intSurveyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_STUDY_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intStudyIDTemp);

            my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, INTERNAL_PARTICIPANT_ID, neuragenix.dao.DBSchema.EQUALS_OPERATOR, intInternalPatientIDTemp);

            //my_select_query.setOrderBy(Study FormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);

            my_select_query.setOrderBy(QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
           // COULD GAIN SOME EFFICIENCES HERE  ---- BUT we can't because we need previous data for the scripting section

            // my_select_query.setWhere(neuragenix.dao.DBSchema.AND_CONNECTOR, QUESTION_ORDER, neuragenix.dao.DBSchema.GREATERTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder"));

            

            blSearchRecordOK = my_select_query.execute();

            ResultSet my_results_resultset= my_select_query.getResults();     

            

            // This part gets options query ready in case we need it

            Query my_options_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_options_query.setDomainName(StudyFormFields.SURVEY_OPTIONS_DOMAIN);

            my_options_query.setField(OPTION_LABEL, "");

            my_options_query.setField(OPTION_VALUE, "");

            my_options_query.setOrderBy(OPTION_ORDER, DBSchema.ORDERBY_ASENDING);

        

            // This is the questions query it only gets the questions greater than the last question order

            Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

            my_query.setDeletedColumn(blDeletedColumn);

            for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

            {

                my_query.setDomainName(vecDomains.get(intCounter).toString());

            }

            while(enumInternalFormFields.hasMoreElements())

            {         

                my_query.setField(enumInternalFormFields.nextElement().toString(), "");

            }

            for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

            {

                my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

            }

            for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

            {

                my_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

            }

            

            blSearchRecordOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults();

                

                

            String strTempInternalFormName; // Stores the internal name of the varible we are dealing with

            String strTempXML = ""; //

            boolean blComplete = false; // This flag indicates whether to stop processing through the question as we have hit a page break or the end of the survey

            

                // If the have hit the back button count back two page breaks and then start the results set

                if(blBackButton == true)

                {                 

                    my_resultset.afterLast();

                    int intPageBreakCounter = 0;

                    // While we haven't countered two page breaks or are at the start of the resultset, 

                    while(intPageBreakCounter != 2 && my_resultset.isBeforeFirst() == false)

                    {

                        my_resultset.previous();

                        if(my_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_PAGEBREAK) // We have a page break!

                        {

                           intPageBreakCounter ++;

                        }

                    }

                }

                // Loop through the survey questions and build the XML file

                while(my_resultset.next())

                {

                    // Get the firstQuestionOrder - Not always the first record in the resultset if they have pressed the back button!

                     if(blFirstTimeAround == true)

                    {

                        intFirstQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER);

                    }

                     

                    blGetOptions = false; // Reset this flag

                    strTempXML = "";

                    

                    // Open the tag

                    strTempXML += "<" + strSearchResultTag + ">"; // Open the tag for this group of variable

                    

                    enumInternalFormFields = hashFormFields.keys();

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        // If it's a page break stop processing   

                        if(strTempInternalFormName.equals(StudyFormFields.QUESTION_TYPE) && my_resultset.getInt(strTempInternalFormName) == StudyFormFields.QUESTION_TYPE_PAGEBREAK)

                        {

                            blComplete = true;

                            // Get the next questionId for the channel

                            intNextQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER);

                            if(my_resultset.isLast()) // If it's the last record there are no more to come

                            {

                                blMoreQuestions = false;   

                            }

                            else

                            {

                                blMoreQuestions = true;

                                my_resultset.last();

                                

                            }

                        }

                        // If it'a a drop down we need the values -- this flag tells us that we need to get options for this question

                        else if(strTempInternalFormName.equals(StudyFormFields.QUESTION_TYPE) && my_resultset.getString(strTempInternalFormName).equals(QUESTION_TYPE_DROPDOWN))

                        {

                            blGetOptions = true;

                            intQuestionIDToGet = my_resultset.getString(INTERNAL_QUESTION_ID);

                        }

                        

                        strTempXML += "<" + strTempInternalFormName + ">"; // Open the tag for this particular variable

                   

                        if(my_resultset.getString(strTempInternalFormName) != null)

                        {

                            // If it's a date convert it to the correct display format 

                            if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                            {

                                strTempXML += Utilities.convertDateForDisplay(my_resultset.getString(strTempInternalFormName));

                            }

                            else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                            {

                                strTempXML += Utilities.convertTimeForDisplay(my_resultset.getString(strTempInternalFormName));

                            }

                            else{

                            

                                  if(hashCutFields != null && hashCutFields.size() > 0)

                                  {

                                          

                                       if(hashCutFields.containsKey(strTempInternalFormName))

                                       {

                                         

                                                // Make sure the length is greater than what you are trying to cut!

                                           if(my_resultset.getString(strTempInternalFormName).length() > ((Integer)hashCutFields.get(strTempInternalFormName)).intValue())

                                           {

                                                strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName).substring(0, ((Integer)hashCutFields.get(strTempInternalFormName)).intValue()) + " ...");

                                            }

                                            else{strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));}

                                       }

                                       else

                                       {

                                               strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                       }

                                    }

                                    else

                                    {

                                             strTempXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                    }

                                                                       

                            }

                        }

                        strTempXML += "</" + strTempInternalFormName + ">"; // Close the tag for this particular variable

                        

                    }

                    // ---------- GET OPTIONS IF NEED FOR THIS QUESTION ----------

   //                 if(blGetOptions == true)

                    if(blGetOptions == true && blComplete == false)

                    {

                        my_options_query.clearWhere();

                        my_options_query.setWhere("", INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, intQuestionIDToGet);

                        blSearchRecordOK = my_options_query.execute();

                        

                        ResultSet my_options_resultset = my_options_query.getResults();

                        

                        while(my_options_resultset.next())

                        {

                            strTempXML += "<options>";

                            strTempXML += "<" + Utilities.cleanForXSL(OPTION_LABEL) + ">" + Utilities.cleanForXSL(my_options_resultset.getString(OPTION_LABEL)) + "</" + Utilities.cleanForXSL(OPTION_LABEL) + ">";

                            strTempXML += "<" + Utilities.cleanForXSL(OPTION_VALUE) + ">" + Utilities.cleanForXSL(my_options_resultset.getString(OPTION_VALUE)) + "</" + Utilities.cleanForXSL(OPTION_VALUE) + ">";

                            strTempXML += "</options>";

                        }

                        

                         my_options_query.killResultSet(my_options_resultset);

                    }

                    // ---------- GET PREVIOUSLY ENTERED RESULTS FOR THIS QUESTION --------

                    strTempXML += "<survey_result>";

                    

                    if(blComplete == false)

                    {

                      // If it's not a script do the normal thing and get any passed results for this question

                      if(my_resultset.getInt(QUESTION_TYPE) != StudyFormFields.QUESTION_TYPE_SCRIPT)

                      {

                        while(my_results_resultset.next())

                        {

                            if(my_results_resultset.getInt(INTERNAL_QUESTION_ID) == my_resultset.getInt(INTERNAL_QUESTION_ID))

                            {

                                strTempXML += "<survey_data>";

                                // Get the data from the correct column depending on type

                                if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_TEXTBOX)

                                {

                                    strTempXML += Utilities.cleanForXSL(my_results_resultset.getString(SURVEY_STORE_DATATEXT));

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DROPDOWN)

                                {

                                    strTempXML += Utilities.cleanForXSL(my_results_resultset.getString(SURVEY_STORE_DATACHOICES));

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_NUMERIC)

                                {

                                    if(my_results_resultset.getString(SURVEY_STORE_DATANUMERIC) != null)

                                    {

                                        strTempXML += my_results_resultset.getString(SURVEY_STORE_DATANUMERIC);

                                    }

                                    else{strTempXML += "";}

                                }

                                else if(my_results_resultset.getInt(QUESTION_TYPE) == StudyFormFields.QUESTION_TYPE_DATE)

                                {

                                    if(my_results_resultset.getString(SURVEY_STORE_DATADATE) != null)

                                    {

                                        strTempXML += Utilities.convertDateForDisplay(my_results_resultset.getString(SURVEY_STORE_DATADATE));

                                    }

                                    else{strTempXML += "";}

                                }

                                strTempXML += "</survey_data>";

                                strTempXML += "<BIO_SURVEY_RESULTS_Timestamp>";

                                strTempXML += my_results_resultset.getString("BIO_SURVEY_RESULTS_Timestamp");

                                strTempXML += "</BIO_SURVEY_RESULTS_Timestamp>";

                             //   break; // ADDED -  5/3/2003 - HIDBA 

                      //   TAKEN OUT 5/3/2003 - HIDBA         

                                my_results_resultset.last(); // Get out of the loop

                            }

                        }

                      }

                        // IT's a script process the code!

                      else

                      {

                             strTempXML += "<survey_data>" + runQuestionScript(my_resultset.getString(StudyFormFields.QUESTION_SCRIPT), my_results_resultset) +  "</survey_data>";

                     

                        

                      }

                       strTempXML += "</survey_result>";

                    }

               // TAKEN OUT 5/3/2003 - HIDBA  

                    my_results_resultset.beforeFirst(); // Reset the resultset for the next time around!

                    

                    strTempXML += "</" + strSearchResultTag + ">"; // Close off the tag for this set of variables

                    intRecordCount ++;

                    if(blComplete == false)

                    {

                        strXML += strTempXML;

                    }

                    // This flag tells us that the resultset has been around once

                    blFirstTimeAround = false;

                }

                // If the next order number has not been set make it the last record in the recordset because it is the final question set

                if(intNextQuestionOrder == 0 && my_resultset != null)

                {

                    my_resultset.last();

                  // Add one for the next question Order

                    intNextQuestionOrder = my_resultset.getInt(StudyFormFields.QUESTION_ORDER) + 1;

                 }

                        

                // Return the resultsets to be killed

                my_query.killResultSet(my_resultset);

                my_select_query.killResultSet(my_results_resultset);

                

                // Clean up

                my_query = null;

                my_select_query = null;

                my_options_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString());

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString());

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString());

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString());

        }

    }
}

