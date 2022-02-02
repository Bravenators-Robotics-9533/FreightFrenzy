package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class BlueStorageUnitSequence extends AbstractAutonomousSequence {

    private static final double INCHES_TO_SHIPPING_HUB = 6;
    private static final double INCHES_TO_WALL = 96.0;
    private static final double INCHES_TO_DUCK_SPINNER = 6.0;
    private static final double INCHES_TO_PARK = 16.0;

    private static final int DUMP_TIME = 600;
    private static final int DUCK_SPIN_TIME = 2000;

    public BlueStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {
        // Strafe away from the wall
        robot.drive.StrafeInches(0.75, -12.0);

        // Drive towards the shipping hub
        robot.drive.DriveByInches(0.75, -48.0);

        // Turn 90 degrees CW
        robot.drive.TurnDegrees(0.75, 90, AbstractDrive.TurnDirection.CLOCKWISE);

        // Lift the lift
        auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3); // TODO: Toggleable Lift Stage

        // Drive up to the alliance shipping hub
        robot.drive.DriveByInches(0.5, -INCHES_TO_SHIPPING_HUB);

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
        auto.turnTableSpinner.setPower(1);
        sleep(DUCK_SPIN_TIME);
        auto.turnTableSpinner.setPower(0);

        // Strafe to park
        robot.drive.StrafeInches(0.75, INCHES_TO_PARK);
    }
}
