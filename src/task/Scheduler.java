package task;

import model.ProcessInput;
import model.ProcessStats;

import java.util.List;

public interface Scheduler {
    List<ProcessStats> schedule(List<ProcessInput> processes);
}
