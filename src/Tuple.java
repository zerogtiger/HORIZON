/*

Description:

Tuple class to compactly store the states of the speeder at each game tick to be referenced by the phantom speeder in
order to replicate the last run. Data includes position, charging and scratching states.

 */

public class Tuple {

    //Player position and velocity
    public int x, y, velX, velY;

    //Player state
    public boolean isChargingLeft, isChargingRight, isScratchingLeft, isScratchingRight;

    //Constructor
    public Tuple(int x, int y, int velX, int velY, boolean isChargingLeft, boolean isChargingRight, boolean isScratchingLeft, boolean isScratchingRight) {

        //Initialize variables
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.isChargingLeft = isChargingLeft;
        this.isChargingRight = isChargingRight;
        this.isScratchingLeft = isScratchingLeft;
        this.isScratchingRight = isScratchingRight;
    }
}
