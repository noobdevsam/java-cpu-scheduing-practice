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
 * User enters:
 * - Number of processes
 * - For each: Process ID (int), Arrival Time (int), Burst Time (int)
 * - For Round Robin: Time Quantum
 * <p>
 * Outputs table with:
 * PID, Arrival, Burst, Completion, Turnaround, Waiting, plus averages.
 */
public class CpuSchedulingApp {

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

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException ex) {
                sc.next(); // discard
                System.out.print("Invalid integer. Try again: ");
            }
        }
    }

    private static String algorithmReadableName(CpuAlgo alg) {
        return switch (alg) {
            case FCFS -> "First-Come First-Served (FCFS)";
            case SJF_NON_PREEMPTIVE -> "Shortest Job First (Non-Preemptive)";
            case ROUND_ROBIN -> "Round Robin";
        };
    }
}