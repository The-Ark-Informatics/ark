package au.org.theark.test.util.externalresource;

import au.org.theark.test.util.Reference;
import org.bytedeco.javacv.*;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ScreenRecordingExternalResource extends ExternalResource {

    private transient static Logger log = LoggerFactory.getLogger(ScreenRecordingExternalResource.class);

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

        try {
            File recording = new File("/tmp/output/" + Reference.currentTestName + ".mp4");
            recording.getParentFile().mkdirs();
            recorder = FFmpegFrameRecorder.createDefault(recording, grabber.getImageWidth(), grabber.getImageHeight());
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
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            log.error("Unable to start screen recording. Is the virtual framebuffer running?");
        }
    }

    @Override
    protected void after() {
        isRecording = false;
    }
}

