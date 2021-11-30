package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class BlueStorageUnitSequence extends AbstractAutonomousSequence {

    public BlueStorageUnitSequence(Auto auto) {
        super(auto);
    }

    @Override
    public boolean RunSequence() {
        final double startAngle = auto.imuController.GetZAxis();

        // Strafe off wall
        StrafeSeconds(-0.25, 1.20);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Drive To wall
        robot.drive.DriveByInches(0.25, -20);

        // Strafe into duck spinner
        sleep(SLEEP_AMOUNT_MILLIS);
        StrafeSeconds(0.15, 1.0);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Spin Turn-Table
        auto.turnTableSpinner.setPower(-1);
        sleep(2500);
        auto.turnTableSpinner.setPower(0);

        // Strafe away from turn-table
        sleep(SLEEP_AMOUNT_MILLIS);
        StrafeSeconds(-0.25, 0.70);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Straighten out into wall
        robot.drive.DriveByInches(0.25, -4.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        final int distanceToShippingHub = 1700; // TODO: Enter the right amount
        final int startTicks = robot.GetDriveMotors()[0].getCurrentPosition();

        sleep(SLEEP_AMOUNT_MILLIS);

        // Drive past the blocks
        robot.drive.Drive(0.25, 0, 0);

        int driveDistance = 0;

        while(opModeIsActive()) {
            int currentPosition = robot.GetDriveMotors()[0].getCurrentPosition();
            int distance = currentPosition - startTicks;

            telemetry.addData("Current Distance", distance);
            telemetry.update();

            if(auto.sideDistanceSensor.getDistance(DistanceUnit.MM) < 59.85 || distance >= distanceToShippingHub) {
                telemetry.addData("Distance", distance);
                telemetry.update();
                robot.drive.Stop();
                driveDistance = distance;
                break;
            }
        }

        sleep(SLEEP_AMOUNT_MILLIS);

        final int distanceToDrive = distanceToShippingHub - driveDistance;

        // Drive to the shipping hub
        robot.drive.DriveInches(0.25, distanceToDrive);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Turn to the goal
        robot.drive.TurnDegrees(0.25, 135, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        // Lift the lift
        if(driveDistance < 950) {
            // Position 1
            telemetry.log().add("Lift cd ~/Position: 3");
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);
        } else if(driveDistance > 1450) {
            // Position 3
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_1);
            telemetry.log().add("Lift Position: 1");
        } else {
            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_2);
            telemetry.log().add("Lift Position: 2");
            // Position 2
        }


        sleep(SLEEP_AMOUNT_MILLIS);

        final double shippingHubDriveDistance = 11.0;

        // Drive into the shipping hub
        robot.drive.DriveByInches(0.25, -shippingHubDriveDistance);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Dump the cup
        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(600);
        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
        sleep(500);

        // Back up away from shipping hub
        robot.drive.DriveByInches(0.25, shippingHubDriveDistance + 1.50);
        sleep(SLEEP_AMOUNT_MILLIS);

        auto.liftController.ZeroLift();
        sleep(SLEEP_AMOUNT_MILLIS);

        // Turn back
        robot.drive.TurnDegrees(0.25, 135, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive back to wall
        robot.drive.DriveInches(0.50, -distanceToShippingHub);

        // Strafe into the shipping hub tape
        StrafeSeconds(-0.25, 1.40);

        return true; // Sequence Succeeded
    }

    private void StrafeSeconds(double power, double time) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        robot.drive.Drive(0, power, 0);

        while(opModeIsActive()) {
            if(timer.seconds() >= time) {
                robot.drive.Stop();
                break;
            }
        }
    }
}
