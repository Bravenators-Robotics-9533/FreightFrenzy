package com.bravenatorsrobotics.common.drive;

import com.bravenatorsrobotics.common.core.Robot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;

import dk.sgjesse.r8api.FileOrigin;

public class MecanumDrive extends FourWheelDrive {

    public MecanumDrive(Robot<? extends MecanumDrive> robot) {
        super(robot);
    }

    protected FourWheelDrive.MotorPosition accumulatedDeltaPosition;

    public void PushBackMovement(FourWheelDrive.MotorPosition deltaPosition) {
        accumulatedDeltaPosition = accumulatedDeltaPosition.Add(deltaPosition);
    }

    public void ClearMovements() {
        accumulatedDeltaPosition = new FourWheelDrive.MotorPosition(0, 0, 0, 0);
    }

    public void RunToAccumulatedPosition(double power) {
        FourWheelDrive.MotorPosition finalPosition = GetCurrentMotorPositions().Add(accumulatedDeltaPosition);
        super.SetMotorPositions(finalPosition, power);
        ClearMovements(); // Clear at the end
    }

    protected static double ScalePower(double value, double max) {
        if(max == 0) { return 0; }
        return value / max;
    }

    @Override
    public void Drive(double v, double h, double r) {
        // Calculate Motors Speeds
        double frontLeft    = v - h + r;
        double frontRight   = v + h - r;
        double backRight    = v - h - r;
        double backLeft     = v + h + r;

        // Limit the vectors to under 1
        double max = Math.max(
                Math.abs(backLeft),
                Math.max(
                        Math.abs(backRight),
                        Math.max(
                                Math.abs(frontLeft), Math.abs(frontRight)
                        )
                )
        );

        // Scale the power
        if(max > 1) { // Only scale if max is greater than one
            frontLeft   = ScalePower(frontLeft, max);
            frontRight  = ScalePower(frontRight, max);
            backLeft    = ScalePower(backLeft, max);
            backRight   = ScalePower(backRight, max);
        }

        SetPower(this.frontLeft, frontLeft);
        SetPower(this.frontRight, frontRight);
        SetPower(this.backLeft, backLeft);
        SetPower(this.backRight, backRight);
    }

    public void StrafeInches(double power, double inches) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) (ticksPerInch * inches));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) (ticksPerInch * inches));

        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) (ticksPerInch * inches));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) (ticksPerInch * inches));

        robot.SetRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        SetAllPower(power);

        LoopUntilNotBusy();

        robot.SetRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
