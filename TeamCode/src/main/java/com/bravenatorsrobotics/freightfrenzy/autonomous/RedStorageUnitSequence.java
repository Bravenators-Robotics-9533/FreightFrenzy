package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.Config;
import com.bravenatorsrobotics.freightfrenzy.autonomous.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;

import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;

public class RedStorageUnitSequence extends AbstractAutonomousSequence {

    private static final double ROBOT_SPEED = 0.75;
    private static final double ROBOT_STRAFE_SPEED = 0.5;

    private static final double DISTANCE_TO_LINE_UP_SHIPPING_HUB = 18.0;
    private static final double INCHES_TO_SHIPPING_HUB = 8.0;
    private static final double INCHES_TO_WALL = 44.0;

    private static final int DUMP_TIME = 600;
    private static final int DUCK_SPIN_TIME = 2000;

    private static final double LEVEL_1_DISTANCE_OFFSET = 3.50;
    private static final double LEVEL_2_DISTANCE_OFFSET = 2.0;

    public RedStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {
        // Strafe away from the wall
        robot.drive.StrafeInches(ROBOT_STRAFE_SPEED, -12.0);

        // Drive towards shipping hub
        robot.drive.DriveByInches(ROBOT_SPEED, DISTANCE_TO_LINE_UP_SHIPPING_HUB);

        // Turn 90 degrees CW
        robot.drive.TurnDegrees(0.5, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        double driveOffset = 0.0;

        // Lift the lift
        switch (auto.GetAllianceShippingElementLocation()) {
            case LEFT: // Position 1
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_1);
                driveOffset = LEVEL_1_DISTANCE_OFFSET;
                break;
            case CENTER: // Position 2
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_2);
                driveOffset = LEVEL_2_DISTANCE_OFFSET;
                break;
            case RIGHT: // Position 3
            default:
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);
                break;
        }

        // Drive up to the alliance shipping hub
        robot.drive.DriveByInches(0.5, -INCHES_TO_SHIPPING_HUB - driveOffset);

        sleep(300);

        // Dump the cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(DUMP_TIME);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Drive away from the alliance shipping hub
        robot.drive.DriveByInches(0.5, INCHES_TO_SHIPPING_HUB + driveOffset);

        // Lower the lift
        auto.liftController.ZeroLift();

        // Turn 90 degrees CW
        robot.drive.TurnDegrees(0.75, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive to the wall
        robot.drive.DriveByInches(0.5, INCHES_TO_WALL);

        sleep(400);

        // Turn to line up with duck spinner
        robot.drive.TurnDegrees(0.5, 45, AbstractDrive.TurnDirection.CLOCKWISE);

        // Strafe into duck spinner
        robot.drive.StrafeInches(0.5, -6.0);

        // Spin the duck
        auto.turnTableSpinner.setPower(0.75);
        sleep(DUCK_SPIN_TIME);
        auto.turnTableSpinner.setPower(0);

        // Strafe Right
        robot.drive.StrafeInches(0.5, 24);

        // Turn
        robot.drive.TurnDegrees(0.5, 45, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Move Forward
        robot.drive.DriveByInches(0.25, 10);
    }
}
