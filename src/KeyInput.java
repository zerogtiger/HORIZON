/*

Description:

Main KeyListener for the game. Responsible for reporting and updating the states of interested keys to be used when
making calculations for the game components.

*/

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.*;

public class KeyInput implements KeyListener {

    //Boolean array to keep track of key states
    //0 for left, 1 for right, 2 for space, 3 for up, 4 for down, 5 for enter, 6 for esc.
    private static boolean[] keyPress;

    public KeyInput() {
        keyPress = new boolean[7];
    }

    public void keyTyped(KeyEvent e) {

    }

    //Description: resets all key-states of interested keys to false
    //Parameters: none
    //Return: void
    public void reset() {

        //Reset every key-state to false
        for (int i = 0; i <= 6; i++) {
            keyPress[i] = false;
        }
    }

    //Description: toggles the state of the pressed key to true if it is a key of interest
    //Parameters: the reported KeyEvent
    //Return: void
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        //Update the key-state to true if a key of interest is pressed
        if (key == KeyEvent.VK_LEFT) {
            keyPress[0] = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            keyPress[1] = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            keyPress[2] = true;
        }
        if (key == KeyEvent.VK_UP) {
            keyPress[3] = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            keyPress[4] = true;
        }
        if (key == KeyEvent.VK_ENTER) {
            keyPress[5] = true;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            keyPress[6] = true;
        }
    }

    //Description: toggles the state of the released key to false if it is a key of interest
    //Parameters: the reported KeyEvent
    //Return: void
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        //Update the key-state to false if a key of interest is released
        if (key == KeyEvent.VK_LEFT) {
            keyPress[0] = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            keyPress[1] = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            keyPress[2] = false;
        }
        if (key == KeyEvent.VK_UP) {
            keyPress[3] = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            keyPress[4] = false;
        }
        if (key == KeyEvent.VK_ENTER) {
            keyPress[5] = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            keyPress[6] = false;
        }
    }

    public static boolean getKeyPress(int index) {
        return keyPress[index];
    }

    public static void setKeyPress(int index, boolean state) {
        keyPress[index] = state;
    }
}
