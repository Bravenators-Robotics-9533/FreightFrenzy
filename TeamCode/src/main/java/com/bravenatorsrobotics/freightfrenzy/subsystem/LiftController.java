package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class LiftController extends AbstractSubSystemController {

    public enum LiftStage {
        STAGE_1(45),
        STAGE_2(185),
        STAGE_3(357);

        public final int liftPosition;

        LiftStage(int liftPosition) {
            this.liftPosition = liftPosition;
        }
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

    public static final double LIFT_POWER = 0.50;

    private final DcMotorEx liftMotor;
    private final TouchSensor liftTouchSensor;
    private final Servo cupServo;

    public LiftController(OperationMode<?> operationMode) {
        super(operationMode);

        liftMotor = operationMode.hardwareMap.get(DcMotorEx.class, "lift");
        liftMotor.setTargetPositionTolerance(1);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftTouchSensor = operationMode.hardwareMap.touchSensor.get("liftTouchSensor");

        cupServo = operationMode.hardwareMap.servo.get("cupServo");
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
        if(!liftTouchSensor.isPressed())
            liftMotor.setPower(-0.1);

        while(!liftTouchSensor.isPressed()) {
            if(!operationMode.opModeIsActive()) break;
        }

        liftMotor.setPower(0);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void SetCupPosition(CupPosition cupPosition) {
        cupServo.setPosition(cupPosition.position);
    }

    public DcMotorEx GetLiftMotor() { return liftMotor; }
    public boolean IsLiftSensorPressed() { return liftTouchSensor.isPressed(); }
}
