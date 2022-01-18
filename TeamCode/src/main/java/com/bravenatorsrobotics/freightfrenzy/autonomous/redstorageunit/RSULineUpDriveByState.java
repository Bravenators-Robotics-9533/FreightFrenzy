package com.bravenatorsrobotics.freightfrenzy.autonomous.redstorageunit;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;

public class RSULineUpDriveByState extends RSUAbstractLinearState {

    public RSULineUpDriveByState(Auto auto, RedStorageUnitSequence redStorageUnitSequence) {
        super("Line Up Drive-By", auto, redStorageUnitSequence);
    }

    @Override
    public void RunState() {
        // Strafe Away From The Duck Spinner
        robot.drive.StrafeInches(0.25, 10.0);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);

        // Calculate Turn Distance
        final double turnDistance = auto.imuController.GetZAxis() - redStorageUnitSequence.GetStartAngle();

        // Turn back to the starting value
        robot.drive.TurnDegrees(RedStorageUnitSequence.ROBOT_SPEED, (int) turnDistance, AbstractDrive.TurnDirection.COUNTER_CLOCKWISE);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);

        // Strafe towards the alliance capping stone
        robot.drive.StrafeInches(RedStorageUnitSequence.ROBOT_SPEED, 3.0);
    }
}
