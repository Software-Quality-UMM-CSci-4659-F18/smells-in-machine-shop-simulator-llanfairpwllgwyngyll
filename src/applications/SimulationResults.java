package applications;

import java.util.Arrays;

public class SimulationResults {
    private int finishTime;
    private int numMachines;
    private int[] numTasksPerMachine;
    private int[] totalWaitTimePerMachine;
    private JobCompletionData[] jobCompletions;
    private int nextJob = 0;

    public SimulationResults(int numJobs) {
        jobCompletions = new JobCompletionData[numJobs];
    }

    public void print() {
        for (JobCompletionData data : jobCompletions) {
            System.out.println("Job " + data.getJobNumber() + " has completed at "
                    + data.getCompletionTime() + " Total wait was " + data.getTotalWaitTime());
        }

        System.out.println("Finish time = " + finishTime);
        for (int p = 1; p <= numMachines; p++) {
            System.out.println("Machine " + p + " completed "
                    + numTasksPerMachine[p] + " tasks");
            System.out.println("The total wait time was "
                    + totalWaitTimePerMachine[p]);
            System.out.println();
        }
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public void setNumMachines(int numMachines) {
        this.numMachines = numMachines;
    }

    public int[] getNumTasksPerMachine() {
        return Arrays.copyOf(numTasksPerMachine, numTasksPerMachine.length);
    }

    public void setNumTasksPerMachine(int[] numTasksPerMachine) {
        this.numTasksPerMachine = numTasksPerMachine;
    }

    public int[] getTotalWaitTimePerMachine() {
        return Arrays.copyOf(totalWaitTimePerMachine, totalWaitTimePerMachine.length);
    }

    public void setTotalWaitTimePerMachine(int[] totalWaitTimePerMachine) {
        this.totalWaitTimePerMachine = totalWaitTimePerMachine;
    }

    public JobCompletionData[] getJobCompletionData() {
        return jobCompletions;
    }

    public void setJobCompletionData(int jobNumber, int completionTime, int totalWaitTime) {
        JobCompletionData jobCompletionData = new JobCompletionData(jobNumber, completionTime, totalWaitTime);
        jobCompletions[nextJob] = jobCompletionData;
        nextJob++;
    }

    public int getLastMachineTime(){
        return this.jobCompletions[jobCompletions.length-1].completionTime;
    }

    public int getJobCompletionTotalWaitTime(){
        int totalJobWaitTime = 0;
        for(JobCompletionData jobCompletionData : this.jobCompletions){
            final int jobWaitTime = jobCompletionData.getTotalWaitTime();
            totalJobWaitTime += jobWaitTime;
        }
        return totalJobWaitTime;
    }

    public int getCompTimeAtIndex(int index){
        return this.jobCompletions[index].completionTime;
    }

    public int getJobCompsLength(){
        return this.jobCompletions.length;
    }


    private static class JobCompletionData {
        private final int completionTime;
        private final int totalWaitTime;
        private final int jobNumber;

        public JobCompletionData(int jobNumber, int completionTime, int totalWaitTime) {
            this.jobNumber = jobNumber;
            this.completionTime = completionTime;
            this.totalWaitTime = totalWaitTime;
        }

        public int getCompletionTime() {
            return completionTime;
        }

        public int getTotalWaitTime() {
            return totalWaitTime;
        }

        public int getJobNumber() {
            return jobNumber;
        }
    }
}
