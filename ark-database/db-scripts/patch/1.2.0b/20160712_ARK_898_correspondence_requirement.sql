use `study`;

--Not allow to run twice.
ALTER TABLE `correspondence_outcome_type` 
ADD UNIQUE INDEX `UK_NAME` (`NAME` ASC);

INSERT IGNORE INTO  `correspondence_outcome_type` (`NAME`) VALUES ('Success'); 
INSERT IGNORE INTO `correspondence_outcome_type` (`NAME`) VALUES ('Failure');
INSERT IGNORE INTO `correspondence_outcome_type` (`NAME`) VALUES ('Left voicemail');
INSERT IGNORE INTO `correspondence_outcome_type` (`NAME`) VALUES ('Return call');
INSERT IGNORE INTO `correspondence_outcome_type` (`NAME`) VALUES ('Rebook visit');
 	
DROP TABLE IF EXISTS `correspondence_mode_direction_outcome`;
CREATE TABLE `correspondence_mode_direction_outcome` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `MODE_TYPE_ID` INT(11) NOT NULL,
  `DIRECTION_TYPE_ID` INT(11) NOT NULL,
  `OUTCOME_TYPE_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `FK_MODE_TYPE_ID_idx` (`MODE_TYPE_ID` ASC),
  INDEX `FK_DIRECTION_TYPE_ID_idx` (`DIRECTION_TYPE_ID` ASC),
  INDEX `FK_OUTCOME_TYPE_ID_idx` (`OUTCOME_TYPE_ID` ASC),
  CONSTRAINT `FK_MODE_TYPE_ID`
    FOREIGN KEY (`MODE_TYPE_ID`)
    REFERENCES `correspondence_mode_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DIRECTION_TYPE_ID`
    FOREIGN KEY (`DIRECTION_TYPE_ID`)
    REFERENCES `correspondence_direction_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_OUTCOME_TYPE_ID`
    FOREIGN KEY (`OUTCOME_TYPE_ID`)
    REFERENCES `correspondence_outcome_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


/*
This part will populate the above table which only shows the relevant out come type for the MODE and DIRECTION
*/

SET @MailID=(SELECT ID FROM correspondence_mode_type WHERE name='Mail');
SET @FaxID=(SELECT ID FROM correspondence_mode_type WHERE name='Fax');
SET @EMailID=(SELECT ID FROM correspondence_mode_type WHERE name='Email');
SET @TelephoneID=(SELECT ID FROM correspondence_mode_type WHERE name='Telephone');
SET @FaceToFaceID=(SELECT ID FROM correspondence_mode_type WHERE name='Face to face');
SET @NotApplicableID=(SELECT ID FROM correspondence_mode_type WHERE name='Not applicable');

SET @OUTGOID=(SELECT ID FROM correspondence_direction_type WHERE name='Outgoing');
SET @INCOMID=(SELECT ID FROM correspondence_direction_type WHERE name='Incoming');
SET @Sent=(SELECT ID FROM correspondence_outcome_type WHERE name='Sent');
SET @Received=(SELECT ID FROM correspondence_outcome_type WHERE name='Received');
SET @RetuntToSender=(SELECT ID FROM correspondence_outcome_type WHERE name='Return to sender');
SET @NotApplicable=(SELECT ID FROM correspondence_outcome_type WHERE name='Not applicable');
SET @NoAnswer=(SELECT ID FROM correspondence_outcome_type WHERE name='No answer');
SET @Engaged=(SELECT ID FROM correspondence_outcome_type WHERE name='Engaged');
SET @ContactMode=(SELECT ID FROM correspondence_outcome_type WHERE name='Contact made');
SET @MessageGiven=(SELECT ID FROM correspondence_outcome_type WHERE name='Message given to person');
SET @Success=(SELECT ID FROM correspondence_outcome_type WHERE name='Success');
SET @Faliure=(SELECT ID FROM correspondence_outcome_type WHERE name='Failure');
-- Ark 1531 additional out come types.
SET @LeftVoicemail=(SELECT ID FROM correspondence_outcome_type WHERE name='Left voicemail');
SET @ReturnCall=(SELECT ID FROM correspondence_outcome_type WHERE name='Return call');
SET @RebookVisit=(SELECT ID FROM correspondence_outcome_type WHERE name='Rebook visit');

DELETE FROM correspondence_mode_direction_outcome;

INSERT INTO correspondence_mode_direction_outcome(MODE_TYPE_ID,DIRECTION_TYPE_ID,OUTCOME_TYPE_ID) 
-- Mail, Outgoing
VALUES(@MailID,@OUTGOID,@Sent),
(@MailID,@OUTGOID,@RetuntToSender),
(@MailID,@OUTGOID,@NotApplicable),
-- Mail, Incoming
(@MailID,@INCOMID,@Received),
(@MailID,@INCOMID,@RetuntToSender),
(@MailID,@INCOMID,@NotApplicable),
-- Fax, Outgoing
(@FaxID,@OUTGOID,@Sent),
(@FaxID,@OUTGOID,@Engaged),
(@FaxID,@OUTGOID,@NoAnswer),
(@FaxID,@OUTGOID,@NotApplicable),
-- Fax, Incoming
(@FaxID,@INCOMID,@Received),
(@FaxID,@INCOMID,@NotApplicable),
-- Email, Outgoing
(@EMailID,@OUTGOID,@Sent),
(@EMailID,@OUTGOID,@RetuntToSender),
(@EMailID,@OUTGOID,@NotApplicable),
-- Email, Incoming
(@EMailID,@INCOMID,@Received),
(@EMailID,@INCOMID,@NotApplicable),
-- Telephone, Outgoing
(@TelephoneID,@OUTGOID,@ContactMode),
(@TelephoneID,@OUTGOID,@Engaged),
(@TelephoneID,@OUTGOID,@MessageGiven),
(@TelephoneID,@OUTGOID,@NoAnswer),
(@TelephoneID,@OUTGOID,@NotApplicable),
-- Ark 1531 additional out come types.
(@TelephoneID,@OUTGOID,@LeftVoicemail),
(@TelephoneID,@OUTGOID,@ReturnCall),
(@TelephoneID,@OUTGOID,@RebookVisit),
-- Telephone, Incoming
(@TelephoneID,@INCOMID,@ContactMode),
(@TelephoneID,@INCOMID,@NoAnswer),
(@TelephoneID,@INCOMID,@MessageGiven),
(@TelephoneID,@INCOMID,@NotApplicable),
-- Face to face, Outgoing
(@FaceToFaceID,@OUTGOID,@ContactMode),
(@FaceToFaceID,@OUTGOID,@NotApplicable),
-- Face to face, Incoming
(@FaceToFaceID,@INCOMID,@ContactMode),
(@FaceToFaceID,@INCOMID,@NotApplicable),
-- Not applicable, Outgoing
(@NotApplicableID,@OUTGOID,@Success),
(@NotApplicableID,@OUTGOID,@Faliure),
-- Not applicable, Incoming
(@NotApplicableID,@INCOMID,@Success),
(@NotApplicableID,@INCOMID,@Faliure);











