package com.bravenatorsrobotics.common.drive;

import com.bravenatorsrobotics.common.core.Robot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

public class TwoWheelDrive extends AbstractDrive {

    private final DcMotorEx left;
    private final DcMotorEx right;

    public TwoWheelDrive(Robot<? extends TwoWheelDrive> robot) {
        super(robot);

        this.left = robot.GetDriveMotors()[0];
        this.right = robot.GetDriveMotors()[1];
    }

    public static String[] GenerateMotors(String left, boolean leftReversed, String right, boolean rightReversed) {
        return new String[] {
                (leftReversed ? "!" : "") + left,
                (rightReversed ? "!" : "") + right
        };
    }

    private void LoopUntilNotBusy() {
        while(true) {
            if (!robot.opMode.opModeIsActive() || (!left.isBusy() && !right.isBusy()))
                break;
        }
    }

    // Two wheel drive (contains two motors)
    @Override public int GetExpectedMotorCount() { return 2; }

    @Override
    public void Drive(double v, double h, double r) {
        double leftPower  = Range.clip(v + r, -1.0, 1.0);
        double rightPower = Range.clip(v - r, -1.0, 1.0);

        SetPower(left, leftPower);
        SetPower(right, rightPower);
    }

    @Override
    public void Stop() {
        this.left.setPower(0);
        this.right.setPower(0);
    }

    @Override
    public void DriveInches(double power, int leftTicks, int rightTicks) {
        // Increment Target Positions
        IncrementTargetPosition(left, leftTicks);
        IncrementTargetPosition(right, rightTicks);

        robot.SetRunMode(DcMotor.RunMode.RUN_TO_POSITION); // Set Run Mode
        SetAllPower(power); // Set Motor Power

        LoopUntilNotBusy();

        this.Stop();

        robot.SetRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void DriveInches(double power, double leftInches, double rightInches) {
        DriveInches(power, (int) (leftInches * ticksPerInch), (int) (rightInches * ticksPerInch));
    }

    @Override
    public void TurnDegrees(double power, int degrees, TurnDirection turnDirection) {
        // Calculate the Distance in Inches
        double distance = Math.abs(degrees) * (pivotCircleCircumference / 360.0);

        // Reverse if turning counter-clockwise
        if(turnDirection == TurnDirection.COUNTER_CLOCKWISE)
            distance = -distance;

        // Drive the sides in different direction of the specified distance
        this.DriveInches(power, -distance, distance);
    }
}
