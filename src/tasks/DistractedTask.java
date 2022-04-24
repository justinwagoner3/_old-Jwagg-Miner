package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;

public class DistractedTask extends TaskNode {
    @Override
    public boolean accept() {
        // 8% chance to be distracted
        if(Calculations.random(1,100) <= 8) {
            log("Distracted");
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int execute() {
        return Calculations.random(2000, 10000);
    }
}
