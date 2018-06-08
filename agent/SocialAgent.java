package agent;

import world.World;

import java.util.HashSet;
import java.util.Random;

public class SocialAgent extends Agent {

    public SocialAgent(boolean ifInfected, int id) {
        super(ifInfected, id);
    }

    @Override
    public void keepMeeting() {
        Random random = World.getInstance().getRandom();
        HashSet<Agent> friendsList = new HashSet<>(getFriendList());

        for (Agent agent : getFriendList())
            friendsList.addAll(agent.getFriendList());

        while (random.nextDouble() < World.getInstance().getMeetingProb())
            arrangeMeeting(friendsList);
    }

    @Override
    public String toString() {
        String result = String.valueOf(getId());
        if (ifInfected())
            result += "*";
        return result + " towarzyski";
    }
}
