package tasks;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;

public class CheckSkillsTask extends TaskNode {

    @Override
    public boolean accept() {
        // 1.3% chance to check skills
        return Calculations.random(1,1000) <= 13;
    }

    @Override
    public int execute() {
        if(Tabs.getOpen() != Tab.SKILLS) {
            Tabs.open(Tab.SKILLS);
            // 64% of the time hover the skills
            if(Calculations.random(1,1000) <= 640){
                Skills.hoverSkill(Skill.MINING);
                sleep(Calculations.random(100,400));
            }
            else{
                sleep(Calculations.random(2000,4000));
            }
            // Re-open the inventory tab after looking at skill
            if(!Tabs.open(Tab.INVENTORY)){
                Tabs.open(Tab.INVENTORY);
            }
        }
        return Calculations.random(100,400);
    }
}
