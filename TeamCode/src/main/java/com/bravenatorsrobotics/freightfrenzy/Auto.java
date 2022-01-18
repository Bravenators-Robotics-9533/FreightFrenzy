package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.bravenatorsrobotics.freightfrenzy.autonomous.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.BlueStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.redstorageunit.RedStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.WarehouseSequence;
import com.bravenatorsrobotics.freightfrenzy.subsystem.IMUController;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="Autonomous")
public class Auto extends AutonomousMode<MecanumDrive> {

    public Config config;

    public LiftController liftController;
    public IMUController imuController;

    public DcMotorEx turnTableSpinner;

    public RevColorSensorV3 sideDistanceSensor;

    private AbstractAutonomousSequence sequence = null;

    public Auto() { super(new Specifications()); }

    @Override
    public void OnInitialize() {
        config = new Config(hardwareMap.appContext); // Get the saved configuration for later

        imuController = new IMUController(this);
        imuController.Initialize();

        liftController = new LiftController(this);

        turnTableSpinner = robot.GetMotor("turnTable", false);
        sideDistanceSensor = hardwareMap.get(RevColorSensorV3.class, "sideDistanceSensor");

        liftController.SetCupPosition(LiftController.CupPosition.TILTED_POSITION);

        AssignAutonomousSequence();
    }

    private void AssignAutonomousSequence() {
        // Create the appropriate sequence
        switch (config.startingPosition) {
            case WAREHOUSE:
                sequence = new WarehouseSequence(this);
                break;
            case STORAGE_UNIT:
                if(config.allianceColor == Config.AllianceColor.RED)
                    sequence = new RedStorageUnitSequence(this);
                else
                    sequence = new BlueStorageUnitSequence(this);
                break;
        }
    }

    @Override
    public void OnStart() {
        // Run the pre-selected sequence
        sequence.RunSequence();
    }

    @Override
    public void OnStop() {
        robot.Stop(); // Force stop
    }
}
