package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.core.RobotSpecifications;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="StrafeTest")
public class StrafeTest extends AutonomousMode<MecanumDrive> {

    public StrafeTest() {
        super(new Specifications());
    }

    @Override
    public void OnInitialize() {

    }

    @Override
    public void OnStart() {
        robot.drive.Strafe(24);

        while(opModeIsActive()) {
            sleep(10);
        }
    }

    @Override
    public void OnStop() {

    }
}
