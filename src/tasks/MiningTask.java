package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;

public class MiningTask extends TaskNode {

    private final short copperOre = 4645;
    private final short ironOre = 2576;

    private boolean justMinedWithFullInventory = false;
    private boolean extraSleep = false;

    @Override
    public boolean accept() {
        // If our inventory isn't full and we're not mining, we should start
        if(!isMining() && !Inventory.isFull()){
            return true;
        }
        // part of the time, still attempt to mine with a full inv
        else if(Inventory.isFull()){
            // if you did not just try to drop with full inventory, and hit probably to try it, try to mine
            if(!justMinedWithFullInventory && Calculations.random(1,1000) >= 500) {
                justMinedWithFullInventory = true;
                extraSleep = true;
                return true;
            }
            // don't mine, reset state
            else {
                justMinedWithFullInventory = false;
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public int execute() {
        GameObject rock = getClosestRock();

        // If there aren't any available rocks near us, we should just wait until one's available
        if (rock == null) return Calculations.random(500, 1000);

        if (rock.interact("Mine")) { // If we successfully click on the rock
            if(extraSleep){
                sleep(Calculations.random(500,4000));
                extraSleep = false;
            }
            else {
                sleepUntil(this::isMining, Calculations.random(100, 2500)); // Wait until we're mining, with a max wait time
            }
        }

        return Calculations.random(200, 500);
    }

    private GameObject getClosestRock() {
        return GameObjects.closest(object ->
                object.getName().equalsIgnoreCase("Rocks") &&
                        object.hasAction("Mine") &&
                        object.getModelColors() != null &&
                        object.getModelColors()[0] == ironOre &&
                object.distance() <= 2);
    }

    private boolean isMining() {
        return getLocalPlayer().isAnimating() || getLocalPlayer().isMoving();
    }

}