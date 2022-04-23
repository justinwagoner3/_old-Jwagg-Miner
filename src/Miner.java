import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import tasks.*;

import java.awt.*;

// Every script needs a ScriptManifest so it can be seen in the script manager
@ScriptManifest(category = Category.MINING, name = "Miner", description = "Mines anything, and drops inventory when full.", author = "Jwagg", version = 1.0)
public class Miner extends TaskScript {

    @Override
    public void onStart() {
        // Start DreamBot's skill tracker for the mining skill, so we can later see how much experience we've gained
        SkillTracker.start(Skill.MINING);

        // Now add our two tasks so the client knows what to do
        addNodes(new HopWorldsTask(), new MiningTask(), new DropTask(), new DistractedTask(), new CheckSkillsTask());
    }

    @Override
    public void onPaint(Graphics g) {
        String experienceGainedText = String.format(
                "Mining Experience: %d (%d per hour)", // The paint's text format. '%d' will be replaced with the next two arguments.
                SkillTracker.getGainedExperience(Skill.MINING),
                SkillTracker.getGainedExperiencePerHour(Skill.MINING)
        );
        String miningLevelText = String.format(
                "Mining Level: %d (%d gained)",
                SkillTracker.getStartLevel(Skill.MINING)+SkillTracker.getGainedLevels(Skill.MINING),
                SkillTracker.getGainedLevels(Skill.MINING)
        );
        // Now we'll draw the text on the canvas at (5, 35). (0, 0) is the top left of the canvas.
        g.drawString(experienceGainedText, 5, 35);
        g.drawString(miningLevelText, 5, 50);

    }

}