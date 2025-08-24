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

        // Print header
        System.out.println(line);
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("%-" + widths[i] + "s | ", headers[i]);
        }
        System.out.println();
        System.out.println(line);

        // Print rows in provided order
        double sumTAT = 0;
        double sumWT = 0;
        for (ProcessStats p : processes) {
            System.out.printf("%-" + widths[0] + "d | ", p.getPid());
            System.out.printf("%-" + widths[1] + "d | ", p.getArrivalTime());
            System.out.printf("%-" + widths[2] + "d | ", p.getBurstTime());
            System.out.printf("%-" + widths[3] + "d | ", p.getCompletionTime());
            System.out.printf("%-" + widths[4] + "d | ", p.getTurnaroundTime());
            System.out.printf("%-" + widths[5] + "d | ", p.getWaitingTime());
            System.out.println();
            sumTAT += p.getTurnaroundTime();
            sumWT += p.getWaitingTime();
        }

        System.out.println(line);

        double averageTAT = sumTAT / processes.size();
        double averageWT = sumWT / processes.size();
        System.out.printf("Average Turnaround Time: %.2f\n", averageTAT);
        System.out.printf("Average Waiting Time: %.2f\n", averageWT);
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
