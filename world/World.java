package world;

import agent.Agent;
import agent.Meeting;
import assistants.Reporter;

import java.util.*;

public class World {
    private static World instance = null;
    private Random random;
    private double meetingProb, infectiousness, recoverability, mortality;
    private int duration, currentDay = 1;
    private Reporter reporter;
    private Map<Agent, HashSet<Agent>> net;
    private ArrayList<LinkedList<Meeting>> timeline = new ArrayList<>();

    public World(){
        // never instanciate
    }

    public static void create(long seed, double meetingProb, double infectiousness, double recoverability, double mortality, Reporter reporter, Map<Agent, HashSet<Agent>> net, int duration){
        if (instance == null) {
            instance = new World();
            instance.random = new Random(seed);
            instance.meetingProb = meetingProb;
            instance.infectiousness = infectiousness;
            instance.recoverability = recoverability;
            instance.mortality = mortality;
            instance.reporter = reporter;
            instance.net = net;
            for (int i = 0; i < duration; i++)
                instance.timeline.add(new LinkedList<>());
            instance.duration = duration;
        }
    }

    public static World getInstance(){
        return instance;
    }

    public void start(){
        while (currentDay <= duration) {
            revolve();
            reporter.noteDay();
            this.currentDay++;
        }
    }

    public void revolve(){

        for (Agent agent : net.keySet()) {
            if (random.nextDouble() < mortality)
                killAgent(agent);
        }

        for (Agent agent : net.keySet()){
            if (random.nextDouble() < recoverability)
                agent.pullThrough();
        }

        for (Agent agent : net.keySet())
            agent.keepMeeting();

        for (Meeting meeting : timeline.get(currentDay))
            meeting.conductMeeting();
    }

    private void killAgent(Agent agent){
        reporter.reportDeath();

        net.remove(agent);
        for (HashSet<Agent> friends : net.values())
            friends.remove(agent);

        for (LinkedList<Meeting> meetings : timeline.subList(currentDay, timeline.size())) {
            //TODO: is removing from meetings while iterating over it safe?
            for (Meeting meeting : meetings) {
                if (meeting.contains(agent))
                    meetings.remove(meeting);
            }
        }
    }

    public void addMeeting(Meeting meeting, int when){
        //TODO: check day numbering
        this.timeline.get(when).add(meeting);
    }

    public Reporter getReporter() {
        return reporter;
    }

    public Random getRandom() {
        return random;
    }

    public double getMeetingProb() {
        return meetingProb;
    }

    public double getInfectiousness() {
        return infectiousness;
    }

    public Map<Agent, HashSet<Agent>> getNet() {
        return net;
    }

    public int getCurrentDay(){
        return currentDay;
    }

    public int getDuration(){
        return duration;
    }
}
