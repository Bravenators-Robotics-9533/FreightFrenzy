package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;
import com.bravenatorsrobotics.freightfrenzy.Teleop;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class AbstractController {

    protected OperationMode<?> operationMode;
    protected Telemetry telemetry;

    public AbstractController(OperationMode<?> operationMode) {
        this.operationMode = operationMode;
        this.telemetry = operationMode.telemetry;
    }

}
