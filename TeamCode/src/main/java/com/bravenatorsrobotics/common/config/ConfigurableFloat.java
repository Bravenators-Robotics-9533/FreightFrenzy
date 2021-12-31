package com.bravenatorsrobotics.common.config;

public class ConfigurableFloat extends ConfigurableItem {

    public interface ConfigurableFloatRunnable {
        void Run(float value);
    }

    private final String identifier;
    private final ConfigurableFloatRunnable configurableFloatRunnable;

    public ConfigurableFloat(String identifier, float max, float min, float inc, float value, ConfigurableFloatRunnable saveRunnable) {
        this.identifier = identifier;

        this.optionRunnable = () -> menu.addOption(identifier, max, min, inc, value);
        this.configurableFloatRunnable = saveRunnable;
    }

    @Override
    public void RunSaveRunnable() {
        this.configurableFloatRunnable.Run((float) Double.parseDouble(menu.getCurrentChoiceOf(this.identifier)));
    }

}