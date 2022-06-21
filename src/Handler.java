/*

Description:

The handler of the game containing a list of GameObjets to be updated and rendered on each game tick.

*/

import java.util.*;
import java.awt.*;

public class Handler {

    //List to store all GameObjects to be updated and rendered
    LinkedList<GameObject> object = new LinkedList<>();

    //Description: updates all GameObjects in the object list
    //Parameters: none
    //Return: void
    public void tick() {

        //Loop through every GameObject and updates them
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.tick();
        }
    }

    //Description: renders all GameObjects in the object list
    //Parameters: the Graphics object to draw the GameObjects
    //Return: void
    public void render(Graphics g) {

        //Loop through every GameObject and renders them
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }
    }

    //Description: adds the given GameObject to the object list
    //Parameters: the GameObject to be added
    //Return: void
    public void addObject(GameObject object) {

        //Adds the provided GameObject to the list
        this.object.add(object);
    }

    //Description: removes the given GameObject from the object list
    //Parameters: the GameObject to be removed
    //Return: void
    public void removeObject(GameObject object) {

        //Removes the provided GameObject from the list
        this.object.remove(object);
    }

}
