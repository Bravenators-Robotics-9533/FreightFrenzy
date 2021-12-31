package com.bravenatorsrobotics.freightfrenzy;

import com.bravenatorsrobotics.common.config.ConfigurableBoolean;
import com.bravenatorsrobotics.common.config.ConfigurableEnum;
import com.bravenatorsrobotics.common.config.ConfigurableInteger;
import com.bravenatorsrobotics.common.config.ConfigurableItem;
import com.bravenatorsrobotics.common.core.SimpleMenu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Configuration", group="Config")
public class ConfigOpMode extends LinearOpMode {

    public final SimpleMenu menu = new SimpleMenu("Configuration Menu");

    @Override
    public void runOpMode() {
        Config config = new Config(hardwareMap.appContext);
        ConfigurableItem.SetMenuContext(menu);

        ConfigurableItem[] configurableItems = {
                new ConfigurableEnum<>(Config.ALLIANCE_COLOR, Config.AllianceColor.class, config.GetAllianceColor(), config::SetAllianceColor),
                new ConfigurableEnum<>(Config.STARTING_POSITION, Config.StartingPosition.class, config.GetStartingPosition(), config::SetStartingPosition),

                new ConfigurableBoolean(Config.SHOULD_JOIN_GAMEPADS, config.ShouldJoinGamepads(), config::SetShouldJoinGamepads),

                new ConfigurableInteger(Config.LIFT_STAGE_1_POSITION, 1000, 0, 10, config.GetLiftStage1Position(), config::SetLiftStage1Position),
                new ConfigurableInteger(Config.LIFT_STAGE_2_POSITION, 1000, 0, 10, config.GetLiftStage2Position(), config::SetLiftStage2Position),
                new ConfigurableInteger(Config.LIFT_STAGE_3_POSITION, 1000, 0, 10, config.GetLiftStage3Position(), config::SetLiftStage3Position)
        };

        menu.clearOptions();

        for(ConfigurableItem item : configurableItems)
            item.RunOptionRunnable();

        menu.setGamepad(gamepad1);
        menu.setTelemetry(telemetry);

        waitForStart();

        while(opModeIsActive()) {
            menu.displayMenu();

            for(ConfigurableItem item : configurableItems) {
                item.RunSaveRunnable();
            }

            sleep(50);
        }

        config.Save();
    }

}
