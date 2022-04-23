package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;

public class MiningTask extends TaskNode {

    private short copperOre = 4645;
    private short ironOre = 2576;

    @Override
    public boolean accept() {
        // If our inventory isn't full and we're not mining, we should start
        return !Inventory.isFull() && !isMining();
    }

    @Override
    public int execute() {
        GameObject rock = getClosestRock();

        // If there aren't any available rocks near us, we should just wait until one's available
        if (rock == null) return Calculations.random(500, 1000);

        if (rock.interact("Mine")) { // If we successfully click on the rock
            sleepUntil(this::isMining, Calculations.random(100, 2500)); // Wait until we're mining, with a max wait time
        }

        return Calculations.random(200, 500);
    }

    private GameObject getClosestRock() {
        return GameObjects.closest(object -> object.getName().equalsIgnoreCase("Rocks") && object.hasAction("Mine") && object.getModelColors() != null && object.getModelColors()[0] == ironOre);
    }

    private boolean isMining() {
        return getLocalPlayer().isAnimating() || getLocalPlayer().isMoving();
    }

}