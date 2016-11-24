package au.org.theark.test.util.externalresource;

import au.org.theark.test.util.Reference;
import org.bytedeco.javacv.*;
import org.junit.rules.ExternalResource;

public class ScreenRecordingExternalResource extends ExternalResource {

    FFmpegFrameGrabber grabber;
    FFmpegFrameRecorder recorder;
    int x = 0, y = 0, w = 1920, h = 1080;
    boolean isRecording = true;

    @Override
    protected void before() throws Throwable {
        grabber = new FFmpegFrameGrabber(":1.0+" + x + "," + y);
        grabber.setFormat("x11grab");
        grabber.setImageHeight(h);
        grabber.setImageWidth(w);

        recorder = FFmpegFrameRecorder.createDefault("/output/" + Reference.currentTestName + ".mp4", grabber.getImageWidth(), grabber.getImageHeight());
        recorder.start();
        grabber.start();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Frame grabbedImage = grabber.grab();
                    while ((grabbedImage = grabber.grab()) != null && isRecording) {
                        recorder.record(grabbedImage);
                    }
                    if (!isRecording) {
                        grabber.stop();
                        recorder.stop();
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    @Override
    protected void after() {
        isRecording = false;
    }
}

