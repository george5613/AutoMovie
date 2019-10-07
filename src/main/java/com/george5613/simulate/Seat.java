package com.george5613.simulate;

public class Seat {

    private int mRow;
    private int mColumn;
    private boolean mEnabled = true;

    public Seat(int row, int column) {
        this.mRow = row;
        this.mColumn = column;
    }

    public Seat(int row, int column, boolean enabled) {
        this.mRow = row;
        this.mColumn = column;
        this.mEnabled = enabled;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        this.mColumn = column;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnable(boolean enabled) {
        mEnabled = enabled;
    }

    public String getTag() {
        return "" + mRow + "排" + mColumn + "座";
    }

    @Override
    public String toString() {
        return getTag();
    }
}
