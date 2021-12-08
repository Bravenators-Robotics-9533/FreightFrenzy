package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.core.FtcGamePad;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.TeleopMode;
import com.bravenatorsrobotics.common.utils.PowerScale;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="Teleop")
public class Teleop extends TeleopMode<MecanumDrive> {

    private static final double REDUCE_SPEED_MULTIPLIER = 0.25;
    private static final double INTAKE_SPEED = 0.5;
    private static final double MAX_ROBOT_SPEED = 0.75;

    private Config config;

    private LiftController liftController;

    private DcMotorEx intake;
    private DcMotorEx turnTableSpinner;

    private boolean shouldOverrideSpeedReduction = false;
    private boolean shouldReduceSpeed = false;
    private boolean shouldReverse = false;

    private boolean shouldZeroLiftEncoder = false;
    private boolean shouldJuiceTheIntake = false;

    // TODO: Reimplement
//    private final PowerScale drivePowerScale = new PowerScale(this, 1.0 / 0.25);
//    private double currentV = 0.0;
//    private double currentH = 0.0;
//    private double currentR = 0.0;

    private boolean objectInCupToggle = false;

    private double turnTablePower = 1;

    // Create TeleopMode with specified specifications
    public Teleop() { super(new Specifications()); }

    @Override
    public void OnInitialize() {
        PrintControls();

        config = new Config(hardwareMap.appContext);

        liftController = new LiftController(this);

        // Reverse the turn table power if on red alliance
        if(config.allianceColor == Config.AllianceColor.BLUE)
            turnTablePower = -turnTablePower;

        intake = robot.GetMotor("intake", true);
        turnTableSpinner = robot.GetMotor("turnTable", false);
    }

    private void InitializeServos() {
        liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
    }

    @Override
    public void OnStart() {
        InitializeServos();
        liftController.ZeroLift();

        telemetry.clearAll();
    }

    @Override
    public void OnUpdate() {

        if(shouldZeroLiftEncoder && liftController.IsLiftSensorPressed()) {
            liftController.SetPower(0);
            liftController.GetLiftMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftController.GetLiftMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shouldZeroLiftEncoder = false;
        }

        // Detect Cup
        if(liftController.IsObjectInCup() && !objectInCupToggle) {
            objectInCupToggle = true;
            liftController.SetCupPosition(LiftController.CupPosition.TILTED_POSITION);
        } else if(objectInCupToggle && !liftController.IsObjectInCup()) {
            objectInCupToggle = false;
        }

        HandleGamePadDrive();

        // Log Important Information
        telemetry.addData("Is Lift Button Pressed", liftController.IsLiftSensorPressed());
        telemetry.addData("Is Reversed", shouldReverse);
        telemetry.addData("Should Reduce Mode", shouldReduceSpeed);
        telemetry.update();
    }

    @Override
    public void OnStop() {
        robot.Stop();
    }

    private void HandleGamePadDrive() {
        double v = -Math.pow(gamepad1.left_stick_y, 3);
        double h = -Math.pow(gamepad1.left_stick_x, 3) + Math.pow(driverGamePad.getLeftTrigger(), 3)
                - Math.pow(driverGamePad.getRightTrigger(), 3);
        double r = Math.pow(gamepad1.right_stick_x, 3);

        if(shouldReverse) {
            v = -v;
            h = -h;
        }

        if(shouldReduceSpeed && !shouldOverrideSpeedReduction) {
            v *= REDUCE_SPEED_MULTIPLIER;
            h *= REDUCE_SPEED_MULTIPLIER;
            r *= REDUCE_SPEED_MULTIPLIER;
        }

        // Scale power to maximum speed
        v = Range.clip(v, -MAX_ROBOT_SPEED, MAX_ROBOT_SPEED);
        h = Range.clip(h, -MAX_ROBOT_SPEED, MAX_ROBOT_SPEED);
        r = Range.clip(r, -MAX_ROBOT_SPEED, MAX_ROBOT_SPEED);

        super.robot.drive.Drive(v, h, r);
    }

    @Override
    protected void OnDriverGamePadChange(FtcGamePad gamePad, int button, boolean pressed) {
        switch (button) {
            case FtcGamePad.GAMEPAD_BACK:
                if(pressed) shouldOverrideSpeedReduction = !shouldOverrideSpeedReduction;
                break;

            case FtcGamePad.GAMEPAD_LBUMPER: // Slow Down
                if(pressed)
                    shouldReduceSpeed = !shouldReduceSpeed;
                break;

            case FtcGamePad.GAMEPAD_RBUMPER: // Reverse
                if(pressed)
                    shouldReverse = !shouldReverse;
                break;
        }
    }

    @Override
    protected void OnOperatorGamePadChange(FtcGamePad gamePad, int button, boolean pressed) {
        switch (button) {

            // Turntable Direction Override
            case FtcGamePad.GAMEPAD_BACK:
                if (pressed) {
                    turnTablePower = -turnTablePower;
                }

                break;

            case FtcGamePad.GAMEPAD_RBUMPER:
                if (pressed) {
                    shouldJuiceTheIntake = !shouldJuiceTheIntake;
                }

                break;

            // Turntable Spinner
            case FtcGamePad.GAMEPAD_X:

                if (pressed) {
                    turnTableSpinner.setPower(turnTablePower);
                } else {
                    turnTableSpinner.setPower(0);
                }

                break;

            // Intake
            case FtcGamePad.GAMEPAD_A:
                if (pressed) {
                    intake.setPower(shouldJuiceTheIntake ? 1 : INTAKE_SPEED);
                } else {
                    intake.setPower(0);
                }

                break;

            // Reverse Intake
            case FtcGamePad.GAMEPAD_Y:

                if (pressed) {
                    intake.setPower(shouldJuiceTheIntake ? -1 : -INTAKE_SPEED);
                } else {
                    intake.setPower(0);
                }

                break;

            // Toggle Cup
            case FtcGamePad.GAMEPAD_B:

                if (pressed) {
                    liftController.SetCupPosition(LiftController.CupPosition.DUMPED_POSITION);
                } else {
                    liftController.SetCupPosition(LiftController.CupPosition.INTAKE_POSITION);
                }

                break;

            // Automatic Lift Down
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if (pressed) {
                    liftController.SetPower(-LiftController.LIFT_POWER);
                    shouldZeroLiftEncoder = true;
                }

                break;

            // Automatic Lift Position 1
            case FtcGamePad.GAMEPAD_DPAD_LEFT:
                if (pressed) {
                    liftController.GoToStage(LiftController.LiftStage.STAGE_1);
                }

                break;

            // Automatic Lift Position 2
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if (pressed) {
                    liftController.GoToStage(LiftController.LiftStage.STAGE_2);
                }

                break;

            // Automatic Lift Position 3
            case FtcGamePad.GAMEPAD_DPAD_RIGHT:
                if (pressed) {
                    liftController.GoToStage(LiftController.LiftStage.STAGE_3);
                }

                break;
        }
    }

    private void PrintControls() {
        telemetry.log().add("OPERATOR CONTROLS");
        telemetry.log().add("(BACK) Reverse Turn-Table Direction");
        telemetry.log().add("(X) Turn-Table");
        telemetry.log().add("(A) Intake");
        telemetry.log().add("(Y) Reverse Intake");
        telemetry.log().add("(B) Toggle Cup Position");
        telemetry.log().add("(D-Pad Down) Lift Down");
        telemetry.log().add("(D-Pad Left) Lift First Position");
        telemetry.log().add("(D-Pad Up) Lift Second Position");
        telemetry.log().add("(D-Pad Right) Lift Third Position");

        telemetry.update();
    }

}
