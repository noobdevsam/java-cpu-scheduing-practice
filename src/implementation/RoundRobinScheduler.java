package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.List;

public class RoundRobinScheduler implements Scheduler {

    private final int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {
        return List.of();
    }

}
