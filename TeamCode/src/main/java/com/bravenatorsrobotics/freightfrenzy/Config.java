package com.bravenatorsrobotics.freightfrenzy;

import android.content.Context;
import android.content.SharedPreferences;

import com.bravenatorsrobotics.common.config.AbstractConfig;

import java.util.ArrayList;

public class Config extends AbstractConfig {

    private static final String PREFERENCES = "BravenatorsConfig";

    public Config(Context context) {
        super(PREFERENCES, context);
    }

    // Alliance Color
    public static final String ALLIANCE_COLOR = "Alliance Color";
    public enum AllianceColor { RED, BLUE }
    private AllianceColor allianceColor;
    public AllianceColor GetAllianceColor() { return this.allianceColor; }
    public void SetAllianceColor(AllianceColor allianceColor) { this.allianceColor = allianceColor; }

    // Starting Position
    public static final String STARTING_POSITION = "Starting Position";
    public enum StartingPosition { STORAGE_UNIT, WAREHOUSE }
    private StartingPosition startingPosition;
    public StartingPosition GetStartingPosition() { return this.startingPosition; }
    public void SetStartingPosition(StartingPosition startingPosition) { this.startingPosition = startingPosition; }

    // Should Join Gamepads
    public static final String SHOULD_JOIN_GAMEPADS = "Should Join Gamepads";
    private boolean shouldJoinGamepads = false;
    public boolean ShouldJoinGamepads() { return this.shouldJoinGamepads; }
    public void SetShouldJoinGamepads(boolean value) { this.shouldJoinGamepads = value; }

    // Lift Stages
    public static final String LIFT_STAGE_1_POSITION = "Lift Stage 1 Position";
    private int liftStage1Position = 200;
    public int GetLiftStage1Position() { return this.liftStage1Position; }
    public void SetLiftStage1Position(int value) { this.liftStage1Position = value; }

    public static final String LIFT_STAGE_2_POSITION = "Lift Stage 2 Position";
    private int liftStage2Position = 400;
    public int GetLiftStage2Position() { return this.liftStage2Position; }
    public void SetLiftStage2Position(int value) { this.liftStage2Position = value; }

    public static final String LIFT_STAGE_3_POSITION = "Lift Stage 3 Position";
    private int liftStage3Position = 600;
    public int GetLiftStage3Position() { return this.liftStage3Position; }
    public void SetLiftStage3Position(int value) { this.liftStage3Position = value; }

    @Override
    protected void PutConfigs() {
        super.PutEnum(STARTING_POSITION, startingPosition);
        super.PutEnum(ALLIANCE_COLOR, allianceColor);

        super.PutBoolean(SHOULD_JOIN_GAMEPADS, shouldJoinGamepads);

        super.PutInteger(LIFT_STAGE_1_POSITION, liftStage1Position);
        super.PutInteger(LIFT_STAGE_2_POSITION, liftStage2Position);
        super.PutInteger(LIFT_STAGE_3_POSITION, liftStage3Position);
    }

    @Override
    protected void GetConfigs() {
        this.startingPosition = super.GetEnum(STARTING_POSITION, startingPosition);
        this.allianceColor = super.GetEnum(ALLIANCE_COLOR, allianceColor);

        this.shouldJoinGamepads = super.GetBoolean(SHOULD_JOIN_GAMEPADS, shouldJoinGamepads);

        this.liftStage1Position = super.GetInteger(LIFT_STAGE_1_POSITION, liftStage1Position);
        this.liftStage2Position = super.GetInteger(LIFT_STAGE_2_POSITION, liftStage2Position);
        this.liftStage3Position = super.GetInteger(LIFT_STAGE_3_POSITION, liftStage3Position);
    }

}
