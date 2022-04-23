package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.script.TaskNode;

public class DropTask extends TaskNode {

    @Override
    public boolean accept() {
        // If our inventory is full, we should execute this task
        return Inventory.isFull();
    }

    @Override
    public int execute() {
        // Filter that will drop all items that isn't a pickaxe or coins
        Inventory.dropAll(i -> i != null && !i.getName().contains("pickaxe") && !i.getName().contains("Coins"));

        return Calculations.random(300, 600);
    }
}