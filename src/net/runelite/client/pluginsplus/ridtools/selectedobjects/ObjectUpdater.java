package net.runelite.client.pluginsplus.ridtools.selectedobjects;

import com.runeliteplus.RuneLitePlus;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;

/**
 * Gathers the world objects without killing the FPS
 * (Doing this stuff on render is laggy just like the dev tools)
 * <p>
 * Updates the detected NPC's
 */
public class ObjectUpdater implements Runnable {

    private SelectedObjectsOverlay overlay;

    public ObjectUpdater(SelectedObjectsOverlay overlay) {
        this.overlay = overlay;
    }

    @Override
    public void run() {
        try {
            while (RuneLitePlus.getInstance().injected) {
                getTileObjects();
                getNpcs();

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getNpcs() {
        synchronized (overlay.detectedNpcs) {
            NPC[] all = overlay.client.getCachedNPCs();
            ArrayList<NPC> wanted = new ArrayList<>();
            npcs:
            for (NPC npc : all) {
                if (npc == null)
                    continue;

                highlighted:
                for (HighlightedObject highlightedObject : overlay.tab.selectedObjects) {
                    if (!highlightedObject.npc)
                        continue;

                    if (highlightedObject.id == npc.getId()) {
                        if ((highlightedObject.x == -1 && highlightedObject.y == -1) ||
                                (highlightedObject.x == npc.getWorldLocation().getX() && highlightedObject.y == npc.getWorldLocation().getY())) {

                            if (highlightedObject.onlyRenderInLineOfSight && !(
                                    (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                            overlay.client,
                                            new WorldPoint(npc.getWorldLocation().getX() - 1,
                                                    npc.getWorldLocation().getY() - 1,
                                                    npc.getWorldLocation().getPlane())
                                    )) ||

                                            (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                                    overlay.client,
                                                    new WorldPoint(npc.getWorldLocation().getX() - 1,
                                                            npc.getWorldLocation().getY() + 1,
                                                            npc.getWorldLocation().getPlane())
                                            )) ||

                                            (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                                    overlay.client,
                                                    new WorldPoint(npc.getWorldLocation().getX() + 1,
                                                            npc.getWorldLocation().getY() - 1,
                                                            npc.getWorldLocation().getPlane())
                                            ))

                            )) {
                                continue highlighted;
                            } else {
                                wanted.add(npc);
                                continue npcs;
                            }

                        }
                    }
                }
            }
            overlay.detectedNpcs = wanted.toArray(new NPC[]{});
        }
    }

    public void getTileObjects() {
        synchronized (overlay.detectedObjects) {

            Scene scene = overlay.client.getScene();
            Tile[][][] tiles = scene.getTiles();

            int z = overlay.client.getPlane();

            ArrayList<TileObject> wanted = new ArrayList<>();

            for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
                for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                    Tile tile = tiles[z][x][y];

                    if (tile == null)
                        continue;

                    addObjects(tile.getDecorativeObject(), wanted);
                    addObjects(tile.getWallObject(), wanted);
                    addObjects(tile.getGroundObject(), wanted);

                    for (TileObject to : tile.getGameObjects()) {
                        addObjects(to, wanted);
                    }
                }
            }

            overlay.detectedObjects = wanted.toArray(new TileObject[]{});
        }
    }

    private void addObjects(TileObject gameObject, ArrayList<TileObject> wanted) {
        if (gameObject == null)
            return;

        highlighted:
        for (HighlightedObject highlightedObject : overlay.tab.selectedObjects) {
            if (highlightedObject.npc)
                continue;

            if (highlightedObject.id == gameObject.getId()) {
                if ((highlightedObject.x == -1 && highlightedObject.y == -1) ||
                        (highlightedObject.x == gameObject.getWorldLocation().getX() && highlightedObject.y == gameObject.getWorldLocation().getY())) {

                    if (highlightedObject.onlyRenderInLineOfSight && !(
                            (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                    overlay.client,
                                    new WorldPoint(gameObject.getWorldLocation().getX() - 1,
                                            gameObject.getWorldLocation().getY() - 1,
                                            gameObject.getWorldLocation().getPlane())
                            )) ||

                                    (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                            overlay.client,
                                            new WorldPoint(gameObject.getWorldLocation().getX() - 1,
                                                    gameObject.getWorldLocation().getY() + 1,
                                                    gameObject.getWorldLocation().getPlane())
                                    )) ||

                                    (overlay.client.getLocalPlayer().getWorldArea().hasLineOfSightTo(
                                            overlay.client,
                                            new WorldPoint(gameObject.getWorldLocation().getX() + 1,
                                                    gameObject.getWorldLocation().getY() - 1,
                                                    gameObject.getWorldLocation().getPlane())
                                    ))

                    )) {
                        continue highlighted;
                    } else {
                        wanted.add(gameObject);
                        return;
                    }
                }
            }
        }
    }
}
