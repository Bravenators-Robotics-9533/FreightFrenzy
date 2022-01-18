package com.bravenatorsrobotics.freightfrenzy.autonomous.redstorageunit;

import com.bravenatorsrobotics.common.linearstatemachine.LinearState;
import com.bravenatorsrobotics.common.linearstatemachine.LinearStateMachine;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.autonomous.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.SpinDuckState;

public class RedStorageUnitSequence extends AbstractAutonomousSequence {

    private final LinearStateMachine linearStateMachine;

    private double startAngle;

    public RedStorageUnitSequence(Auto auto) {
        super(auto);

        this.linearStateMachine = new LinearStateMachine(auto.telemetry, new LinearState[] {
                new RSUDriveToDuckState(auto, this),
                new SpinDuckState(auto),
                new RSULineUpDriveByState(auto, this)
        });
    }

    @Override
    public void RunSequence() {
        linearStateMachine.RunAllStatesLinear();
    }

    public double GetStartAngle() { return this.startAngle; }
    public void SetStartAngle(double startAngle) { this.startAngle = startAngle; }

//    @Override
//    public void RunSequence() {
//
//        final double startAngle = auto.imuController.GetZAxis();
//
//        robot.drive.StrafeInches(0.25, 14.0);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.DriveByInches(0.25, 11);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.TurnDegrees(0.25, 45, AbstractDrive.TurnDirection.CLOCKWISE);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.StrafeInches(0.25, -10.0);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        auto.turnTableSpinner.setPower(1);
//        sleep(2000);
//        auto.turnTableSpinner.setPower(0);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.StrafeInches(0.25, 10.0);
//
//        // Calculate Turn Distance
//        final double turnDistance = auto.imuController.GetZAxis() - startAngle;
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.TurnDegrees(0.25, (int) turnDistance, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.StrafeInches(0.25, 3.0);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        // Calculate Drive Distance
//        final int startPosition = robot.GetDriveMotors()[0].getCurrentPosition();
//        final int driveDistanceEncoderTicks
//                = robot.drive.CalculateDriveByInches(-28.0, -28.0).frontLeftPosition;
//
//        int distanceDriven = 0;
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.Drive(-0.10, 0, 0);
//
//        while(opModeIsActive()) {
//            int currentMotorPosition = robot.GetDriveMotors()[0].getCurrentPosition();
//            final int distanceToDrive = Math.abs(startPosition + driveDistanceEncoderTicks) - Math.abs(currentMotorPosition);
//
//            if(auto.sideDistanceSensor.getDistance(DistanceUnit.MM) < 58 ||
//                    currentMotorPosition <= startPosition + driveDistanceEncoderTicks) {
//                robot.drive.Stop();
//
//
//                robot.drive.DriveInches(0.25, distanceToDrive);
//                distanceDriven = currentMotorPosition;
//
//                break;
//            }
//
//            telemetry.addData("Current Motor Position", currentMotorPosition);
//            telemetry.addData("Delta Position", currentMotorPosition - startPosition);
//            telemetry.addData("Start Position", startPosition);
//            telemetry.addData("End Distance", driveDistanceEncoderTicks);
//            telemetry.update();
//        }
//
//        // Drive to alliance shipping hub
//
//        // Turn towards shipping hub
//        int degreesToShippingHub = 65;
//        robot.drive.TurnDegrees(0.25, degreesToShippingHub, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);
//
//        // Lift the lift
//        if(distanceDriven > 875) {
//            // Lift Stage 1
//            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_1);
//        } else if(distanceDriven < 495) {
//            // Lift Height 3
//            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_3);
//        } else {
//            // Lift Height 2
//            auto.liftController.GoToStage(LiftController.LiftStage.STAGE_2);
//        }
//
//        final double distanceToDriveToShippingHub = -8.75;
//
//        // Drive to shipping hub
//        robot.drive.DriveByInches(0.30, distanceToDriveToShippingHub);
//
//        // Dump the cup
//        auto.liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
//        sleep(600);
//        auto.liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
//        sleep(500);
//
//        // Back up away from shipping hub
//        robot.drive.DriveByInches(0.50, 21.0 + distanceToDriveToShippingHub);
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        auto.liftController.ZeroLift();
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.TurnDegrees(0.50, degreesToShippingHub, AbstractDrive.TurnDirection.CLOCKWISE);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        robot.drive.DriveByInches(0.5, 32);
//
//        sleep(SLEEP_AMOUNT_MILLIS);
//
//        double strafeDistance = 18;
//
//        robot.drive.StrafeInches(0.50, strafeDistance);
//    }
}
