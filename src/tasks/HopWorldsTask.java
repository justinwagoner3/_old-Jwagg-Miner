package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Player;

public class HopWorldsTask extends TaskNode {

    private final Area ironOreAreaEastVarrock = new Area(3283, 3365, 3290, 3372);

    @Override
    public boolean accept() {
        //search for a player using closest method and a lambda expression. First, make sure the player isn't your own player. Then check if it's in the area.
        Player player = Players.closest(p -> p != null && !p.equals(getLocalPlayer()) && ironOreAreaEastVarrock.contains(p));
        if (player != null) {
            sleep(Calculations.random(7000,10000));
            Player player2 = Players.closest(p -> p != null && !p.equals(getLocalPlayer()) && ironOreAreaEastVarrock.contains(p));
            if (player2 != null) {
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public int execute() {
        // F2P, no level requirement, no PVP world
        World world = Worlds.getRandomWorld(w -> w.isF2P() && !w.isPVP() && w.getMinimumLevel() == 0);

        WorldHopper.hopWorld(world);
        sleep(Calculations.random(5000,7000));
        Player player = Players.closest(p -> p != null && !p.equals(getLocalPlayer()) && ironOreAreaEastVarrock.contains(p));
        if (player == null) {
            if(Tabs.getOpen() != Tab.INVENTORY) {
                Tabs.open(Tab.INVENTORY);
            }
        }
        return Calculations.random(100,200);
    }
}
