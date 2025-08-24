public enum CpuAlgo {

    FCFS,
    SJF_NON_PREEMPTIVE,
    ROUND_ROBIN;

    public static CpuAlgo fromChoice(int choice) {
        return switch (choice) {
            case 1 -> FCFS;
            case 2 -> SJF_NON_PREEMPTIVE;
            case 3 -> ROUND_ROBIN;
            default -> throw new IllegalArgumentException("Invalid choice: " + choice);
        };
    }

}
