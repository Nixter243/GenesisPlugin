package net.runelite.client.pluginsplus.ridtools.selectedobjects;


import net.runelite.client.pluginsplus.ridtools.GenesisPlugin;

import javax.swing.*;

/**
 * Created by FussyPace on 5/11/2015.
 */
public class SelectedObjectsTable extends JTable {

    public SelectedObjectsModel model;

    public SelectedObjectsTable(GenesisPlugin sp) {
        super();
        setModel(model = new SelectedObjectsModel(sp));

        setOpaque(false);
        setAutoCreateRowSorter(true);
        setShowVerticalLines(true);
        setShowGrid(true);
    }

}
