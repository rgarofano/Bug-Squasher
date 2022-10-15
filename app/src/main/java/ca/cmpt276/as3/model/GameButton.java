package ca.cmpt276.as3.model;

public class GameButton {
    private boolean isBug;
    private int bugCount;
    private final int row;
    private final int col;
    private boolean hideText = true;
    private boolean bugFound = false;

    public GameButton(boolean isBug, int bugCount, int row, int col) {
        this.isBug = isBug;
        this.bugCount = bugCount;
        this.row = row;
        this.col = col;
    }

    public boolean isBug() {
        return isBug;
    }

    public void setBug(boolean bug) {
        isBug = bug;
    }

    public int getBugCount() {
        return bugCount;
    }

    public void setBugCount(int bugCount) {
        this.bugCount = bugCount;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public void showText() {
        this.hideText = false;
    }
    public boolean isHidingText() {
        return this.hideText;
    }

    public boolean isBugFound() {
        return bugFound;
    }

    public void setBugFound(boolean bugFound) {
        this.bugFound = bugFound;
    }
}
