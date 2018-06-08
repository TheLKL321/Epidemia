package agent;

import world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public abstract class Agent {
    private boolean ifInfected, ifImmune;
    private int id;

    protected Agent(boolean ifInfected, int id) {
        this.ifInfected = ifInfected;
        this.id = id;
    }

    public abstract void keepMeeting();

    protected void arrangeMeeting(HashSet<Agent> friendList){
        Random random = World.getInstance().getRandom();
        int currentDay = World.getInstance().getCurrentDay(), duration = World.getInstance().getDuration();
        World.getInstance().addMeeting(new Meeting(this, (new ArrayList<>(friendList)).get(random.nextInt(friendList.size()))),
                random.nextInt(duration - currentDay + 1) + currentDay - 1);
    }

    public void becomeInfected(){
        this.ifInfected = !ifImmune;
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

    public HashSet<Agent> getFriendList(){
        return World.getInstance().getNet().get(this);
    }

    public int getId(){
        return id;
    }

    @Override
    public abstract String toString();
}
