package net.runelite.client.pluginsplus.ridtools;

import com.runeliteplus.ui.controls.HMButton;
import com.runeliteplus.ui.images.ImageUtility;
import com.runeliteplus.ui.tabgenerator.UITab;
import net.runelite.client.pluginsplus.ridtools.selectedobjects.AddObjectDialog;
import net.runelite.client.pluginsplus.ridtools.selectedobjects.HighlightedObject;
import net.runelite.client.pluginsplus.ridtools.selectedobjects.SelectedObjectsTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class GenesisTab extends UITab {


    public SelectedObjectsTable t;
    private HMButton addButton = new HMButton("Add New Object", ImageUtility.loadIcon1616("add.png"));
    private HMButton removeAllButton = new HMButton("Remove Selected", ImageUtility.loadIcon1616("delete.png"));

    public GenesisPlugin genesisPlugin;


    public GenesisTab(GenesisPlugin plug) {
        super("RiD Genesis Toolbox", ImageUtility.loadIcon1616("configuration.png"));
        genesisPlugin = plug;

        setLayout(new BorderLayout());
        add(new JPanel() {
            {
                setLayout(new BorderLayout());
                add(new JScrollPane(t = new SelectedObjectsTable(plug)), BorderLayout.CENTER);
                setBorder(BorderFactory.createTitledBorder("Highlighted Game Objects"));
            }
        }, BorderLayout.CENTER);

        t.getColumnModel().getColumn(2).setCellRenderer(new MyTableCellRenderer());

        addButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new AddObjectDialog(this));
        });

        removeAllButton.addActionListener(e -> {
            HighlightedObject[] selected = menuSelectedObjects();

            if (selected.length == 0)
                return;

            for (HighlightedObject p : selected) {
                genesisPlugin.selectedObjects.remove(p);
            }

            t.model.fireTableDataChanged();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(addButton);
        bottomPanel.add(removeAllButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public HighlightedObject[] menuSelectedObjects() {
        ArrayList<HighlightedObject> selected = new ArrayList<>();

        for (int i : t.getSelectedRows()) {
            int ID = (int) t.getValueAt(i, 0);
            String[] loc = ((String) t.getValueAt(i, 1)).split(","); // get the user id.
            int X = Integer.parseInt(loc[0].replace(" ", ""));
            int Y = Integer.parseInt(loc[1].replace(" ", ""));


            for (HighlightedObject ho : genesisPlugin.selectedObjects) {
                if (ho != null && ID == ho.id && X == ho.x && Y == ho.y) {
                    selected.add(ho);
                }
            }
        }
        return selected.toArray(new HighlightedObject[]{});
    }

    public class MyTableCellRenderer extends JButton implements TableCellRenderer {
        public MyTableCellRenderer() {
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

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            b = true;
            setText(" ");
            setBackground((Color) value);
            b = false;
            return this;
        }
    }



}
