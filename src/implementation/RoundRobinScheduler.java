package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.List;

public class RoundRobinScheduler implements Scheduler {

    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {
        return List.of();
    }

}
