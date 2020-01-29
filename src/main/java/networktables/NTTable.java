package networktables;

import edu.wpi.first.networktables.NetworkTable;
import java.util.Set;

public class NTTable {

    // the base Network table that this class wraps
    NetworkTable table;

    public NTTable(NetworkTable table) {

        this.table = table;

    }

    // returns all of the set keys in the table in a String[] array.
    public String[] getKeys() {

        Set<String> rawKeys = table.getKeys();

        String[] keys = rawKeys.toArray(new String[0]);

        return keys;

    }

    public NTTable[] getSubtables() {
        // gets all of the sub table names inside of this table.
        Set<String> rawSubtableNames = table.getSubTables();
        // gets the keys of the current table
        String[] subtableNames = (String[])rawSubtableNames.toArray();
        // initializes a new array of NTTables
        NTTable[] subtables = new NTTable[subtableNames.length];

        for(int i = 0; i < subtables.length; i++) {
            // each of the subtables are added to the array
            subtables[i] = new NTTable(table.getSubTable(subtableNames[i]));

        }

        return subtables;

    }

    // gets the path of the NetworkTable
    public String getTablePath() {

        return table.getPath();

    }
    // gets the value at the key in this NetworkTable
    public Object get(String key) {
        // this value is not bound to a specific type, so needs to be typecasted before use elsewhere
        return table.getEntry(key).getValue();

    }
    // sets the value at the key in this NetworkTable
    public void set(String key, Object value) {
        // this value is not bound to a specific type, so it can be set with any Object type
        table.getEntry(key).setValue(value);

    }

}