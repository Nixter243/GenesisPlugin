package net.runelite.client.pluginsplus.ridtools;

import com.runeliteplus.core.attach.RLCallbacks;
import com.runeliteplus.core.settings.Settings;
import com.runeliteplus.core.tweaks.TweakManager;
import com.runeliteplus.ui.images.ImageUtility;
import com.runeliteplus.ui.tabgenerator.UITab;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.pluginsplus.PluginPlus;
import net.runelite.client.pluginsplus.ridtools.selectedobjects.HighlightedObject;
import net.runelite.client.pluginsplus.ridtools.selectedobjects.SelectedObjectsOverlay;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@PluginDescriptor(
        name = "RiD Genesis ToolBox",
        description = "RiD Genesis extension",
        tags = {"RiD", "Genesis", "panel"},
        loadWhenOutdated = true,
        developerPlugin = true
)
public class GenesisPlugin extends PluginPlus {
    @Inject
    private ClientToolbar clientToolbar;

    public ArrayList<HighlightedObject> selectedObjects = new ArrayList<>();

    private GenesisMapOverlay sceneOverlay;
    private SelectedObjectsOverlay selectedObjectsOverlay;

    private GenesisPanel panel;
    private NavigationButton navButton;

    public static boolean fillDraw = false;
    public static boolean hideMap = false;

    @Override
    protected void startUp() {
        clientToolbar = RLCallbacks.injector.getInstance(ClientToolbar.class);

        sceneOverlay = new GenesisMapOverlay(RLCallbacks.getInstance().getClient());
        selectedObjectsOverlay = new SelectedObjectsOverlay(RLCallbacks.getInstance().getClient(), this);

        hideMap = (boolean) Settings.getInstance().getSetting("enableHiddenMap", false);
        fillDraw = (boolean) Settings.getInstance().getSetting("enablePolyFill", false);

        selectedObjects = (ArrayList<HighlightedObject>) Settings.getInstance().getSetting(
                "highlightedObjects",
                new ArrayList<HighlightedObject>());

        tab.t.model.fireTableDataChanged();

        if (GenesisPlugin.fillDraw) {
            TweakManager.getInstance().applyTweak(new OverlayTweaks.OverlayUtils());
            TweakManager.getInstance().applyTweak(new OverlayTweaks.NPCOverlay());
        }

        SwingUtilities.invokeLater(() -> {
            panel = new GenesisPanel();
            panel.init(getPlugin());

            ImageIcon iconc = ImageUtility.loadIcon("configuration.png");

            BufferedImage bi = new BufferedImage(
                    iconc.getIconWidth(),
                    iconc.getIconHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();

            iconc.paintIcon(null, g, 0, 0);
            g.dispose();

            final BufferedImage icon = bi;

            navButton = NavigationButton.builder()
                    .tooltip("RiD Toolbox")
                    .icon(icon)
                    .priority(0)
                    .panel(panel)
                    .build();

            clientToolbar.addNavigation(navButton);
        });
    }

    public GenesisPlugin getPlugin() {return this;}

    @Override
    protected void shutDown() {
        clientToolbar.removeNavigation(navButton);
    }

    private GenesisTab tab;

    @Override
    public UITab getTab() {
        return tab = new GenesisTab(this);
    }

}
