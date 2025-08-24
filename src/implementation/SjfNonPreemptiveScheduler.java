package implementation;

import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;

import java.util.*;

public class SjfNonPreemptiveScheduler implements Scheduler {
    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {

        Map<Integer, ProcessStats> statsMap = new LinkedHashMap<>();
        for (var in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

        List<ProcessInput> remainingProcesses = new ArrayList<>(processes);
        int currentTime = 0;
        int completed = 0;
        int n = remainingProcesses.size();

        while (completed < n) {
            // Collect arrived processes
            List<ProcessInput> arrivedProcesses = new ArrayList<>();
            for (var process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    arrivedProcesses.add(process);
                }
            }

            if (arrivedProcesses.isEmpty()) {
                // Jump to the next arrival
                int nextArrival = remainingProcesses.stream()
                        .mapToInt(ProcessInput::getArrivalTime)
                        .min()
                        .orElse(currentTime);
                currentTime = Math.max(currentTime, nextArrival);
                continue;
            }

            // Choose the shortest burst, tie by arrival time, then PID
            arrivedProcesses.sort(
                    Comparator.comparingInt(ProcessInput::getBurstTime)
                            .thenComparingInt(ProcessInput::getArrivalTime)
                            .thenComparingInt(ProcessInput::getPid)
            );

            ProcessInput chosenProcess = arrivedProcesses.getFirst();
            currentTime += chosenProcess.getBurstTime();
            statsMap.get(chosenProcess.getPid()).setCompletionTime(currentTime);
            completed++;

        }

        return new ArrayList<>(statsMap.values());
    }
}
