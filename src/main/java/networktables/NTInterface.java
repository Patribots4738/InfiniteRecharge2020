package networktables;

import edu.wpi.first.networktables.NetworkTableInstance;

public class NTInterface {

    NetworkTableInstance NT;

    public NTInterface() {

        NT = NetworkTableInstance.getDefault();

    }

    public NTTable getTable(String tableName) {

        return new NTTable(NT.getTable(tableName));

    }

    public NTTable createTable(String tableName) {

        return new NTTable(NT.getTable(tableName));

    }

}