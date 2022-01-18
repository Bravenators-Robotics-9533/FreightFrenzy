package com.bravenatorsrobotics.common.linearstatemachine;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinearStateMachine {

    private final Telemetry telemetry;
    private final LinearState[] linearStates;

    public LinearStateMachine(Telemetry telemetry, LinearState[] linearStates) {
        this.telemetry = telemetry;
        this.linearStates = linearStates;
    }

    public void RunAllStates() {
        for (LinearState linearState : linearStates) {
            telemetry.addData("Current State", linearState.getClass().getSimpleName());
            telemetry.update();

            linearState.RunState();
        }
    }

}
