package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.*;

/**
 * The `implementation.FcfsScheduler` class implements the `task.Scheduler` interface and provides
 * a First-Come, First-Served (FCFS) scheduling algorithm for processes.
 * <p>
 * The FCFS scheduling algorithm schedules processes in the order of their
 * arrival time. If two processes have the same arrival time, they are scheduled
 * in the order of their process IDs (PIDs).
 */
public class FcfsScheduler implements Scheduler {

    /**
     * Schedules the given list of processes using the FCFS scheduling algorithm.
     *
     * @param processes A list of `model.ProcessInput` objects representing the processes
     *                  to be scheduled. Each process contains information such as
     *                  arrival time, burst time, and PID.
     * @return A list of `model.ProcessStats` objects containing the scheduling results
     * for each process, including their completion times.
     */
    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {
        // Preserve the original order of processes for output
        Map<Integer, ProcessStats> statsMap = new LinkedHashMap<>();
        for (var in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

        // Sort the processes by arrival time, then by PID
        List<ProcessInput> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(
                Comparator.comparingInt(ProcessInput::getArrivalTime)
                        .thenComparingInt(ProcessInput::getPid)
        );

        int currentTime = 0; // Tracks the current time in the scheduling process

        // Iterate through the sorted processes and calculate their completion times
        for (var process : sortedProcesses) {
            // Ensure the current time is at least the process's arrival time
            currentTime = Math.max(currentTime, process.getArrivalTime());
            // Add the process's burst time to the current time
            currentTime += process.getBurstTime();
            // Update the completion time for the process in the stats map
            statsMap.get(process.getPid()).setCompletionTime(currentTime);
        }

        // Return the scheduling results as a list of model.ProcessStats
        return new ArrayList<>(statsMap.values());
    }

}