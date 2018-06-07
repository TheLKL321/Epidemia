package agent;

import world.World;

import java.util.Random;

public class Meeting {
    private Agent agent1, agent2;

    public Meeting(Agent agent1, Agent agent2) {
        this.agent1 = agent1;
        this.agent2 = agent2;
    }

    public void conductMeeting(){
        Random random = World.getInstance().getRandom();
        double infectiousness = World.getInstance().getInfectiousness();
        if (agent1.ifInfected()){
            if (!agent2.ifInfected()){
                if (random.nextDouble() < infectiousness)
                    agent2.becomeInfected();
            }
        } else if (agent2.ifInfected()){
            if (random.nextDouble() < infectiousness)
                agent1.becomeInfected();
        }
    }

    public boolean contains(Agent agent){
        return agent.getId() == agent1.getId() || agent.getId() == agent2.getId();
    }
}
