package net.runelite.client.pluginsplus;

import com.runeliteplus.ui.tabgenerator.UITab;
import net.runelite.client.plugins.Plugin;

/**
 * Implementation of RuneLite Plugin
 */
public abstract class PluginPlus extends Plugin {

    public boolean requiredInjection = true;

    /**
     * Specify a UITab for implementing an interface into the RL+ window.
     * Return null if you have no interface.
     */
    public abstract UITab getTab();

}
