package com.bravenatorsrobotics.freightfrenzy.autonomous.redstorageunit;

import com.bravenatorsrobotics.common.core.Robot;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.linearstatemachine.LinearState;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class RSUAbstractLinearState extends LinearState {

    protected final Auto auto;
    protected final Robot<MecanumDrive> robot;
    protected final Telemetry telemetry;
    protected final RedStorageUnitSequence redStorageUnitSequence;

    public RSUAbstractLinearState(String stateName, Auto auto, RedStorageUnitSequence redStorageUnitSequence) {
        super(stateName);

        this.auto = auto;
        this.robot = auto.GetRobot();
        this.telemetry = auto.telemetry;
        this.redStorageUnitSequence = redStorageUnitSequence;
    }

    public void sleep(int millis) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(auto.opModeIsActive()) {
            if(timer.milliseconds() >= millis) {
                break;
            }
        }
    }

}
