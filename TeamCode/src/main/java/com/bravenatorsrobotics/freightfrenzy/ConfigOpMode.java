package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.core.SimpleMenu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Configuration", group="Config")
public class ConfigOpMode extends LinearOpMode {

    private static final SimpleMenu menu = new SimpleMenu("Configuration Menu");

    @Override
    public void runOpMode() {
        Config config = new Config(hardwareMap.appContext);

        menu.clearOptions();

        menu.addOption("Alliance Color", Config.AllianceColor.class, config.allianceColor);
        menu.addOption("Starting Position", Config.StartingPosition.class, config.startingPosition);

        menu.addOption("Lift Stage 1 Position", 1000, 0, 10, config.liftStage1Position);
        menu.addOption("Lift Stage 2 Position", 1000, 0, 10, config.liftStage2Position);
        menu.addOption("Lift Stage 3 Position", 1000, 0, 10, config.liftStage3Position);

        menu.setGamepad(gamepad1);
        menu.setTelemetry(telemetry);

        waitForStart();

        while(opModeIsActive()) {
            menu.displayMenu();

            switch (menu.getCurrentChoiceOf("Alliance Color")) {
                case "RED":
                    config.allianceColor = Config.AllianceColor.RED;
                    break;
                case "BLUE":
                    config.allianceColor = Config.AllianceColor.BLUE;
                    break;
            }

            switch (menu.getCurrentChoiceOf("Starting Position")) {
                case "STORAGE_UNIT":
                    config.startingPosition = Config.StartingPosition.STORAGE_UNIT;
                    break;
                case "WAREHOUSE":
                    config.startingPosition = Config.StartingPosition.WAREHOUSE;
                    break;
            }

            config.liftStage1Position = Integer.parseInt(menu.getCurrentChoiceOf("Lift Stage 1 Position"));
            config.liftStage2Position = Integer.parseInt(menu.getCurrentChoiceOf("Lift Stage 2 Position"));
            config.liftStage3Position = Integer.parseInt(menu.getCurrentChoiceOf("Lift Stage 3 Position"));

            sleep(50);
        }

        config.Save();
    }

}
