package com.bravenatorsrobotics.freightfrenzy.autonomousSequence;

import com.bravenatorsrobotics.common.core.Robot;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class AbstractAutonomousSequence {

    public static final int SLEEP_AMOUNT_MILLIS = 20;

    protected final Auto auto;
    protected final Robot<MecanumDrive> robot;
    protected final Telemetry telemetry;

    public AbstractAutonomousSequence(Auto auto) {
        this.auto = auto;
        this.robot = auto.GetRobot();
        this.telemetry = auto.telemetry;
    }

    public abstract boolean RunSequence();

    public void sleep(int millis) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opModeIsActive()) {
            if(timer.milliseconds() >= millis) {
                break;
            }
        }
    }

    public boolean opModeIsActive() { return auto.opModeIsActive(); }

}
