package com.bravenatorsrobotics.common.config;

import com.bravenatorsrobotics.common.core.BravenatorRuntimeException;
import com.bravenatorsrobotics.common.core.SimpleMenu;

public class ConfigurableItem {

    protected static SimpleMenu menu = null;

    public interface ConfigurableItemRunnable {
        void Run();
    }

    protected ConfigurableItemRunnable optionRunnable;
    protected ConfigurableItemRunnable saveRunnable;

    public ConfigurableItem() {}

    public ConfigurableItem(ConfigurableItemRunnable optionRunnable, ConfigurableItemRunnable saveRunnable) {
        if(ConfigurableItem.menu == null) {
            throw new BravenatorRuntimeException("You must call ConfigurableItem.SetMenuContext() before creating configurable items!");
        }

        this.optionRunnable = optionRunnable;
        this.saveRunnable = saveRunnable;
    }

    public void RunOptionRunnable() {
        if(this.optionRunnable != null)
            this.optionRunnable.Run();
    }

    public void RunSaveRunnable() {
        if(this.saveRunnable != null) {
            this.saveRunnable.Run();
        }
    }

    public static void SetMenuContext(SimpleMenu menu) {
        ConfigurableItem.menu = menu;
    }

}