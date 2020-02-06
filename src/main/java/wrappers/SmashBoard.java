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

    public Object getValue(String key) {

        return smashBoardTable.get(key);

    }

}
