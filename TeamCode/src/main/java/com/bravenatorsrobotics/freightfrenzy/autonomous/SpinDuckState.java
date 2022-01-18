package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.common.linearstatemachine.LinearState;
import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptI2cAddressChange;

public class SpinDuckState extends LinearState {

    public static final int DUCK_SPIN_TIME_MILLIS = 2000;

    private final Auto auto;

    private final double duckSpinPower;

    public SpinDuckState(Auto auto) {
        super("Spin Duck");

        this.auto = auto;

        Config config = new Config(auto.hardwareMap.appContext);
        this.duckSpinPower = config.allianceColor == Config.AllianceColor.RED ? 1 : -1;
    }

    @Override
    public void RunState() {
        auto.turnTableSpinner.setPower(duckSpinPower);
        sleep(DUCK_SPIN_TIME_MILLIS);
        auto.turnTableSpinner.setPower(0);
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
