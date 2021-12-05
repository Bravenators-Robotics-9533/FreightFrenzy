package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RedStorageUnitSequence extends AbstractAutonomousSequence {

    public RedStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public boolean RunSequence() {

        final double startAngle = auto.imuController.GetZAxis();

        robot.drive.StrafeInches(0.25, 14.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.25, 10);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, 45, AbstractDrive.TurnDirection.CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.25, -10.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        auto.turnTableSpinner.setPower(1);
        sleep(2500);
        auto.turnTableSpinner.setPower(0);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.25, 10.0);

        // Calculate Turn Distance
        final double turnDistance = auto.imuController.GetZAxis() - startAngle;

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, (int) turnDistance, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.25, 3.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        // Calculate Drive Distance
        final int startPosition = robot.GetDriveMotors()[0].getCurrentPosition();
        final int driveDistanceEncoderTicks
                = robot.drive.CalculateDriveByInches(-28.0, -28.0).frontLeftPosition;

        int distanceDriven = 0;

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.Drive(-0.10, 0, 0);

        while(opModeIsActive()) {
            int currentMotorPosition = robot.GetDriveMotors()[0].getCurrentPosition();
            final int distanceToDrive = Math.abs(startPosition + driveDistanceEncoderTicks) - Math.abs(currentMotorPosition);

            if(auto.sideDistanceSensor.getDistance(DistanceUnit.MM) < 58 ||
                    currentMotorPosition <= startPosition + driveDistanceEncoderTicks) {
                robot.drive.Stop();


                robot.drive.DriveInches(0.25, distanceToDrive);
                distanceDriven = currentMotorPosition;

                break;
            }

            telemetry.addData("Current Motor Position", currentMotorPosition);
            telemetry.addData("Delta Position", currentMotorPosition - startPosition);
            telemetry.addData("Start Position", startPosition);
            telemetry.addData("End Distance", driveDistanceEncoderTicks);
            telemetry.update();
        }

        // Drive to alliance shipping hub

        // Turn towards shipping hub
        int degreesToShippingHub = 55;
        robot.drive.TurnDegrees(0.25, degreesToShippingHub, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Lift the lift
        if(distanceDriven > 875) {
            // Lift Stage 1
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_1);
        } else if(distanceDriven < 495) {
            // Lift Height 3
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);
        } else {
            // Lift Height 2
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_2);
        }

        final double distanceToDriveToShippingHub = -6.75;

        // Drive to shipping hub
        robot.drive.DriveByInches(0.30, distanceToDriveToShippingHub);

        // Dump the cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(600);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
        sleep(500);

        // Back up away from shipping hub
        robot.drive.DriveByInches(0.50, 21.0 + distanceToDriveToShippingHub);
        sleep(SLEEP_AMOUNT_MILLIS);

        auto.liftController.ZeroLift();
        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.50, degreesToShippingHub, AbstractDrive.TurnDirection.CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.5, 32);

        sleep(SLEEP_AMOUNT_MILLIS);

        double strafeDistance = 18;

        robot.drive.StrafeInches(0.50, strafeDistance);

        return true; // Passes
    }
}
