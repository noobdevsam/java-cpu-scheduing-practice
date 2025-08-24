package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.*;

/**
 * The `SjfNonPreemptiveScheduler` class implements the `Scheduler` interface
 * and provides the Shortest Job First (SJF) non-preemptive scheduling algorithm.
 * <p>
 * The SJF non-preemptive scheduling algorithm selects the process with the
 * shortest burst time from the processes that have arrived. If multiple
 * processes have the same burst time, they are scheduled based on their
 * arrival time, and if the arrival time is also the same, they are scheduled
 * by their process IDs (PIDs).
 */
public class SjfNonPreemptiveScheduler implements Scheduler {

    /**
     * Schedules the given list of processes using the SJF non-preemptive
     * scheduling algorithm.
     *
     * @param processes A list of `ProcessInput` objects representing the processes
     *                  to be scheduled. Each process contains information such as
     *                  arrival time, burst time, and PID.
     * @return A list of `ProcessStats` objects containing the scheduling results
     * for each process, including their completion times.
     */
    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {

        // Map to store the scheduling statistics for each process
        Map<Integer, ProcessStats> statsMap = new LinkedHashMap<>();
        for (var in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

        // List of processes that are yet to be scheduled
        List<ProcessInput> remainingProcesses = new ArrayList<>(processes);
        int currentTime = 0; // Tracks the current time in the scheduling process
        int completed = 0;  // Tracks the number of completed processes
        int n = remainingProcesses.size(); // Total number of processes

        // Continue scheduling until all processes are completed
        while (completed < n) {
            // Collect processes that have arrived by the current time
            List<ProcessInput> arrivedProcesses = new ArrayList<>();
            for (var process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    arrivedProcesses.add(process);
                }
            }

            if (arrivedProcesses.isEmpty()) {
                // If no processes have arrived, jump to the next arrival time
                int nextArrival = remainingProcesses.stream()
                        .mapToInt(ProcessInput::getArrivalTime)
                        .min()
                        .orElse(currentTime);
                currentTime = Math.max(currentTime, nextArrival);
                continue;
            }

            // Sort the arrived processes by burst time, then arrival time, then PID
            arrivedProcesses.sort(
                    Comparator.comparingInt(ProcessInput::getBurstTime)
                            .thenComparingInt(ProcessInput::getArrivalTime)
                            .thenComparingInt(ProcessInput::getPid)
            );

            // Select the process with the shortest burst time
            ProcessInput chosenProcess = arrivedProcesses.getFirst();
            // Update the current time by adding the chosen process's burst time
            currentTime += chosenProcess.getBurstTime();
            // Set the completion time for the chosen process
            statsMap.get(chosenProcess.getPid()).setCompletionTime(currentTime);
            completed++; // Increment the count of completed processes
        }

        // Return the scheduling results as a list of ProcessStats
        return new ArrayList<>(statsMap.values());
    }
}