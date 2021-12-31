package com.bravenatorsrobotics.common.config;

public class ConfigurableBoolean extends ConfigurableItem {

    public interface ConfigurableBooleanRunnable {
        void Run(boolean value);
    }

    private final String identifier;
    private final ConfigurableBooleanRunnable configurableBooleanRunnable;

    public ConfigurableBoolean(String identifier, boolean value, ConfigurableBooleanRunnable saveRunnable) {
        this.identifier = identifier;

        this.optionRunnable = () -> menu.addBooleanOption(identifier, value);
        this.configurableBooleanRunnable = saveRunnable;
    }

    @Override
    public void RunSaveRunnable() {
        this.configurableBooleanRunnable.Run(menu.getCurrentBooleanChoiceOf(this.identifier));
    }
}
