use `admin`;
ALTER TABLE `work_request` 
ADD UNIQUE INDEX `UK_work_request_status_and_study_id` (`NAME` ASC, `STUDY_ID` ASC);

