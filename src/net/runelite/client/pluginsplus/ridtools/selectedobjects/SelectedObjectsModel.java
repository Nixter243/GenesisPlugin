package net.runelite.client.pluginsplus.ridtools.selectedobjects;

import net.runelite.client.pluginsplus.ridtools.GenesisPlugin;

import javax.swing.table.AbstractTableModel;

/**
 * Created by FussyPace on 5/11/2015.
 */
public class SelectedObjectsModel extends AbstractTableModel {
    private final String[] columnNames = { "ID", "Location", "Color", "Line of sight", "Is NPC" };
    private final GenesisPlugin genesisTab;

    public SelectedObjectsModel(GenesisPlugin profilesTab) {
        this.genesisTab = profilesTab;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return genesisTab.selectedObjects.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return genesisTab.selectedObjects.get(rowIndex).id;

            case 1:
                return String.format("%s, %s", genesisTab.selectedObjects.get(rowIndex).x, genesisTab.selectedObjects.get(rowIndex).y);

            case 2:
                return genesisTab.selectedObjects.get(rowIndex).color;

            case 3:
                return genesisTab.selectedObjects.get(rowIndex).onlyRenderInLineOfSight;

            case 4:
                return genesisTab.selectedObjects.get(rowIndex).npc;

        }
        return "";
    }
}
