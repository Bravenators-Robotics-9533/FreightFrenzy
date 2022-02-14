package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.Config;
import com.bravenatorsrobotics.freightfrenzy.autonomous.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;

import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;

public class RedStorageUnitSequence extends AbstractAutonomousSequence {

    private static final double ROBOT_SPEED = 0.75;

    private static final double DISTANCE_TO_SHIPPING_HUB_INCHES = 24.0;
    private static final double DISTANCE_TO_GET_CLOSE_TO_SHIPPING_HUB = 6.0;

    private static final int DUMP_TIME = 600;
    private static final int DUCK_SPIN_TIME = 2000;

    public RedStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {

        // Strafe off the wall
        robot.drive.StrafeInches(ROBOT_SPEED, 12);

        // Drive backwards to duck spinner
        robot.drive.DriveByInches(ROBOT_SPEED, -18.0);

        // Turn CCW 135 degrees
        robot.drive.TurnDegrees(ROBOT_SPEED, 135, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Strafe left
        robot.drive.StrafeInches(ROBOT_SPEED, -8.0);

        // Spin the duck
        auto.turnTableSpinner.setPower(-1);
        sleep(DUCK_SPIN_TIME);
        auto.turnTableSpinner.setPower(0);

        // Strafe Right
        robot.drive.StrafeInches(ROBOT_SPEED, 8.0);

        // Turn 45 degrees CCW
        robot.drive.TurnDegrees(ROBOT_SPEED, 45, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Drive to the alliance shipping hub
        robot.drive.DriveByInches(ROBOT_SPEED, -DISTANCE_TO_SHIPPING_HUB_INCHES);

        // Turn towards the alliance shipping hub
        robot.drive.TurnDegrees(ROBOT_SPEED, 65, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Lift the lift
        switch (auto.GetAllianceShippingElementLocation()) {
            case RIGHT: // Position 1
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_1);
                break;
            case CENTER: // Position 2
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_2);
                break;
            case LEFT: // Position 3
            default:
                auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);
                break;
        }

        // Drive Backwards
        robot.drive.DriveByInches(ROBOT_SPEED, -DISTANCE_TO_GET_CLOSE_TO_SHIPPING_HUB);

        // Dump the Cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(DUMP_TIME);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);

        // Drive forward
        robot.drive.DriveByInches(ROBOT_SPEED, DISTANCE_TO_GET_CLOSE_TO_SHIPPING_HUB);

        // Lower the lift
        auto.liftController.ZeroLift();

        // Turn 65 degrees CW
        robot.drive.TurnDegrees(ROBOT_SPEED, 65, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive to the wall
        robot.drive.DriveByInches(ROBOT_SPEED, DISTANCE_TO_SHIPPING_HUB_INCHES);

        // Strafe Right
        robot.drive.StrafeInches(ROBOT_SPEED, 12);

    }
}
