package wrappers;

import networking.*;
public class SmashBoard {

    NTTable smashBoardTable;

    public SmashBoard() {

        smashBoardTable = new NTInterface().getTable("/SmartDashboard");

    }

    public void putValue(Object value, String key) {

        smashBoardTable.set(key, value);

    }

    // you'll need to typecast the objects you get from this, but they won't be strange object types, just doubles, booleans and strings
    public Object getValue(String key) {

        return smashBoardTable.get(key);

    }

}
