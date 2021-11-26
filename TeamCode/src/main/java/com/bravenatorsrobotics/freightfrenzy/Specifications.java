package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.core.RobotSpecifications;
import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Specifications extends RobotSpecifications {

    public Specifications() {
        super(MecanumDrive.GenerateMotors(
                "fl", false,
                "fr", true,
                "bl", false,
                "br", true
                ), MecanumDrive.class,
                560, 1, 3.70, (16.45 + 11.50 / 2.0));

        this.useVelocity = true;
        this.maxVelocity = 2800;
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
        this.shouldTunePIDF = false;
        this.debugModeEnabled = true;
    }

}