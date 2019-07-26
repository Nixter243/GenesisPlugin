/*
 * Copyright (c) 2018 Charlie Waters
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.pluginsplus.ridtools;

import com.runeliteplus.RuneLitePlus;
import com.runeliteplus.core.settings.Settings;
import com.runeliteplus.core.tweaks.TweakManager;
import com.runeliteplus.ui.controls.HMButton;
import com.runeliteplus.ui.images.ImageUtility;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

class GenesisPanel extends PluginPanel {

    private JCheckBox enablePolyFill = new JCheckBox("Patch polygon rendering to solid.");
    private JCheckBox enableHiddenMap = new JCheckBox("Hide mini-map.");
    private HMButton openRuneLitePlus = new HMButton( "Open RL+ Window", ImageUtility.loadIcon1616("home.png"));

    void init(GenesisPlugin p) {
        // this may or may not qualify as a hack
        // but this lets the editor pane expand to fill the whole parent panel
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel container = new JPanel(), mainContainer = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        enablePolyFill.setSelected((boolean) Settings.getInstance().getSetting("enablePolyFill", false));
        enablePolyFill.addItemListener(e -> {
            GenesisPlugin.fillDraw = (e.getStateChange() == ItemEvent.SELECTED);
            //hot-fixing/patching the polygon rendering stuff in.
            if (GenesisPlugin.fillDraw) {
                TweakManager.getInstance().applyTweak(new OverlayTweaks.OverlayUtils());
                TweakManager.getInstance().applyTweak(new OverlayTweaks.NPCOverlay());
            } else {
                TweakManager.getInstance().reverseTweak(new OverlayTweaks.OverlayUtils());
                TweakManager.getInstance().reverseTweak(new OverlayTweaks.NPCOverlay());
            }
            Settings.getInstance().setSetting("enablePolyFill", e.getStateChange() == ItemEvent.SELECTED);
        });

        enableHiddenMap.setSelected((boolean) Settings.getInstance().getSetting("enableHiddenMap", false));
        enableHiddenMap.addItemListener(e -> {
            GenesisPlugin.hideMap = (e.getStateChange() == ItemEvent.SELECTED);
            Settings.getInstance().setSetting("enableHiddenMap", e.getStateChange() == ItemEvent.SELECTED);
        });

        openRuneLitePlus.addActionListener(e -> {
            if (!RuneLitePlus.getInstance().getMainInterface().isVisible())
                RuneLitePlus.getInstance().getMainInterface().setVisible(true);

            RuneLitePlus.getInstance().getMainInterface().toFront();
        });

        mainContainer.add(new JLabel("General Tweaks: "));
        mainContainer.add(enablePolyFill);
        mainContainer.add(enableHiddenMap);

        mainContainer.add(Box.createRigidArea(new Dimension(1, 10)));

        mainContainer.add(openRuneLitePlus);

        mainContainer.add(Box.createRigidArea(new Dimension(1, 10)));

        container.add(new JLabel(ImageUtility.loadIconElsewhere(GenesisPanel.class, "Genesis.png", 150, 80)), BorderLayout.CENTER);

        add(container, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);

    }

}
