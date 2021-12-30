package com.bravenatorsrobotics.freightfrenzy;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

        private static final String PREFERENCES = "BravenatorsConfig";
        private final SharedPreferences sharedPreferences;

        public Config(Context context) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Load();
        }

        public void Load() {
            allianceColor = AllianceColor.ToAllianceColor(sharedPreferences.getString(ALLIANCE_COLOR, "RED"));
            startingPosition = StartingPosition.ToStartingPosition(sharedPreferences.getString(STARTING_POSITION, "STORAGE_UNIT"));

            liftStage1Position = sharedPreferences.getInt(LIFT_STAGE_1_POSITION, liftStage1Position);
            liftStage2Position = sharedPreferences.getInt(LIFT_STAGE_2_POSITION, liftStage2Position);
            liftStage3Position = sharedPreferences.getInt(LIFT_STAGE_3_POSITION, liftStage3Position);
        }

        public void Save() {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(ALLIANCE_COLOR, allianceColor.name());
            editor.putString(STARTING_POSITION, startingPosition.name());

            // Lift Positions
            editor.putInt(LIFT_STAGE_1_POSITION, liftStage1Position);
            editor.putInt(LIFT_STAGE_2_POSITION, liftStage2Position);
            editor.putInt(LIFT_STAGE_3_POSITION, liftStage3Position);

            editor.apply();
        }

        // Alliance Color
        public static final String ALLIANCE_COLOR = "AllianceColor";
        public AllianceColor allianceColor;
        public enum AllianceColor {
            RED, BLUE;

            public static AllianceColor ToAllianceColor(String allianceColor) {
                try {
                    return valueOf(allianceColor);
                } catch(Exception e) {
                    return RED; // Default Alliance Color
                }
            }
        }

        // Starting Position
        public static final String STARTING_POSITION = "StartingPosition";
        public StartingPosition startingPosition;
        public enum StartingPosition {
            STORAGE_UNIT,
            WAREHOUSE;

            public static StartingPosition ToStartingPosition(String startingPosition) {
                try {
                    return valueOf(startingPosition);
                } catch (Exception e) {
                    return STORAGE_UNIT;
                }
            }
        }

        // Lift Stages
        public static final String LIFT_STAGE_1_POSITION = "LiftStage1Position";
        public int liftStage1Position = 200;

        public static final String LIFT_STAGE_2_POSITION = "LiftStage2Position";
        public int liftStage2Position = 400;

        public static final String LIFT_STAGE_3_POSITION = "LiftStage3Position";
        public int liftStage3Position = 600;
}
