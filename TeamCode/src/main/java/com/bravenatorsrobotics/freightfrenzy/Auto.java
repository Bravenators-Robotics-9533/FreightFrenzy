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


    // Warehouse Code (compatible for both sides with the 'movementModifier')
    private void RunWarehouse() {
        // Drive to the alliance shipping hub

        // Deliver the preloaded block (make sure the robot has this)

        // Strafe and park fully in the warehouse
    }

    private void Strafe(double power, double time) {
        ElapsedTime timer = new ElapsedTime();

        robot.drive.Drive(0, power, 0);

        while(opModeIsActive()) {
            if(timer.seconds() >= time) {
                robot.drive.Stop();
                break;
            }
        }
    }

    // Storage Unit Code (compatible for both sides with the 'movementModifier')
    private void RunRedStorageUnit() {
        final double startAngle = imuController.GetZAxis();

        // Drive to the turn table

        // Drive off the wall
        robot.drive.DriveByInches(0.25, 5);

        sleep(SLEEP_AMOUNT_MILLIS);

        // TODO: Use Encoders
        // Strafe to the turn-table
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        robot.drive.Drive(0, 0.25, 0);

        while(timer.seconds() < 1.75) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        // Turn Towards Turn-Table
        // TODO: Reverse
        robot.drive.TurnDegrees(0.25, 15, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Strafe into turn-table
        robot.drive.Drive(0, 0.05, 0);
        timer.reset();
        while(timer.seconds() < 0.01) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }
        robot.drive.Stop();
        robot.drive.Drive(0, 0, 0);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Spin the turn-table
        // TODO: Use the config
        turnTableSpinner.setPower(1);

        timer.reset();

        while(timer.seconds() < 2.5) { // Turn-Table
            if(!opModeIsActive()) break;
        }

        turnTableSpinner.setPower(0);

        sleep(SLEEP_AMOUNT_MILLIS);

        // Strafe away from turn-table
        timer.reset();

        robot.drive.Drive(0, -0.25, 0);

        while(timer.seconds() < 0.50) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        final double imuCorrection = imuController.GetZAxis() - startAngle;

        robot.drive.TurnDegrees(0.25, (int) Math.abs(imuCorrection) + 5, // 5 degree correction
                imuCorrection > 0 ? AbstractDrive.TurnDirection.CLOCKWISE : AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        robot.drive.TurnDegrees(0.45, 90, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        // Strafe to the block
        timer.reset();

        robot.drive.Drive(0, -0.25, 0);

        while(timer.seconds() < 1.05) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.Drive(-0.10, 0, 0);

        final int distanceToShippingHub = 1150;
        int startTicks = robot.GetDriveMotors()[0].getCurrentPosition();

        while(opModeIsActive()) {
            double distance = sideDistanceSensor.getDistance(DistanceUnit.MM);

            telemetry.addData("Distance Sensor", distance);
            telemetry.update();

            if(distance < 59.85 || Math.abs(robot.GetDriveMotors()[0].getCurrentPosition() - startTicks) >= distanceToShippingHub)
                break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        final int driveDifference = Math.abs(robot.GetDriveMotors()[0].getCurrentPosition() - startTicks);

        final int distanceToDrive = distanceToShippingHub - driveDifference;

        robot.drive.DriveByEncoders(0.25, -distanceToDrive); // Negate

        sleep(SLEEP_AMOUNT_MILLIS);

        int degreesTowardsShippingHub = 45;
        boolean isHeight3 = false;

        if(driveDifference < 550) {
            // Position 1
            telemetry.log().add("Position 1");
            liftController.GoToStage(LiftController.LiftStage.STAGE_1);
        } else if(driveDifference > 800) {
            // Position 3
            telemetry.log().add("Position 3");
            liftController.GoToStage(LiftController.LiftStage.STAGE_3);
            isHeight3 = true;
        } else {
            // Position 2
            telemetry.log().add("Position 2");
            liftController.GoToStage(LiftController.LiftStage.STAGE_2);
            degreesTowardsShippingHub -= 5;
        }

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.50, degreesTowardsShippingHub, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE); // Turn to shipping hub

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.15, !isHeight3 ? -4.75 : -5.20); // Drive to shipping hub

        timer.reset();

        while(liftController.GetLiftMotor().isBusy() && timer.seconds() < 1.25) {
            if(!opModeIsActive())
                break;
        }

        // Drop the Block
        liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
        sleep(600);
        liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
        sleep(500);
        liftController.ZeroLift();
        sleep(SLEEP_AMOUNT_MILLIS);

        // Back up away from goal
        robot.drive.DriveByInches(0.50, 12.5);
        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, degreesTowardsShippingHub, AbstractDrive.TurnDirection.CLOCKWISE); // Turn to shipping hub

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.Drive(0, 0.25, 0);

        while(timer.seconds() < 0.94) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.5, -85);
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
        Strafe(-0.25, 0.65);
        sleep(SLEEP_AMOUNT_MILLIS);

        // Straighten out into wall
        robot.drive.DriveByInches(0.25, -4.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        final int startTicks = robot.GetDriveMotors()[0].getCurrentPosition();

        sleep(SLEEP_AMOUNT_MILLIS);

        // Drive past the blocks
        robot.drive.Drive(0.25, 0, 0);

        while(opModeIsActive()) {
            if(sideDistanceSensor.getDistance(DistanceUnit.MM) < 59.85) {
                telemetry.addData("Distance", robot.GetDriveMotors()[0].getCurrentPosition() - startTicks);
                telemetry.update();
                robot.Stop();
//                break;
            }
        }

    }

    @Override
    public void OnStop() {
        robot.drive.Stop();
    }
}
