package net.runelite.client.pluginsplus;

import com.runeliteplus.RuneLitePlus;
import com.runeliteplus.core.attach.RLCallbacks;
import net.runelite.client.ui.overlay.Overlay;

/**
 * Implementation of RuneLite Overlay
 */
public abstract class OverlayPlus extends Overlay {

    public OverlayPlus() {
        if (RuneLitePlus.getInstance().injected)
            RLCallbacks.getInstance().getOverlayManager().add(this);
    }

}
