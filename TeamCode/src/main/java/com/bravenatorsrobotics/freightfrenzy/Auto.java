package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.bravenatorsrobotics.common.vision.TensorFlowObjectDetector;
import com.bravenatorsrobotics.freightfrenzy.subsystem.IMUController;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

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

    }

    @Override
    public void OnStart() {

        // 1 if on blue alliance -1 if on red alliance
        int movementModifier = config.allianceColor == Config.AllianceColor.BLUE ? 1 : -1;

        sleep(SLEEP_AMOUNT_MILLIS);

        // Call the appropriate method and pass in the movement modifier
        switch (config.startingPosition) {
            case WAREHOUSE:
                RunWarehouse(movementModifier);
                break;
            case STORAGE_UNIT:
                RunStorageUnit(movementModifier);
                break;
        }
    }


    // Warehouse Code (compatible for both sides with the 'movementModifier')
    private void RunWarehouse(int movementModifier) {
        // Drive to the alliance shipping hub

        // Deliver the preloaded block (make sure the robot has this)

        // Strafe and park fully in the warehouse
    }

    // Storage Unit Code (compatible for both sides with the 'movementModifier')
    private void RunStorageUnit(int movementModifier) {
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

        robot.drive.TurnDegrees(0.25, 90, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);

        sleep(SLEEP_AMOUNT_MILLIS);

        // Strafe to the block

        timer.reset();

        robot.drive.Drive(0, -0.25, 0);

        while(timer.seconds() < 0.94) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.Drive(-0.10, 0, 0);

        final int distanceToShippingHub = 1415;
        int startTicks = robot.GetDriveMotors()[0].getCurrentPosition();

        while(opModeIsActive()) {
            double distance = sideDistanceSensor.getDistance(DistanceUnit.MM);

            telemetry.addData("Distance Sensor", distance);
            telemetry.update();

            if(distance < 59.85 || Math.abs(robot.GetDriveMotors()[0].getCurrentPosition()) >= distanceToShippingHub)
                break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        final int driveDifference = Math.abs(robot.GetDriveMotors()[0].getCurrentPosition() - startTicks);

        final int distanceToDrive = distanceToShippingHub - driveDifference;

        robot.drive.DriveByEncoders(0.25, -distanceToDrive); // Negate

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, 55, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE); // Turn to shipping hub

        sleep(SLEEP_AMOUNT_MILLIS);

        if(driveDifference < 550) {
            // Position 1
            telemetry.log().add("Position 1");
            liftController.GoToStage(LiftController.LiftStage.STAGE_1);
        } else if(driveDifference > 800) {
            // Position 3
            telemetry.log().add("Position 3");
            liftController.GoToStage(LiftController.LiftStage.STAGE_3);
        } else {
            // Position 2
            telemetry.log().add("Position 2");
            liftController.GoToStage(LiftController.LiftStage.STAGE_2);
        }

        robot.drive.DriveByInches(0.15, -5.0);

        while(liftController.GetLiftMotor().isBusy()) {
            if(!opModeIsActive())
                break;
        }

        // TODO: Drop the block

        liftController.ZeroLift();

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.15, 15.0);

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.TurnDegrees(0.25, 55, AbstractDrive.TurnDirection.CLOCKWISE); // Turn to shipping hub

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.Drive(0, 0.25, 0);

        while(timer.seconds() < 0.94) { // Strafe Seconds
            if(!opModeIsActive()) break;
        }

        robot.drive.Stop();

        sleep(SLEEP_AMOUNT_MILLIS);

        robot.drive.DriveByInches(0.5, -90);



    }

    @Override
    public void OnStop() {
        robot.drive.Stop();
    }
}
