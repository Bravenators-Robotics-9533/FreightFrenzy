package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.freightfrenzy.Auto;
import com.bravenatorsrobotics.freightfrenzy.Config;

public class WarehouseSequence extends AbstractAutonomousSequence {

    public WarehouseSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {
        if(auto.config.allianceColor == Config.AllianceColor.RED) {
            robot.drive.StrafeInches(0.5, 48);
        } else {
            robot.drive.StrafeInches(0.5, -48);
        }

        robot.drive.DriveByInches(0.5, 24);
    }
}
