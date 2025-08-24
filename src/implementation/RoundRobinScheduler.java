package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.*;

/**
 * The `RoundRobinScheduler` class implements the `Scheduler` interface
 * and provides a Round Robin scheduling algorithm for processes.
 * <p>
 * The Round Robin scheduling algorithm assigns a fixed time quantum to each
 * process in the ready queue. Processes are executed in a cyclic order, and
 * if a process is not completed within its time quantum, it is re-enqueued
 * for the next cycle.
 */
public class RoundRobinScheduler implements Scheduler {

    private final int timeQuantum;

    /**
     * Constructs a `RoundRobinScheduler` with the specified time quantum.
     *
     * @param timeQuantum The fixed time quantum for the Round Robin scheduling algorithm.
     */
    public RoundRobinScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    /**
     * Schedules the given list of processes using the Round Robin scheduling algorithm.
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
        for (ProcessInput in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

        // Sort processes by arrival time, then by PID
        List<ProcessInput> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(
                Comparator.comparingInt(ProcessInput::getArrivalTime)
                        .thenComparingInt(ProcessInput::getPid)
        );

        int n = sortedProcesses.size();
        int[] remainingBurstTimes = new int[n];
        for (int i = 0; i < n; i++) {
            remainingBurstTimes[i] = sortedProcesses.get(i).getBurstTime();
        }

        // Queue to manage the order of processes for execution
        Queue<Integer> queue = new ArrayDeque<>();
        int currentTime = 0;
        int index = 0;

        // Enqueue the first process or jump to the earliest arrival time
        if (index < n && sortedProcesses.get(index).getArrivalTime() > currentTime) {
            currentTime = sortedProcesses.get(index).getArrivalTime();
        }
        while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
            queue.offer(index++);
        }

        // Process the queue until all processes are completed
        while (!queue.isEmpty()) {
            int i = queue.poll();
            ProcessInput process = sortedProcesses.get(i);

            // Execute the process for the time quantum or until completion
            int executionTime = Math.min(timeQuantum, remainingBurstTimes[i]);
            remainingBurstTimes[i] -= executionTime;
            currentTime += executionTime;

            // Enqueue newly arrived processes
            while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                queue.offer(index++);
            }

            if (remainingBurstTimes[i] > 0) {
                // If the process is not finished, re-enqueue it
                queue.offer(i);
            } else {
                // If the process is finished, set its completion time
                statsMap.get(process.getPid()).setCompletionTime(currentTime);
            }

            // If the queue is empty but there are processes yet to arrive
            if (queue.isEmpty() && index < n) {
                // Jump to the next process arrival time
                currentTime = Math.max(currentTime, sortedProcesses.get(index).getArrivalTime());
                while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                    queue.offer(index++);
                }
            }
        }

        // Return the scheduling results as a list of ProcessStats
        return new ArrayList<>(statsMap.values());
    }

}