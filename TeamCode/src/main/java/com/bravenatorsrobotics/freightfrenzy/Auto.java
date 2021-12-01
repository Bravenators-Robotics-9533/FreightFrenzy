package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.bravenatorsrobotics.freightfrenzy.subsystem.IMUController;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous")
public class Auto extends AutonomousMode<MecanumDrive> {

    private static final int SLEEP_AMOUNT_MILLIS = 20;

    private Config config;

    private LiftController liftController;
    private IMUController imuController;

    private DcMotorEx turnTableSpinner;

    private RevColorSensorV3 sideDistanceSensor;

    public Auto() { super(new Specifications()); }

    @Override
    public void OnInitialize() {
        config = new Config(hardwareMap.appContext); // Get the saved configuration for later

        imuController = new IMUController(this);
        imuController.Initialize();

        liftController = new LiftController(this);

        turnTableSpinner = robot.GetMotor("turnTable", false);
        sideDistanceSensor = hardwareMap.get(RevColorSensorV3.class, "sideDistanceSensor");

        liftController.SetCupPosition(LiftController.CupPosition.TILTED_POSITION);
    }

    @Override
    public void OnStart() {

        sleep(SLEEP_AMOUNT_MILLIS);

        // Call the appropriate method and pass in the movement modifier
        switch (config.startingPosition) {
            case WAREHOUSE:
                RunWarehouse();
                break;
            case STORAGE_UNIT:
                if(config.allianceColor == Config.AllianceColor.RED)
                    RunRedStorageUnit();
                else
                    RunBlueStorageUnit();
                break;
        }
    }


    // Warehouse Code
    private void RunWarehouse() {
       robot.drive.DriveByInches(0.5, -37);
    }

    private void Strafe(double power, double time) {
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

    private void sleep(int millis) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opModeIsActive()) {
            if(timer.milliseconds() >= millis) break;
        }
    }

    // Storage Unit Code (compatible for both sides with the 'movementModifier')
    private void RunRedStorageUnit() {
        final double startAngle = imuController.GetZAxis();

        robot.drive.StrafeInches(0.25, 14.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.25, 10);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, 45, AbstractDrive.TurnDirection.CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.25, -10.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        turnTableSpinner.setPower(1);
        sleep(2500);
        turnTableSpinner.setPower(0);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.25, 10.0);

        // Calculate Turn Distance
        final double turnDistance = imuController.GetZAxis() - startAngle;

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

            if(sideDistanceSensor.getDistance(DistanceUnit.MM) < 59.85 ||
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
            liftController.GoToStage(LiftController.LiftStage.STAGE_1);
        } else if(distanceDriven < 495) {
            // Lift Height 3
            liftController.GoToStage(LiftController.LiftStage.STAGE_3);
        } else {
            // Lift Height 2
            liftController.GoToStage(LiftController.LiftStage.STAGE_2);
        }

        robot.drive.DriveByInches(0.30, -9.85);

        // Dump the cup
        liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(600);
        liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
        sleep(500);

        // Back up away from shipping hub
        robot.drive.DriveByInches(0.50, 11.25 + 1.50);
        sleep(SLEEP_AMOUNT_MILLIS);

        liftController.ZeroLift();
        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.50, degreesToShippingHub, AbstractDrive.TurnDirection.CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.5, 32);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.StrafeInches(0.50, 14.25);

    }

    private void RunBlueStorageUnit() {
        final double startAngle = imuController.GetZAxis();

        // Strafe off wall
        Strafe(-0.25, 1.20);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Drive To wall
        robot.drive.DriveByInches(0.25, -20);

        // Strafe into duck spinner
        sleep(SLEEP_AMOUNT_MILLIS);
        Strafe(0.15, 1.0);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Spin Turn-Table
        turnTableSpinner.setPower(-1);
        sleep(2500);
        turnTableSpinner.setPower(0);

        // Strafe away from turn-table
        sleep(SLEEP_AMOUNT_MILLIS);
        Strafe(-0.25, 0.70);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Straighten out into wall
        robot.drive.DriveByInches(0.25, -4.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        final int distanceToShippingHub = 1700;
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

            if(sideDistanceSensor.getDistance(DistanceUnit.MM) < 59.85 || distance >= distanceToShippingHub) {
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
            liftController.GoToStage(LiftController.LiftStage.STAGE_3);
        } else if(driveDistance > 1450) {
            // Position 3
            liftController.GoToStage(LiftController.LiftStage.STAGE_1);
            telemetry.log().add("Lift Position: 1");
        } else {
            liftController.GoToStage(LiftController.LiftStage.STAGE_2);
            telemetry.log().add("Lift Position: 2");
            // Position 2
        }


        sleep(SLEEP_AMOUNT_MILLIS);

        final double shippingHubDriveDistance = 11.0;

        // Drive into the shipping hub
        robot.drive.DriveByInches(0.25, -shippingHubDriveDistance);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Dump the cup
        liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(600);
        liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
        sleep(500);

        // Back up away from shipping hub
        robot.drive.DriveByInches(0.25, shippingHubDriveDistance + 1.50);
        sleep(SLEEP_AMOUNT_MILLIS);

        liftController.ZeroLift();
        sleep(SLEEP_AMOUNT_MILLIS);

        // Turn back
        robot.drive.TurnDegrees(0.25, 135, AbstractDrive.TurnDirection.CLOCKWISE);

        // Drive back to wall
        robot.drive.DriveInches(0.50, -distanceToShippingHub);

        // Strafe into the shipping hub tape
        Strafe(-0.25, 1.40);

    }

    @Override
    public void OnStop() {
        robot.drive.Stop();
    }
}
