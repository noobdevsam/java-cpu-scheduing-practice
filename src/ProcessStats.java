public class ProcessStats extends ProcessInput {

    public int completionTime;
    public int turnaroundTime;
    public int waitingTime;

    public ProcessStats(ProcessInput input) {
        super(input.getPid(), input.getArrivalTime(), input.getBurstTime());
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
        this.turnaroundTime = this.completionTime - getArrivalTime();
        this.waitingTime = this.turnaroundTime - getBurstTime();
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

}
