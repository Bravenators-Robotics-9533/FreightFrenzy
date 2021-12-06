package com.bravenatorsrobotics.freightfrenzy.autonomousSequence;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.common.drive.FourWheelDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RedWarehouseSequence extends AbstractAutonomousSequence {

    public RedWarehouseSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {

        // Strafe into the warehouse
        robot.drive.StrafeInches(0.5, 12);

        // Move off the wall
        robot.drive.DriveInches(0.5, 6);

        // Turn towards the game material
        robot.drive.TurnDegrees(0.5, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive forward slowly until the game material is in the cup unless timeout is reached

    }

    // Returns if one is reached or not
    private boolean GetGameMaterial() {

        // Save the current position of each motor
        final FourWheelDrive.MotorPosition startingPositions = robot.drive.GetCurrentMotorPositions();

        // Turn on the intake
        auto.intakeMotor.setPower(0.5);
        robot.drive.Drive(0.075, 0, 0);

        // Setup timer
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opModeIsActive()) {

            // TODO: Detect Object In Cup

            if(timer.milliseconds() > 5000) {

                robot.drive.Stop(); // Stop the robot
                auto.intakeMotor.setPower(0); // Stop the intake motors

            }

        }

        return true;
    }
}
