package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMUController extends AbstractController {

    private final BNO055IMU.Parameters parameters;

    private final BNO055IMU imu;

    public IMUController(OperationMode<?> operationMode) {
        super(operationMode);

        parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = operationMode.hardwareMap.get(BNO055IMU.class, "imu");
    }

    public void Initialize() {
        imu.initialize(parameters);
    }

    public double GetZAxis() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return angles.firstAngle;
    }

    public void DebugOutput() {


        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        operationMode.telemetry.addData("Z", angles.firstAngle);
        operationMode.telemetry.addData("Y", angles.secondAngle);
        operationMode.telemetry.addData("Z", angles.thirdAngle);
        operationMode.telemetry.update();

    }

}
