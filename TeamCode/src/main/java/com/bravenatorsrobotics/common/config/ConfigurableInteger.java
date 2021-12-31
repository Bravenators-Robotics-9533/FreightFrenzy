package com.bravenatorsrobotics.common.config;

public class ConfigurableInteger extends ConfigurableItem {

    public interface ConfigurableIntegerRunnable {
        void Run(int value);
    }

    private final String identifier;
    private final ConfigurableIntegerRunnable configurableIntegerRunnable;

    public ConfigurableInteger(String identifier, int max, int min, int inc, int value, ConfigurableIntegerRunnable saveRunnable) {
        this.identifier = identifier;

        this.optionRunnable = () -> menu.addOption(identifier, max, min, inc, value);
        this.configurableIntegerRunnable = saveRunnable;
    }

    @Override
    public void RunSaveRunnable() {
        this.configurableIntegerRunnable.Run(Integer.parseInt(menu.getCurrentChoiceOf(this.identifier)));
    }
}