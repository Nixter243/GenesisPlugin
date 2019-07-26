package net.runelite.client.pluginsplus.ridtools.selectedobjects;

import java.awt.*;
import java.io.Serializable;

public class HighlightedObject implements Serializable {
    private static final long serialVersionUID = 4L;

    boolean npc;
    public int id;
    public int x = -1, y = -1;
    public Color color;
    public boolean onlyRenderInLineOfSight = false;
}