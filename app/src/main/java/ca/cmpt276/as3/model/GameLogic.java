package ca.cmpt276.as3.model;


import java.util.HashMap;
import java.util.Map;

public class GameLogic {
    /* Singleton Interface */
    private static GameLogic gameLogic = null;
    KeyGen keyGen;

    private GameLogic() {
        keyGen = new KeyGen();
    }

    public static GameLogic getInstance() {
        if (gameLogic == null) {
            gameLogic = new GameLogic();
        }
        return gameLogic;
    }

    /* Standard object interface */
    private int numBugs = 6;
    private int numRows = 4;
    private int numCols = 6;
    private int bugsFound = 0;
    private Map<String, GameButton> gameBoard = new HashMap<String, GameButton>();
    private final int INCREMENT_COUNT = 1;
    private final int DECREMENT_COUNT = -1;
    public enum GameStatus {
        NO_SCAN_USED,
        NO_BUG_FOUND,
        BUG_FOUND,
        GAME_OVER
    }

    public int getNumBugs() {
        return numBugs;
    }

    public int getNumberOfRows() {
        return numRows;
    }

    public int getNumberOfColumns() {
        return numCols;
    }

    public int getBugsFound() {
        return bugsFound;
    }

    public void changeOptions(int numRows, int numCols, int numBugs) {
        if (this.numRows != numRows || this.numCols != numCols || this.numBugs != numBugs) {
            this.numRows = numRows;
            this.numCols = numCols;
            this.numBugs = numBugs;
            resetGameBoard();
        }
    }

    private void resetGameBoard() {
        gameBoard = new HashMap<String, GameButton>();
    }

    public void addGameButton(int row, int col) {
        // create a key for this row and column so that we can reuse in
        // the future to access the correct ButtonGame object in the map
        String buttonID = keyGen.generateKey(row, col);
        // allow this key to be randomly selected when choosing
        // which buttons are bugs in the initializeGame function
        keyGen.addKeyToRandomGenerator(buttonID);
        GameButton button = new GameButton(false, 0, row, col);
        gameBoard.put(buttonID, button);
    }

    public void initializeGame() {
        for (int i = 0; i < numBugs; i++) {
            String buttonID = keyGen.generateRandomKey();
            GameButton button = gameBoard.get(buttonID);
            button.setBug(true);
            updateBugCount(button.getRow(), button.getColumn(), INCREMENT_COUNT);
        }
    }

    private void updateBugCount(int bugRow, int bugCol, int offset) {
        // increment the bugCount for every button in the same column as the bug
        // Do not modify the count of the bug itself
        for (int row = 0; row < numRows; row++) {
            if (row != bugRow) {
                String buttonID = keyGen.generateKey(row, bugCol);
                GameButton button = gameBoard.get(buttonID);
                int currentBugCount = button.getBugCount();
                button.setBugCount(currentBugCount + offset);
            }
        }
        // increment the bugCount for every button in the same row as the bug
        // Do not modify the count of the bug itself
        for (int col = 0; col < numRows; col++) {
            if (col != bugCol) {
                String buttonID = keyGen.generateKey(bugRow, col);
                GameButton button = gameBoard.get(buttonID);
                int currentBugCount = button.getBugCount();
                button.setBugCount(currentBugCount + offset);
            }
        }
    }

    public String getText(int row, int col) {
        String buttonID = keyGen.generateKey(row, col);
        GameButton button = gameBoard.get(buttonID);
        return button.isHidingText() ? "" : String.valueOf(button.getBugCount());
    }

    public GameStatus generateClickResponse(int row, int col) {
        String buttonID = keyGen.generateKey(row, col);
        GameButton button = gameBoard.get(buttonID);
        if (button.isBug() && button.isBugFound() && button.isHidingText()) {
            button.showText();
            return GameStatus.NO_BUG_FOUND;
        } else if (button.isBug() && !button.isBugFound()) {
            button.setBugFound(true);
            bugsFound++;
            updateBugCount(row, col, DECREMENT_COUNT);
            return isGameOver() ? GameStatus.GAME_OVER : GameStatus.BUG_FOUND;
        } else if (button.isHidingText()) {
            button.showText();
            return GameStatus.NO_BUG_FOUND;
        }
        return GameStatus.NO_SCAN_USED;
    }

    private boolean isGameOver() {
        return bugsFound == numBugs;
    }
}
