import java.util.*;

public class FcfsScheduler implements Scheduler {

    @Override
    public List<ProcessStats> schedule(List<ProcessInput> processes) {
        // Preserve original order for output
        Map<Integer, ProcessStats> statsMap = new LinkedHashMap<>();
        for (var in : processes) {
            statsMap.put(in.getPid(), new ProcessStats(in));
        }

        // Sort by arrival time, then by PID
        List<ProcessInput> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(
                Comparator.comparingInt(ProcessInput::getArrivalTime)
                        .thenComparingInt(ProcessInput::getPid)
        );

        int currentTime = 0;
        for (var process : sortedProcesses) {
            currentTime = Math.max(currentTime, process.getArrivalTime());
            currentTime += process.getBurstTime();
            statsMap.get(process.getPid()).setCompletionTime(currentTime);
        }

        return new ArrayList<>(statsMap.values());
    }

}
