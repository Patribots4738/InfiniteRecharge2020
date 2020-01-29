package networktables;

import edu.wpi.first.networktables.NetworkTableInstance;

public class NTInterface {

    NetworkTableInstance NT;

    public NTInterface() {

        NT = NetworkTableInstance.getDefault();

    }
    // gets the table name specified and wraps it in the NTTable class. If the table is not created, then it will be created
    public NTTable getTable(String tableName) {

        return new NTTable(NT.getTable(tableName));

    }

}