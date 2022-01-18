package com.bravenatorsrobotics.freightfrenzy.autonomous;

import com.bravenatorsrobotics.freightfrenzy.Auto;

public class WarehouseSequence extends AbstractAutonomousSequence {

    public WarehouseSequence(Auto auto) {
        super(auto);
    }

    @Override
    public void RunSequence() {
        robot.drive.DriveByInches(0.5, -37);
    }
}
