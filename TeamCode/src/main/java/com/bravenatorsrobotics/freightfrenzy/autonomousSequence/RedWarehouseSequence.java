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

        // Capture the robot's warehouse position
        FourWheelDrive.MotorPosition warehousePosition = robot.drive.GetCurrentMotorPositions();

        // Move off the wall
        robot.drive.DriveInches(0.5, 6);

        // Turn towards the game material
        robot.drive.TurnDegrees(0.5, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive to the game material
        robot.drive.DriveInches(0.5, 6);

        // Get the game material
        boolean robotContainsGameMaterial = GetGameMaterial();

        // Return to the warehouse position
        robot.drive.SetMotorPositions(warehousePosition, 0.5);

        // Strafe back to starting position
        robot.drive.StrafeInches(0.5, -12);

        // Save the home position
        FourWheelDrive.MotorPosition homePosition = robot.drive.GetCurrentMotorPositions();

        // If the robot contains a block, deliver it
        if(robotContainsGameMaterial)
            DeliverBlockFromStartingPosition();

        // Return to the home positions
        robot.drive.SetMotorPositions(homePosition, 0.5);
    }

    // Returns if a game material is in the cup or not
    private boolean GetGameMaterial() {

        // Make sure the lift is zeroed
        auto.liftController.ZeroLift();

        // Set the cup position to intake position
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Turn on the intake
        auto.intakeMotor.setPower(0.5);

        // Start moving the robot towards the game material
        robot.drive.Drive(0.1, 0, 0);

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
        sleep(750);

        // Stop the intake
        auto.intakeMotor.setPower(0);

        return isObjectInCup;
    }

    private void DeliverBlockFromStartingPosition() {
        // Strafe to the alliance shipping hub
        robot.drive.StrafeInches(0.5, -12);

        // Move off the wall
        robot.drive.DriveInches(0.5, 6);

        // Spin the lift to the correction position
        robot.drive.TurnDegrees(0.5, 180, AbstractDrive.TurnDirection.CLOCKWISE);

        // Lift the lift to the top position
        auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);

        final double distanceToShippingHub = 8.0; // inches

        // Drive to the alliance shipping hub
        robot.drive.DriveInches(0.5, -distanceToShippingHub);

        // Dump the cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(800);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Drive away from the alliance shipping hub
        robot.drive.DriveInches(0.5, distanceToShippingHub);

        // Lower the lift
        auto.liftController.ZeroLift();
    }
}
