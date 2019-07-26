package net.runelite.client.pluginsplus.ridtools;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.pluginsplus.OverlayPlus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class GenesisMapOverlay extends OverlayPlus
{
    private final Client client;

    @Inject
    public GenesisMapOverlay(Client client)
    {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {

        if (GenesisPlugin.hideMap)
            renderBlackMap(graphics);

        return null;
    }

    private void renderBlackMap(Graphics2D graphics)
    {
        Point minimapLocation = client.getLocalPlayer().getMinimapLocation();
        if (minimapLocation != null)
        {
            renderMinimapLocationWhole(graphics, minimapLocation);
        }
    }

    public static void renderMinimapLocationWhole(Graphics2D graphics, Point mini) {
        graphics.setColor(Color.BLACK);
        graphics.fillOval(mini.getX() - 70, mini.getY() - 70, 140, 140);
    }
}