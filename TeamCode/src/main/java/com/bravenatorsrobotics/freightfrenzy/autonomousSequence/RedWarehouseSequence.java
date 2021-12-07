package com.bravenatorsrobotics.freightfrenzy.autonomousSequence;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.common.drive.FourWheelDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
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

        // Capture the robot's home position
        FourWheelDrive.MotorPosition homePosition = robot.drive.GetCurrentMotorPositions();

        // Move off the wall
        robot.drive.DriveInches(0.5, 6);

        // Turn towards the game material
        robot.drive.TurnDegrees(0.5, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive to the game material
        robot.drive.DriveInches(0.5, 6);

        // Get the game material
        boolean robotContainsGameMaterial = GetGameMaterial();

        // Return to the home position
        robot.drive.SetMotorPositions(homePosition, 0.5);
    }

    // Returns if a game material is in the cup or not
    private boolean GetGameMaterial() {

        // Set the cup position to intake position
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Turn on the intake
        auto.intakeMotor.setPower(0.5);
        robot.drive.Drive(0.075, 0, 0);

        // Setup timer
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        boolean isObjectInCup = false;

        while(opModeIsActive()) {

            isObjectInCup = auto.liftController.IsObjectInCup();

            if(timer.milliseconds() > 5000 || isObjectInCup) {
                robot.drive.Stop(); // Stop the robot
                auto.intakeMotor.setPower(-0.5); // Reverse the intake motor
                break;
            }

        }

        // If there's an object in the cup tilt it back
        if(isObjectInCup) {
            auto.liftController.SetCupPosition(LiftController.CupPosition.TILTED_POSITION);
        }

        // Wait to spit out any stuck game material
        sleep(1500);

        // Stop the intake
        auto.intakeMotor.setPower(0);

        return isObjectInCup;
    }
}
