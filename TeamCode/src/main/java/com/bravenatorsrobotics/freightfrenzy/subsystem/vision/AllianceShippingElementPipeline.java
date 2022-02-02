package com.bravenatorsrobotics.freightfrenzy.subsystem.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class AllianceShippingElementPipeline extends OpenCvPipeline {

    private final Telemetry telemetry;
    private final Mat mat = new Mat();

    public static final Scalar AOI_COLOR = new Scalar(255, 0, 0); // RED

    public enum AllianceShippingElementLocation {
        LEFT, RIGHT, CENTER, UNIDENTIFIED
    }

    public static final Scalar LOW_HSV = new Scalar(155, 155, 84);
    public static final Scalar HIGH_HSV = new Scalar(190, 255, 255);
    public static final double PERCENT_COLOR_THRESHOLD = 0.15; // 40%

    public static final Rect LEFT_ROI = new Rect(
            new Point(0, 0),
            new Point((1280.0f / 2.0f)  - 100, 720)
    );

    public static final Rect RIGHT_ROI = new Rect(
            new Point((1280.0f / 2.0f) + 100, 0),
            new Point(1280, 720)
    );

    private AllianceShippingElementLocation shippingElementLocation = AllianceShippingElementLocation.UNIDENTIFIED;

    public AllianceShippingElementPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        Core.inRange(mat, LOW_HSV, HIGH_HSV, mat);

        Mat left = mat.submat(LEFT_ROI);
        Mat right = mat.submat(RIGHT_ROI);

        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;

        left.release();
        right.release();

        telemetry.addData("Left Raw Value", (int) Core.sumElems(left).val[0]);
        telemetry.addData("Right Raw Value", (int) Core.sumElems(right).val[0]);
        telemetry.addData("Left Percentage", Math.round(leftValue * 100) + "%");
        telemetry.addData("Right Percentage", Math.round(rightValue * 100) + "%");

        boolean isLeftPosition = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean isRightPosition = rightValue > PERCENT_COLOR_THRESHOLD;

        if(isLeftPosition) {
            shippingElementLocation = AllianceShippingElementLocation.CENTER;
            telemetry.addData("Shipping Element Location", "Center");
        } else if(isRightPosition) {
            shippingElementLocation = AllianceShippingElementLocation.RIGHT;
            telemetry.addData("Shipping Element Location", "Right");
        } else {
            shippingElementLocation = AllianceShippingElementLocation.LEFT;
            telemetry.addData("Shipping Element Location", "Left");
        }

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB); // Convert back to RGB for rectangle

        // Draw AIO Locations
        Imgproc.rectangle(mat, LEFT_ROI, AOI_COLOR);
        Imgproc.rectangle(mat, RIGHT_ROI, AOI_COLOR);

        telemetry.update();
        return mat;
    }

    public AllianceShippingElementLocation GetAllianceShippingElementLocation() {
        return this.shippingElementLocation;
    }
}
