package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.drive.MecanumDrive;
import com.bravenatorsrobotics.common.operation.AutonomousMode;
import com.bravenatorsrobotics.freightfrenzy.autonomous.AbstractAutonomousSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.BlueStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.RedStorageUnitSequence;
import com.bravenatorsrobotics.freightfrenzy.autonomous.WarehouseSequence;
import com.bravenatorsrobotics.freightfrenzy.subsystem.IMUController;
import com.bravenatorsrobotics.freightfrenzy.subsystem.LiftController;
import com.bravenatorsrobotics.freightfrenzy.subsystem.vision.AllianceShippingElementPipeline;
import com.bravenatorsrobotics.freightfrenzy.subsystem.vision.VisionController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="Autonomous")
public class Auto extends AutonomousMode<MecanumDrive> {

    public Config config;

    public LiftController liftController;
    public IMUController imuController;

    private VisionController visionController;
    private AllianceShippingElementPipeline visionPipeline;

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
        DetectAllianceShippingElement();
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

    private void DetectAllianceShippingElement() {
        visionPipeline = new AllianceShippingElementPipeline(telemetry);

        visionController = new VisionController(this);
        visionController.OpenCameraToPipeline(visionPipeline);
    }

    @Override
    public void OnStart() {
        telemetry.addData("Shipping Element Location", visionPipeline.GetAllianceShippingElementLocation().name());
        telemetry.update();



        visionController.CloseCamera(); // Stop using CPU resources

        // Run the pre-selected sequence
        sequence.RunSequence();
    }

    @Override
    public void OnStop() {
        robot.Stop(); // Force stop
    }
}
