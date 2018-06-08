package agent;

import world.World;

import java.util.Random;

public class NormalAgent extends Agent {

    public NormalAgent(boolean ifInfected, int id) {
        super(ifInfected, id);
    }

    @Override
    public void keepMeeting() {
        Random random = World.getInstance().getRandom();

        while (random.nextDouble() < World.getInstance().getMeetingProb())
            arrangeMeeting(getFriendList());
    }

    @Override
    public String toString() {
        String result = String.valueOf(getId());
        if (ifInfected())
            result += "*";
        return result + " zwykły";
    }
}
