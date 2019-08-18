/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.report.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

import au.org.theark.core.service.IArkCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DataExtractionUploadJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DataExtractionUploadJob.class);

    static final String IARKCOMMONSERVICE = "iArkCommonService";
    static final String SEARCH_ID = "uploadId";
    static final String CURRENT_USER = "currentUser";

    /**
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that
     * is associated with the Job.
     */
    public void execute(JobExecutionContext context) {
        try {
            JobDataMap data = context.getJobDetail().getJobDataMap();
            IArkCommonService iArkCommonService = (IArkCommonService) data.get(IARKCOMMONSERVICE);
            Long searchId = (Long) data.get(SEARCH_ID);
            String currentUser = data.getString(CURRENT_USER);
            iArkCommonService.runSearch(searchId, currentUser);
        } catch (Exception e) {
            log.error("Data extraction failed with exception: ", e);
        }
    }
}
