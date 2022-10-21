package ca.cmpt276.as3.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Serves as a key generator for the game board map in the GameLogic class.
    Provides the ability to select a random key from a stored list. **/

public class KeyGen {
    private List<String> allowedKeys = new ArrayList<String>();

    public String generateKey(int row, int col) {
        Pair<Integer, Integer> tuple = new Pair(row, col);
        String key = tuple.toString();
        return key;
    }

    public void addKeyToRandomGenerator(String key) {
        allowedKeys.add(key);
    }

    public String generateRandomKey() {
        if (allowedKeys.size() == 0) {
            throw new RuntimeException("ERROR: No Keys Left To Generate");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(allowedKeys.size());
        String key = allowedKeys.get(randomIndex);
        allowedKeys.remove(key);
        return key;
    }

    public void reset() {
        allowedKeys = new ArrayList<String>();
    }
}
