package com.bravenatorsrobotics.common.config;

public class ConfigurableEnum<T extends Enum<T>> extends ConfigurableItem {

    public interface ConfigurableEnumRunnable<T extends Enum<T>> {
        void Run(T value);
    }

    private final String identifier;
    private final Class<T> enumeration;
    private final ConfigurableEnumRunnable<T> configurableEnumRunnable;

    public ConfigurableEnum(String identifier, Class<T> enumeration, T value, ConfigurableEnumRunnable<T> configurableEnumRunnable) {
        this.identifier = identifier;
        this.enumeration = enumeration;

        this.optionRunnable = () -> menu.addOption(identifier, enumeration, value);

        this.configurableEnumRunnable = configurableEnumRunnable;
    }

    @Override
    public void RunSaveRunnable() {
        T value = null;
        T[] possibleValues = enumeration.getEnumConstants();

        String currentChoice = menu.getCurrentChoiceOf(identifier);

        for(T possibleValue : possibleValues) {
            if(possibleValue.name().equals(currentChoice)) {
                value = possibleValue;
                break;
            }
        }

        this.configurableEnumRunnable.Run(value);
    }
}
