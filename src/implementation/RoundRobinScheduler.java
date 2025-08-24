package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.*;

public class RoundRobinScheduler implements Scheduler {

    private final int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {

        Map<Integer, ProcessStats> statsMap = new LinkedHashMap<>();
        for (ProcessInput in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

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

        Queue<Integer> queue = new ArrayDeque<>();
        int currentTime = 0;
        int index = 0;

        // Enqueue first arrival at time 0 (or the earliest arrival time)
        if (index < n && sortedProcesses.get(index).getArrivalTime() > currentTime) {
            currentTime = sortedProcesses.get(index).getArrivalTime();
        }
        while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
            queue.offer(index++);
        }

        while (!queue.isEmpty()) {
            int i = queue.poll();
            ProcessInput process = sortedProcesses.get(i);

            int executionTime = Math.min(timeQuantum, remainingBurstTimes[i]);
            remainingBurstTimes[i] -= executionTime;
            currentTime += executionTime;

            // Enqueue newly arrived processes that have come by
            while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                queue.offer(index++);
            }

            if (remainingBurstTimes[i] > 0) {
                // Not finished, re-enqueue
                queue.offer(i);
            } else {
                // Finished
                statsMap.get(process.getPid()).setCompletionTime(currentTime);
            }

            // If queue is empty but there are still processes not yet arrived
            if (queue.isEmpty() && index < n) {
                // Jump to the next process arrival time
                currentTime = Math.max(currentTime, sortedProcesses.get(index).getArrivalTime());
                while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                    queue.offer(index++);
                }
            }
        }

        return new ArrayList<>(statsMap.values());
    }

}
