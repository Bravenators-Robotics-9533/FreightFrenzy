package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.bravenatorsrobotics.freightfrenzy.autonomousSequence.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomousSequence.BlueStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomousSequence.BlueWarehouseSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomousSequence.RedStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomousSequence.RedWarehouseSequence;
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
                if(config.allianceColor == Config.AllianceColor.RED) {
                    // Red Warehouse
                    sequence = new RedWarehouseSequence(this);
                } else {
                    // Blue Warehouse
                    sequence = new BlueWarehouseSequence(this);
                }
                break;
            case STORAGE_UNIT:
                if(config.allianceColor == Config.AllianceColor.RED) {
                    // Red Storage Unit
                    sequence = new RedStorageUnitSequence(this);
                } else {
                    // Blue Storage Unit
                    sequence = new BlueStorageUnitSequence(this);
                }
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
