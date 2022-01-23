package com.bravenatorsrobotics.freightfrenzy.subsystem.vision;

import com.bravenatorsrobotics.common.core.BravenatorRuntimeException;
import com.bravenatorsrobotics.common.operation.OperationMode;
import com.bravenatorsrobotics.freightfrenzy.subsystem.AbstractController;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class VisionController extends AbstractController {

    public static final OpenCvCameraRotation CAMERA_ROTATION = OpenCvCameraRotation.UPRIGHT;
    public static final int CAMERA_RES_WIDTH    = 1280  ;
    public static final int CAMERA_RES_HEIGHT   = 720   ;

    private final OpenCvWebcam webcam;

    public VisionController(OperationMode<?> operationMode) {
        super(operationMode);

        // If no camera monitor view ID is desired then use different createWebcam method signature
        int cameraMonitorViewID = operationMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", operationMode.hardwareMap.appContext.getPackageName());
        this.webcam = OpenCvCameraFactory.getInstance().createWebcam(operationMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewID);

        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for requesting camera permission
    }

    public void OpenCameraToPipeline(OpenCvPipeline pipeline) {
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                webcam.startStreaming(CAMERA_RES_WIDTH, CAMERA_RES_HEIGHT, CAMERA_ROTATION);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
                throw new BravenatorRuntimeException("OpenCvCamera Error Code: " + errorCode);
            }
        });
    }

    public void CloseCamera() {
        webcam.stopStreaming();
    }

}
