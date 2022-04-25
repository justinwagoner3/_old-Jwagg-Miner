package tasks;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.List;

public class MiningTask extends TaskNode {

    private final short copperOre = 4645;
    private final short ironOre = 2576;

    private boolean justMinedWithFullInventory = false;
    private boolean extraSleep = false;
    private boolean firstIteration = true;

    private GameObject rock = null;
    private GameObject nextRock = null;
    private Tile rockTile = null;

    @Override
    public boolean accept() {
        // If our inventory isn't full and we're not mining, we should start
        if(!Inventory.isFull()){
            return true;
        }
        // part of the time, still attempt to mine with a full inv
        else if(Inventory.isFull()){
            // if you did not just try to drop with full inventory, and hit probability to try it, try to mine
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
        /*
        log("start");
        if(rock != null) {
            log(rock.getTile());
        }
        if(nextRock != null){
            log(nextRock.getTile());
        }
        if(firstIteration) {
            rock = getClosestRock();
        }
        else{
            rock = null;
            rock = nextRock;
        }
         */
        rock = getClosestRock();
        rockTile = rock.getTile();

        // If there aren't any available rocks near us, we should just wait until one's available
        if (rock == null) return Calculations.random(100, 300);

        if (rock.interact("Mine")) { // If we successfully click on the rock
            if(extraSleep){
                sleep(Calculations.random(500,1500));
                extraSleep = false;
            }
            else {
                //nextRock = getNextRock();
                //log("inner");
                //log(rock.getTile());
                //log(nextRock.getTile());
                //Mouse.move(nextRock.getTile());
                sleepUntil(this::rockIsMined, Calculations.random(1000,5000));
            }
        }

        return Calculations.random(0,100);
    }

    private GameObject getClosestRock() {
        return GameObjects.closest(object ->
                        object.getModelColors() != null &&
                        object.getModelColors()[0] == ironOre &&
                object.distance() <= 3);
    }

    // Get the rock that was just mined; need this because have to constantly query the rock in a sleepUntil
    private GameObject getMinedRock() {
        return GameObjects.getTopObjectOnTile(rockTile);
    }

    private boolean rockIsMined(){
        GameObject minedRock = getMinedRock();
        return minedRock.getModelColors() == null;
    }

    private GameObject getNextRock(){
        return GameObjects.closest(object ->
                object.getName().equalsIgnoreCase("Rocks") &&
                        object.hasAction("Mine") &&
                        object.getModelColors() != null &&
                        object.getModelColors()[0] == ironOre &&
                        object.distance() <= 3 &&
                        object.getTile() != rockTile);
    }
}