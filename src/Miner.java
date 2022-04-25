import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import tasks.*;

import java.awt.*;

// Every script needs a ScriptManifest so it can be seen in the script manager
@ScriptManifest(category = Category.MINING,
        name = "Miner",
        description = "Mines anything, and drops inventory when full.",
        author = "Jwagg",
        version = 1.2)

public class Miner extends TaskScript {

    private Anti time = new Anti(this);

    @Override
    public void onStart() {


        time = new Anti(this);

        // Start DreamBot's skill tracker for the mining skill, so we can later see how much experience we've gained
        SkillTracker.start(Skill.MINING);

        // Now add our two tasks so the client knows what to do
        addNodes(new HopWorldsTask(),
                new DistractedTask(),
                new CheckSkillsTask(),
                new MiningTask(),
                new DropTask()
        );
    }

    @Override
    public void onPaint(Graphics g) {
        String experienceGainedText = String.format(
                "Mining Experience: %d (%d per hour)",
                SkillTracker.getGainedExperience(Skill.MINING),
                SkillTracker.getGainedExperiencePerHour(Skill.MINING)
        );
        String miningLevelText = String.format(
                "Mining Level: %d (%d gained)",
                SkillTracker.getStartLevel(Skill.MINING)+SkillTracker.getGainedLevels(Skill.MINING),
                SkillTracker.getGainedLevels(Skill.MINING)
        );

        long ms = time.getElapsedTime();
        long hours = ms / 3600000;
        ms = ms % 3600000;
        long minutes = ms / 60000;
        ms = ms % 60000;
        long seconds = ms / 1000;
        String elapsedTime = String.format(
                "Time Ran: %d:%d:%d",
                hours,
                minutes,
                seconds
        );
        long ms2 = SkillTracker.getTimeToLevel(Skill.MINING);
        long hours2 = ms2 / 3600000;
        ms2 = ms2 % 3600000;
        long minutes2 = ms2 / 60000;
        ms2 = ms2 % 60000;
        long seconds2 = ms2 / 1000;

        String timeToLevel = String.format(
                "Time To Next Level: %d:%d:%d",
                hours2,
                minutes2,
                seconds2
        );
        // Now we'll draw the text on the canvas at (5, 35). (0, 0) is the top left of the canvas.
        g.drawString(elapsedTime, 5, 290);
        g.drawString(experienceGainedText, 5, 305);
        g.drawString(miningLevelText, 5, 320);
        g.drawString(timeToLevel, 5, 335);

    }

    public class Anti {

        public final long START_TIME;

        public Anti(Miner miner) {
            START_TIME = System.currentTimeMillis();
        }

        public long getElapsedTime() {
            return System.currentTimeMillis() - START_TIME;
        }

    }

}