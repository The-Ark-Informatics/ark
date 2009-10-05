/*
 * Study.java
 *
 * Created on 23 September 2005, 11:28
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.bio.study;

import java.util.Date;

/**
 *
 * @author dmurley
 */
public class Study
{
   private int studyKey = -1;
   private String studyName = "";
   private String studyCode = "";
   private String studyOwner = "";
   private Date startDate;
   private Date endDate;
   private int targetPatientNumber = 0;
   private String studyDescription = "";
           
   
   
   /** Creates a new instance of Study */
   public Study()
   {
   }
   
   public Study (int studyKey, String studyName, String studyCode, String studyOwner, Date startDate,
           Date endDate, int targetPatientNumber, String studyDescription)
   {
      this.setStudyKey(studyKey);
      this.setStudyName(studyName);
      this.setStudyCode(studyCode);
      this.setStudyOwner(studyOwner);
      this.setStartDate(startDate);
      this.setEndDate(endDate);
      this.setTargetPatientNumber(targetPatientNumber);
      this.setStudyDescription(studyDescription);
   }

   public int getStudyKey()
   {
      return studyKey;
   }

   public void setStudyKey(int studyKey)
   {
      this.studyKey = studyKey;
   }

   public String getStudyName()
   {
      return studyName;
   }

   public void setStudyName(String studyName)
   {
      this.studyName = studyName;
   }

   public String getStudyCode()
   {
      return studyCode;
   }

   public void setStudyCode(String studyCode)
   {
      this.studyCode = studyCode;
   }

   public String getStudyOwner()
   {
      return studyOwner;
   }

   public void setStudyOwner(String studyOwner)
   {
      this.studyOwner = studyOwner;
   }

   public Date getStartDate()
   {
      return startDate;
   }

   public void setStartDate(Date startDate)
   {
      this.startDate = startDate;
   }

   public Date getEndDate()
   {
      return endDate;
   }

   public void setEndDate(Date endDate)
   {
      this.endDate = endDate;
   }

   public int getTargetPatientNumber()
   {
      return targetPatientNumber;
   }

   public void setTargetPatientNumber(int targetPatientNumber)
   {
      this.targetPatientNumber = targetPatientNumber;
   }

   public String getStudyDescription()
   {
      return studyDescription;
   }

   public void setStudyDescription(String studyDescription)
   {
      this.studyDescription = studyDescription;
   }
}
