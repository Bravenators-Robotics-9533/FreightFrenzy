package com.bravenatorsrobotics.common.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractConfig {

    private final String configurationID;

    public static class EnumMapValue {

        public final Enum<?> enumeration;
        public final String value;

        public EnumMapValue(Enum<?> enumeration, String value) {
            this.enumeration = enumeration;
            this.value = value;
        }

    }

    private final HashMap<String, Integer> integerMap;
    private final HashMap<String, Float> floatMap;
    private final HashMap<String, Boolean> booleanMap;
    private final HashMap<String, EnumMapValue> enumMap;

    private SharedPreferences sharedPreferences = null;

    public AbstractConfig(@NonNull String configurationID, Context context) {
        this.configurationID = configurationID;

        this.integerMap = new HashMap<>();
        this.floatMap = new HashMap<>();
        this.booleanMap = new HashMap<>();
        this.enumMap = new HashMap<>();

        if(context != null) {
            this.sharedPreferences = context.getSharedPreferences(configurationID, Context.MODE_PRIVATE);

            this.PutConfigs();
            this.Load();
        }
    }

    public void Initialize(@NonNull Context context) {
        if(this.sharedPreferences != null)
            return;

        this.sharedPreferences = context.getSharedPreferences(configurationID, Context.MODE_PRIVATE);

        this.PutConfigs();
        this.Load();
    }

    protected abstract void PutConfigs();
    protected abstract void GetConfigs();

    public HashMap<String, Integer> GetIntegerMap() { return this.integerMap; }
    public HashMap<String, Float> GetFloatMap() { return this.floatMap; }
    public HashMap<String, Boolean> GetBooleanMap() { return this.booleanMap; }
    public HashMap<String, EnumMapValue> GetEnumMap() { return this.enumMap; }

    public void Save() {
        integerMap.clear();
        floatMap.clear();
        booleanMap.clear();
        enumMap.clear();

        this.PutConfigs();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(Map.Entry<String, Integer> entry : integerMap.entrySet()) {
            editor.putInt(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String, Float> entry : floatMap.entrySet()) {
            editor.putFloat(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String, Boolean> entry : booleanMap.entrySet()) {
            editor.putBoolean(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String, EnumMapValue> entry : enumMap.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue().value);
        }

        editor.apply();
    }

    public void Load() {
        for(Map.Entry<String, Integer> entry : integerMap.entrySet()) {
            integerMap.put(entry.getKey(), sharedPreferences.getInt(entry.getKey(), entry.getValue()));
        }

        for(Map.Entry<String, Float> entry : floatMap.entrySet()) {
            floatMap.put(entry.getKey(), sharedPreferences.getFloat(entry.getKey(), entry.getValue()));
        }

        for(Map.Entry<String, Boolean> entry : booleanMap.entrySet()) {
            booleanMap.put(entry.getKey(), sharedPreferences.getBoolean(entry.getKey(), entry.getValue()));
        }

        for(Map.Entry<String, EnumMapValue> entry : enumMap.entrySet()) {
            enumMap.put(entry.getKey(), new EnumMapValue(entry.getValue().enumeration, sharedPreferences.getString(entry.getKey(), entry.getValue().value)));
        }

        this.GetConfigs();
    }

    protected void PutInteger(@NonNull String identifier, int value) {
        integerMap.put(identifier, value);
    }

    protected void PutFloat(@NonNull String identifier, float value) {
        floatMap.put(identifier, value);
    }

    protected void PutBoolean(@NonNull String identifier, boolean value) {
        booleanMap.put(identifier, value);
    }

    protected <T extends Enum<T>> void PutEnum(@NonNull String identifier, @NonNull Enum<T> enumeration) {
        this.enumMap.put(identifier, new EnumMapValue(enumeration, enumeration.name()));
    }

    protected int GetInteger(@NonNull String identifier, int defaultValue) {
        Integer value = integerMap.get(identifier);

        return (value != null) ? value : defaultValue;
    }

    protected float GetFloat(@NonNull String identifier, float defaultValue) {
        Float value = floatMap.get(identifier);

        return (value != null) ? value : defaultValue;
    }

    protected boolean GetBoolean(@NonNull String identifier, boolean defaultValue) {
        Boolean value = booleanMap.get(identifier);

        return (value != null) ? value : defaultValue;
    }

    protected <T extends Enum<T>> T GetEnum(@NonNull String identifier, @NonNull T defaultValue) {
        String value = Objects.requireNonNull(enumMap.get(identifier)).value;

        if(value == null)
            return null;

        try {
            return T.valueOf(defaultValue.getDeclaringClass(), value);
        } catch(Exception e) {
            return defaultValue;
        }
    }
}
