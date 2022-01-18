package com.bravenatorsrobotics.freightfrenzy.autonomous.redstorageunit;

import com.bravenatorsrobotics.common.drive.AbstractDrive;
import com.bravenatorsrobotics.freightfrenzy.Auto;

public class RSUDriveToDuckState extends RSUAbstractLinearState {

    public RSUDriveToDuckState(Auto auto, RedStorageUnitSequence redStorageUnitSequence) {
        super("Drive To Duck", auto, redStorageUnitSequence);
    }

    @Override
    public void RunState() {
        // Save the start angle
        redStorageUnitSequence.SetStartAngle(auto.imuController.GetZAxis());

        // Strafe away from the wall
        robot.drive.StrafeInches(RedStorageUnitSequence.ROBOT_SPEED, 14.0);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);

        // Drive forward towards the duck spinner
        robot.drive.DriveByInches(RedStorageUnitSequence.ROBOT_SPEED, 11);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);

        // Turn to line up the compliant wheels with the spinner
        robot.drive.TurnDegrees(RedStorageUnitSequence.ROBOT_SPEED, 45, AbstractDrive.TurnDirection.CLOCKWISE);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);

        // Strafe into the duck spinner
        robot.drive.StrafeInches(RedStorageUnitSequence.ROBOT_SPEED, -10.0);
        sleep(RedStorageUnitSequence.SLEEP_AMOUNT_MILLIS);
    }
}
