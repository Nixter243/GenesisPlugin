package net.runelite.client.pluginsplus.ridtools.selectedobjects;

import net.runelite.client.pluginsplus.ridtools.GenesisTab;
import com.runeliteplus.RuneLitePlus;
import com.runeliteplus.core.settings.Settings;

import javax.swing.*;
import java.awt.*;

public class AddObjectDialog extends JDialog {

    Color[] colors = {Color.YELLOW, Color.PINK, Color.GRAY, Color.CYAN, Color.ORANGE, Color.white, Color.red, Color.blue, Color.green};

    JTextField objectid = new JTextField(), objectx = new JTextField("-1"), objecty = new JTextField("-1");
    JCheckBox renderInLineOfSight = new JCheckBox("Line of sight?"), isNpc = new JCheckBox("Is an NPC?");
    JButton accept = new JButton("Add");


    JComboBox colorBox = new JComboBox(colors);

    public AddObjectDialog(GenesisTab tab) {
        super(RuneLitePlus.getInstance().getMainInterface());
        setTitle("Add New Object");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        colorBox.setPreferredSize(new Dimension(50, 20));
        colorBox.setRenderer(new MyCellRenderer());


        add(new JLabel("Object ID:"));
        add(objectid);
        add(new JLabel("Object X:"));
        add(objectx);
        add(new JLabel("Object Y:"));
        add(objecty);
        add(new JPanel() {{
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(renderInLineOfSight);
            add(isNpc);
        }});
        add(new JLabel("Color:"));
        add(colorBox);

        accept.addActionListener(e -> {
            HighlightedObject newHob = new HighlightedObject();

            try {
                newHob.id = Integer.parseInt(objectid.getText());
                newHob.x = Integer.parseInt(objectx.getText());
                newHob.y = Integer.parseInt(objecty.getText());
                newHob.color = (Color) colorBox.getSelectedItem();
                newHob.onlyRenderInLineOfSight = renderInLineOfSight.isSelected();
                newHob.npc = isNpc.isSelected();

                tab.genesisPlugin.selectedObjects.add(newHob);
                Settings.getInstance().setSetting("highlightedObjects", tab.genesisPlugin.selectedObjects);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            tab.t.model.fireTableDataChanged();
            dispose();
        });

        add(accept);

        setMinimumSize(new Dimension(200, 400));
        setPreferredSize(getMinimumSize());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public class MyCellRenderer extends JButton implements ListCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }

        boolean b = false;

        @Override
        public void setBackground(Color bg) {
            if (!b) {
                return;
            }
            super.setBackground(bg);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            b = true;
            setText(" ");
            setBackground((Color) value);
            b = false;
            return this;
        }
    }
}

