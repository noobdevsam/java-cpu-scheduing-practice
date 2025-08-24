package util;

import model.ProcessStats;

import java.util.List;
import java.util.function.ToIntFunction;

public class TablePrinter {

    public static void print(List<ProcessStats> processes) {
        if (processes.isEmpty()) {
            System.out.println("No processes to display.");
            return;
        }

        String[] headers = {
                "PID", "Arrival Time", "Burst Time",
                "Completion Time", "Turnaround Time", "Waiting Time"
        };
        int[] widths = new int[headers.length];

        // Initialize widths with header lengths
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        // Update widths based on data
        updateWidths(widths, 0, processes, p -> p.getPid());
        updateWidths(widths, 1, processes, p -> p.getArrivalTime());
        updateWidths(widths, 2, processes, p -> p.getBurstTime());
        updateWidths(widths, 3, processes, p -> p.getCompletionTime());
        updateWidths(widths, 4, processes, p -> p.getTurnaroundTime());
        updateWidths(widths, 5, processes, p -> p.getWaitingTime());

        int totalWidth = 0;
        for (int w : widths) {
            totalWidth += w + 3; // 3 for padding and separator
        }
        String line = "-".repeat(Math.max(totalWidth, 10));
    }

    private static void updateWidths(
            int[] widths,
            int index,
            List<ProcessStats> stats,
            ToIntFunction<ProcessStats> function
    ) {
        for (ProcessStats p : stats) {
            widths[index] = Math.max(
                    widths[index],
                    String.valueOf(function.applyAsInt(p)).length()
            );
        }
    }
}
