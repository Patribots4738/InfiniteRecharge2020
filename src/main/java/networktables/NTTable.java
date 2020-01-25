package networktables;

import edu.wpi.first.networktables.NetworkTable;

public class NTTable {

    NetworkTable table;

    public NTTable(NetworkTable table) {

        this.table = table;

    }

    public String[] getKeys() {

        java.util.Set<String> rawKeys = table.getKeys();

        String[] keys = rawKeys.toArray(new String[0]);

        return keys;

    }

    public NTTable[] getSubtables() {

        java.util.Set<String> rawSubtableNames = table.getSubTables();

        String[] subtableNames = (String[])rawSubtableNames.toArray();

        NTTable[] subtables = new NTTable[subtableNames.length];

        for(int i = 0; i < subtables.length; i++) {

            subtables[i] = new NTTable(table.getSubTable(subtableNames[i]));

        }

        return subtables;

    }

    public String getTablePath() {

        return table.getPath();

    }

    public double getDouble(String key) {

        return table.getEntry(key).getDouble(0);

    }

    public void setDouble(String key, double value) {

        table.getEntry(key).setDouble(value);

    }

    public String getString(String key) {

        return table.getEntry(key).getString("");

    }

    public void setString(String key, String value) {

        table.getEntry(key).setString(value);

    }

    public boolean getBoolean(String key) {

        return table.getEntry(key).getBoolean(false);

    }

    public void setBoolean(String key, boolean value) {

        table.getEntry(key).setBoolean(value);

    }

}