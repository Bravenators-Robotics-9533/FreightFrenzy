package com.bravenatorsrobotics.common.drive;

import androidx.annotation.NonNull;

import com.bravenatorsrobotics.common.core.Robot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
import com.sun.source.tree.ModifiersTree;

public class FourWheelDrive extends AbstractDrive {

    protected final DcMotorEx frontLeft;
    protected final DcMotorEx frontRight;
    protected final DcMotorEx backLeft;
    protected final DcMotorEx backRight;

    public static class MotorPosition {

        public final int frontLeftPosition;
        public final int frontRightPosition;
        public final int backLeftPosition;
        public final int backRightPosition;

        public MotorPosition(int fl, int fr, int bl, int br) {
            this.frontLeftPosition = fl;
            this.frontRightPosition = fr;
            this.backLeftPosition = bl;
            this.backRightPosition = br;
        }

        public MotorPosition(int left, int right) {
            this.frontLeftPosition = left;
            this.backLeftPosition = left;

            this.frontRightPosition = right;
            this.backRightPosition = right;
        }

    }

    public FourWheelDrive(Robot<? extends FourWheelDrive> robot) {
        super(robot);

        this.frontLeft = robot.GetDriveMotors()[0];
        this.frontRight = robot.GetDriveMotors()[1];
        this.backLeft = robot.GetDriveMotors()[2];
        this.backRight = robot.GetDriveMotors()[3];
    }

    public static String[] GenerateMotors(String frontLeftName, boolean frontLeftReversed,
                                          String frontRightName, boolean frontRightReversed,
                                          String backLeftName, boolean backLeftReversed,
                                          String backRightName, boolean backRightReversed) {
        return new String[] {
                (frontLeftReversed ? "!" : "") + frontLeftName,
                (frontRightReversed ? "!" : "") + frontRightName,
                (backLeftReversed ? "!" : "") + backLeftName,
                (backRightReversed ? "!" : "") + backRightName
        };
    }

    protected void LoopUntilNotBusy() {
        while(robot.opMode.opModeIsActive()) {
            if ((!frontLeft.isBusy() &&
                    !backLeft.isBusy() &&
                    !backRight.isBusy() &&
                    !backLeft.isBusy())) break;
        }
    }

    // Four Wheel Drive
    @Override public int GetExpectedMotorCount() { return 4; }

    @Override
    public void Drive(double v, double h, double r) {
        double leftPower  = Range.clip(v + r, -1.0, 1.0);
        double rightPower = Range.clip(v - r, -1.0, 1.0);

        SetPower(this.frontLeft, leftPower);
        SetPower(this.backLeft, leftPower);
        SetPower(this.frontRight, rightPower);
        SetPower(this.backRight, rightPower);
    }

    @Override
    public void Stop() {
        this.frontLeft.setPower(0);
        this.frontRight.setPower(0);
        this.backLeft.setPower(0);
        this.backRight.setPower(0);
    }

    @Override
    public void DriveEncoderTicks(double power, int leftTicks, int rightTicks) {
        // Increment Target Positions
        IncrementTargetPosition(frontLeft, leftTicks);
        IncrementTargetPosition(backLeft, leftTicks);
        IncrementTargetPosition(frontRight, rightTicks);
        IncrementTargetPosition(backRight, rightTicks);

        robot.SetRunMode(DcMotorEx.RunMode.RUN_TO_POSITION); // Set Run Mode
        SetAllPower(power); // Set Motor Power

        LoopUntilNotBusy();

        this.Stop();

        robot.SetRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void SetMotorPositions(@NonNull MotorPosition motorPosition, double power) {
        // Set target positions
        frontLeft.setTargetPosition(motorPosition.frontLeftPosition);
        frontRight.setTargetPosition(motorPosition.frontRightPosition);
        backLeft.setTargetPosition(motorPosition.backLeftPosition);
        backRight.setTargetPosition(motorPosition.backRightPosition);

        // Set Motor Mode and Power
        robot.SetRunMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        SetAllPower(power);

        LoopUntilNotBusy();

        // Stop the motors
        this.Stop();

        // Return Motor Mode
        robot.SetRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public MotorPosition GetCurrentMotorPositions() {
        return new MotorPosition(
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition());
    }

    public MotorPosition CalculateDriveByInches(double leftInches, double rightInches) {
        return new MotorPosition((int) (leftInches * ticksPerInch), (int) (rightInches * ticksPerInch));
    }

    public MotorPosition CalculateTurnDegrees(int degrees, TurnDirection turnDirection) {
        // Calculate the Distance in Inches
        double distance = Math.abs(degrees) * (pivotCircleCircumference / 360.0);

        // Reverse if turning counter-clockwise
        if(turnDirection == TurnDirection.CLOCKWISE)
            distance = -distance;

        // Convert Inches to Encoder Ticks
        distance *= ticksPerInch;

        return new MotorPosition((int) -distance, (int) distance);
    }

    @Override
    public void DriveInches(double power, double leftInches, double rightInches) {
        MotorPosition calculatedPosition = CalculateDriveByInches(leftInches, rightInches);
        DriveEncoderTicks(power, calculatedPosition.backLeftPosition, calculatedPosition.backRightPosition);
    }

    @Override
    public void TurnDegrees(double power, int degrees, TurnDirection turnDirection) {
        MotorPosition calculatedPosition = CalculateTurnDegrees(degrees, turnDirection);
        DriveEncoderTicks(power, calculatedPosition.backLeftPosition, calculatedPosition.backRightPosition);
    }
}
