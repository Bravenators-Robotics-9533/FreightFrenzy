package com.bravenatorsrobotics.freightfrenzy.autonomousSequence;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.common.drive.FourWheelDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RedWarehouseSequence extends AbstractAutonomousSequence {

    private static final double ROBOT_SPEED = 0.75;

    public RedWarehouseSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {

        final double distanceWarehouse = 36; // One and a half tiles

        // Strafe into the warehouse
        robot.drive.StrafeInches(ROBOT_SPEED, distanceWarehouse);

        // Capture the robot's warehouse position
        FourWheelDrive.MotorPosition warehousePosition = robot.drive.GetCurrentMotorPositions();

        // Calculate the encoder delta position to drive off the wall, turn, and drive towards the game material
        robot.drive.ClearMovements();
        robot.drive.PushBackMovement(robot.drive.CalculateDriveByInches(6, 6)); // Move off the wall
        robot.drive.PushBackMovement(robot.drive.CalculateTurnDegrees(90, AbstractDrive.TurnDirection.CLOCKWISE)); // Turn towards the game elements
        robot.drive.PushBackMovement(robot.drive.CalculateDriveByInches(6, 6)); // Get closer to the game elements
        robot.drive.RunToAccumulatedPosition(ROBOT_SPEED); // Run the accumulated movements

        // Get the game material
        boolean robotContainsGameMaterial = GetGameMaterial();

        // Return to the warehouse position
        robot.drive.SetMotorPositions(warehousePosition, ROBOT_SPEED);

        // Strafe back to starting position
        robot.drive.StrafeInches(ROBOT_SPEED, -distanceWarehouse);

        auto.intakeMotor.setPower(0);

        // Save the home position
        FourWheelDrive.MotorPosition homePosition = robot.drive.GetCurrentMotorPositions();

        // If the robot contains a block, deliver it
        if(robotContainsGameMaterial)
            DeliverBlockFromStartingPosition();

        // Return to the home positions
        robot.drive.SetMotorPositions(homePosition, ROBOT_SPEED);
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
        // Stop the intake

        return isObjectInCup;
    }

    private void DeliverBlockFromStartingPosition() {
        final double inchesToStrafe = 24; // One tile distance

        // Strafe to the alliance shipping hub
        robot.drive.StrafeInches(ROBOT_SPEED, -inchesToStrafe);

        // Move off the wall
        robot.drive.DriveInches(ROBOT_SPEED, 6);

        // Spin the lift to the correction position
        robot.drive.TurnDegrees(ROBOT_SPEED, 180, AbstractDrive.TurnDirection.CLOCKWISE);

        // Lift the lift to the top position
        auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);

        final double distanceToShippingHub = 16.0; // inches

        // Drive to the alliance shipping hub
        robot.drive.DriveInches(ROBOT_SPEED, -distanceToShippingHub);

        // Dump the cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(800);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Drive away from the alliance shipping hub
        robot.drive.DriveInches(ROBOT_SPEED, distanceToShippingHub);

        // Lower the lift
        auto.liftController.ZeroLift();

        // Spin around to correct position
        robot.drive.TurnDegrees(ROBOT_SPEED, 180, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Drive back to the wall
        robot.drive.DriveInches(ROBOT_SPEED, -6);

        // Strafe back home
        robot.drive.StrafeInches(ROBOT_SPEED, inchesToStrafe);
    }
}
