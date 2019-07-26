package net.runelite.client.pluginsplus.ridtools.selectedobjects;

import net.runelite.client.pluginsplus.OverlayPlus;
import net.runelite.client.pluginsplus.ridtools.GenesisPlugin;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

public class SelectedObjectsOverlay extends OverlayPlus {

    public Client client;
    public GenesisPlugin tab;
    public TileObject[] detectedObjects = new TileObject[0];
    public NPC[] detectedNpcs = new NPC[0];

    public SelectedObjectsOverlay(Client _client, GenesisPlugin _tab) {
        client = _client;
        tab = _tab;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.HIGHEST);

        new Thread(new ObjectUpdater(this)).start();
    }

    /**
     * Render the highlighted objects while checking if user provided conditions are met.
     * @param graphics
     * @return
     */
    @Override
    public Dimension render(Graphics2D graphics) {
        renderObjects(graphics);
        renderNpcs(graphics);

        return null;
    }

    public void renderNpcs(Graphics2D graphics) {
        NPC[] dNpcs = detectedNpcs;
        for (NPC npc : dNpcs) {
            if (npc == null || npc.getConvexHull() == null)
                continue;


            java.awt.geom.Area p = Perspective.getClickbox(client, npc.getModel(), npc.getOrientation(), npc.getLocalLocation());//npc.getConvexHull();
            //p = p.createTransformedArea(AffineTransform.getTranslateInstance(-10, -25));
            for (HighlightedObject highlightedObject : tab.selectedObjects) {
                if (!highlightedObject.npc)
                    continue;


                if (highlightedObject.id == npc.getId() && (
                        (highlightedObject.x == -1 && highlightedObject.y == -1)  ||
                                (highlightedObject.x == npc.getWorldLocation().getX() && highlightedObject.y == npc.getWorldLocation().getY())
                )) {
                    graphics.setColor(highlightedObject.color);
                    graphics.fill(p);
                    graphics.setStroke(new BasicStroke(2));
                    graphics.setColor(highlightedObject.color.darker());
                    graphics.draw(p);

                }
            }
        }
    }

    public void renderObjects(Graphics2D graphics) {
        TileObject[] dObjs = detectedObjects;
        for (TileObject gameObject : dObjs) {
            if (gameObject == null || (gameObject.getClickbox() == null))
                continue;
            //if (gameObject.getClickbox() == null)
            java.awt.geom.Area fixedA = gameObject.getClickbox();//.createTransformedArea(AffineTransform.getTranslateInstance(-10, -25));

            for (HighlightedObject highlightedObject : tab.selectedObjects) {
                if (highlightedObject.npc)
                    continue;

                if (highlightedObject.id == gameObject.getId() && (
                        (highlightedObject.x == -1 && highlightedObject.y == -1)  ||
                                (highlightedObject.x == gameObject.getWorldLocation().getX() && highlightedObject.y == gameObject.getWorldLocation().getY())
                )) {
                    graphics.setColor(highlightedObject.color);
                    graphics.fill(fixedA);
                    graphics.setStroke(new BasicStroke(3));
                    graphics.setColor(highlightedObject.color.darker());
                    graphics.draw(fixedA);
                }
            }
        }
    }




}
