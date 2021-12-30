package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;
import com.bravenatorsrobotics.freightfrenzy.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LiftController extends AbstractController {

    private static final double CUP_OBJECT_THRESHOLD_CM = 6.0; // CM
    public static final double LIFT_POWER = 0.50;

    private final DcMotorEx liftMotor;
    private final TouchSensor liftTouchSensor;
    private final Servo cupServo;
    private final RevColorSensorV3 cupDistanceSensor;

    public enum LiftStage {
        STAGE_1,
        STAGE_2,
        STAGE_3;

        private int liftPosition;

        public void SetLiftPosition(int position) { this.liftPosition = position; }
        public int GetLiftPosition() { return liftPosition; }
    }

    public enum CupPosition {
        DUMPED_POSITION(0),
        TILTED_POSITION(0.6),
        INTAKE_POSITION(1);

        public final double position;

        CupPosition(double position) {
            this.position = position;
        }
    }

    public LiftController(OperationMode<?> operationMode, Config config) {
        super(operationMode);

        liftMotor = operationMode.hardwareMap.get(DcMotorEx.class, "lift");
        liftMotor.setTargetPositionTolerance(1);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftTouchSensor = operationMode.hardwareMap.touchSensor.get("liftTouchSensor");

        cupServo = operationMode.hardwareMap.servo.get("cupServo");

        cupDistanceSensor = operationMode.hardwareMap.get(RevColorSensorV3.class, "cupDistanceSensor");

        LiftStage.STAGE_1.SetLiftPosition(config.liftStage1Position);
        LiftStage.STAGE_2.SetLiftPosition(config.liftStage2Position);
        LiftStage.STAGE_3.SetLiftPosition(config.liftStage3Position);
    }

    public void RunToPosition(int position) {
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(LIFT_POWER);
    }

    public void SetPower(double power) {
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setPower(power);
    }

    public void GoToStage(LiftStage liftStage) {
        this.RunToPosition(liftStage.liftPosition);
    }

    public void ZeroLift() {
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(!liftTouchSensor.isPressed())
            liftMotor.setPower(-LIFT_POWER / 2.0);
        else
            return;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(!liftTouchSensor.isPressed() && timer.seconds() <= 1.5) { // Time out after 1.5 seconds in case lift gets stuck
            if(!operationMode.opModeIsActive()) break;
        }

        liftMotor.setPower(0);

        if(liftTouchSensor.isPressed()) { // Only reset if the lift actually hit the button
            liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void SetCupPosition(CupPosition cupPosition) {
        cupServo.setPosition(cupPosition.position);
    }

    public boolean IsObjectInCup() {
        double distance = cupDistanceSensor.getDistance(DistanceUnit.CM);
        return (distance < CUP_OBJECT_THRESHOLD_CM) || (cupDistanceSensor.getLightDetected() > 0.1);
    }

    public DcMotorEx GetLiftMotor() { return liftMotor; }
    public boolean IsLiftSensorPressed() { return liftTouchSensor.isPressed(); }
}
