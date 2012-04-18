package au.org.theark.study.web.component.subjectUpload;

import org.springframework.core.task.TaskExecutor;

public class AsynchSubjectUploadProcessor {

  private class MessagePrinterTask implements Runnable {

    private String message;

    public MessagePrinterTask(String message) {
      this.message = message;
    }

    public void run() {
      System.out.println(message);
    }

  }

  private TaskExecutor taskExecutor;

  public AsynchSubjectUploadProcessor(TaskExecutor taskExecutor) {
    this.taskExecutor = taskExecutor;
  }

  public void printMessages() {
    for(int i = 0; i < 25; i++) {
      taskExecutor.execute(new MessagePrinterTask("Message" + i));
    }
  }
}


/*package au.org.theark.study.web.component.subjectUpload;

import org.springframework.core.task.TaskExecutor;

public class AsynchSubjectUploadProcessor implements Runnable {
	 private TaskExecutor taskExecutor;
    private UploadUtilities uploadUtils;

    public AsynchSubjectUploadProcessor(TaskExecutor taskExecutor, UploadUtilities uploadUtils) {
         this.taskExecutor = taskExecutor;
         this.uploadUtils = uploadUtils;
    }
/ *
    public void fire(final Integer uploadIdToProcess) {
         taskExecutor.execute( new Runnable() {
              public void run() {
                   uploadUtils.processSubjectUpload( uploadIdToProcess );
              }
         });
    }
* /
	public void run() {
		// TODO Auto-generated method stub

      uploadUtils.processSubjectUpload( 53 );
	}

}
*/