import implementation.FcfsScheduler;
import implementation.RoundRobinScheduler;
import implementation.SjfNonPreemptiveScheduler;
import model.CpuAlgo;
import model.ProcessInput;
import model.ProcessStats;
import task.Scheduler;
import util.TablePrinter;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * CPU Scheduling Simulator for:
 * 1. First-Come First-Served (FCFS)
 * 2. Shortest Job First Non-Preemptive (SJF Non-Preemptive)
 * 3. Round Robin
 * <p>
 * This application allows the user to simulate CPU scheduling algorithms by
 * entering process details and selecting an algorithm. The results include
 * process statistics such as completion time, turnaround time, and waiting time.
 */
public class CpuSchedulingApp {

    /**
     * The main method serves as the entry point for the CPU Scheduling Simulator.
     * It prompts the user to select a scheduling algorithm, input process details,
     * and displays the scheduling results in a tabular format.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("CPU Scheduling Algorithms:");
        System.out.println("1. FCFS");
        System.out.println("2. SJF (Non-Preemptive)");
        System.out.println("3. Round Robin");
        System.out.print("Enter your choice (1/2/3): ");

        int choice = readInt(sc);
        CpuAlgo algorithm;
        try {
            algorithm = CpuAlgo.fromChoice(choice);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid choice. Exiting.");
            return;
        }

        System.out.print("Enter number of processes: ");
        int n = readInt(sc);
        if (n <= 0) {
            System.out.println("Number of processes must be > 0. Exiting.");
            return;
        }

        List<ProcessInput> inputs = new ArrayList<>();
        System.out.println("Enter process details (PID ArrivalTime BurstTime)");
        for (int i = 0; i < n; i++) {
            System.out.printf("Process %d: ", (i + 1));
            int pid = sc.nextInt();
            int arrival = sc.nextInt();
            int burst = sc.nextInt();
            try {
                inputs.add(new ProcessInput(pid, arrival, burst));
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid values: " + ex.getMessage());
                i--;
            }
        }

        Scheduler scheduler;
        switch (algorithm) {
            case FCFS -> scheduler = new FcfsScheduler();
            case SJF_NON_PREEMPTIVE -> scheduler = new SjfNonPreemptiveScheduler();
            case ROUND_ROBIN -> {
                System.out.print("Enter Time Quantum: ");
                int tq = readInt(sc);
                try {
                    scheduler = new RoundRobinScheduler(tq);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid time quantum. Exiting.");
                    return;
                }
            }
            default -> throw new IllegalStateException("Unexpected algorithm: " + algorithm);
        }

        List<ProcessStats> results = scheduler.schedule(inputs);
        System.out.println();
        System.out.println("Scheduling Algorithm: " + algorithmReadableName(algorithm));
        TablePrinter.print(results);
    }

    /**
     * Reads an integer from the user input, prompting again if the input is invalid.
     *
     * @param sc The `Scanner` object used to read user input.
     * @return The integer value entered by the user.
     */
    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.next(); // discard invalid input
                System.out.print("Invalid integer. Try again: ");
            }
        }
    }

    /**
     * Converts a `CpuAlgo` enumeration value to a human-readable name.
     *
     * @param alg The `CpuAlgo` enumeration value representing the selected algorithm.
     * @return A string representing the human-readable name of the algorithm.
     */
    private static String algorithmReadableName(CpuAlgo alg) {
        return switch (alg) {
            case FCFS -> "First-Come First-Served (FCFS)";
            case SJF_NON_PREEMPTIVE -> "Shortest Job First (Non-Preemptive)";
            case ROUND_ROBIN -> "Round Robin";
        };
    }
}

/*
Sample Input / Output (illustrative):

CPU Scheduling Algorithms:
1. FCFS
2. SJF (Non-Preemptive)
3. Round Robin
Enter your choice (1/2/3): 1
Enter number of processes: 4
Enter process details (PID ArrivalTime BurstTime)
Process 1: 1 0 5
Process 2: 2 1 3
Process 3: 3 2 8
Process 4: 4 3 6

Scheduling Algorithm: First-Come First-Served (FCFS)
-----------------------------------------------------------
PID  Arrival  Burst  Completion  Turnaround  Waiting
-----------------------------------------------------------
1    0        5      5           5           0
2    1        3      8           7           4
3    2        8      16          14          6
4    3        6      22          19          13
-----------------------------------------------------------
[Average Turnaround Time = 11.25]
[Average Waiting Time = 5.75]

For Round Robin (time quantum e.g., 3) the order and completion times will differ.
*/