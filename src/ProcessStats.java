public class ProcessStats extends ProcessInput {

    public ProcessStats(ProcessInput input) {
        super(input.getPid(), input.getArrivalTime(), input.getBurstTime());
    }


}
