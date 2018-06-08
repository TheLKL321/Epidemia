package agent;

import world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public abstract class Agent {
    private boolean ifInfected, ifImmune;
    private int id;

    Agent(boolean ifInfected, int id) {
        this.ifInfected = ifInfected;
        this.id = id;
    }

    // losuje chęci spotkania i umawia się na spotkania dopóki nie wylosuje że nie chce się spotykać
    public abstract void keepMeeting();

    // losuje znajomego z listy i umawia się z nim na spotkanie
    void arrangeMeeting(HashSet<Agent> friendList){
        Random random = World.getInstance().getRandom();
        int currentDay = World.getInstance().getCurrentDay(), duration = World.getInstance().getDuration();

        World.getInstance().addMeeting(new Meeting(this, (new ArrayList<>(friendList)).get(random.nextInt(friendList.size()))),
                random.nextInt(duration - currentDay + 1) + currentDay - 1);
    }

    // jeśli Agent nie jest odporny, to zostaje zarażony
    void becomeInfected(){
        this.ifInfected = !ifImmune;
        if (ifInfected())
            World.getInstance().getReporter().reportInfection();
    }

    public void pullThrough(){
        this.ifInfected = false;
        this.ifImmune = true;
        World.getInstance().getReporter().reportRecovery();
    }

    public boolean ifInfected(){
        return ifInfected;
    }

    HashSet<Agent> getFriendList(){
        return World.getInstance().getNet().get(this);
    }

    public int getId(){
        return id;
    }

    @Override
    public abstract String toString();
}
