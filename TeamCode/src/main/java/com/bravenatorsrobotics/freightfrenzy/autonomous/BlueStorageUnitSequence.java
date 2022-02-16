package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class BlueStorageUnitSequence extends AbstractAutonomousSequence {

    private static final double DISTANCE_TO_LINE_UP_SHIPPING_HUB = 24;
    private static final double INCHES_TO_SHIPPING_HUB = 8;
    private static final double INCHES_TO_WALL = 48.0;
    private static final double INCHES_TO_DUCK_SPINNER = 11.0;
    private static final double INCHES_TO_PARK = 19.50;

    private static final int DUMP_TIME = 600;
    private static final int DUCK_SPIN_TIME = 2250;

    private static final double LEVEL_1_DISTANCE_OFFSET = 3.50;
    private static final double LEVEL_2_DISTANCE_OFFSET = 2.0;

    public BlueStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {
        // Strafe away from the wall
        robot.drive.StrafeInches(0.5, -12.0);

        // Drive towards the shipping hub
        robot.drive.DriveByInches(0.75, -DISTANCE_TO_LINE_UP_SHIPPING_HUB);

        // Turn 90 degrees CW
        robot.drive.TurnDegrees(0.75, 90, AbstractDrive.TurnDirection.CLOCKWISE);

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
        robot.drive.DriveByInches(0.5, INCHES_TO_SHIPPING_HUB);

        // Lower the lift
        auto.liftController.ZeroLift();

        // Turn 90 degrees CW
        robot.drive.TurnDegrees(0.75, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive to the wall
        robot.drive.DriveByInches(0.75, -INCHES_TO_WALL);

        // Strafe into the duck spinner
        robot.drive.StrafeInches(0.5, -INCHES_TO_DUCK_SPINNER);

        // Spin the duck
        auto.turnTableSpinner.setPower(-0.75);
        sleep(DUCK_SPIN_TIME);
        auto.turnTableSpinner.setPower(0);

        // Strafe to park
        robot.drive.StrafeInches(0.75, INCHES_TO_PARK);
    }
}
