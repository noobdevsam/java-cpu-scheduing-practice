package util;

import model.ProcessStats;

import java.util.List;
import java.util.function.ToIntFunction;

public class TablePrinter {

    public static void print(List<ProcessStats> processes) {
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
