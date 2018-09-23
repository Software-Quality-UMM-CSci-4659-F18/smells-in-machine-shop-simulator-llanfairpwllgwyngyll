package applications;

import java.util.Arrays;

public class SimulationSpecification {
    private int numMachines;
    private int numJobs;
    private int[] changeOverTimes;
    private JobSpecification[] jobSpecifications;

    public void setNumMachines(int numMachines) {
        this.numMachines = numMachines;
    }

    public void setNumJobs(int numJobs) {
        this.numJobs = numJobs;
        setJobSpecificationLength(numJobs);
    }

    public int getNumMachines() {
        return numMachines;
    }

    public int getNumJobs() {
        return numJobs;
    }

    public void setChangeOverTimes(int[] changeOverTimes) {
        this.changeOverTimes = changeOverTimes;
    }

    public int getChangeOverTimes(int machineNumber) {
        return changeOverTimes[machineNumber];
    }

    public void setSpecificationsForTasks(int jobNumber, int[] specificationsForTasks) {
        jobSpecifications[jobNumber].setSpecificationsForTasks(specificationsForTasks);
    }
//
//    public void setJobSpecification(JobSpecification[] jobSpecifications) {
//        this.jobSpecifications = jobSpecifications;
//    }
//
//    public void setJobSpecification(int tasksNumber, int[] tasks) {
//        this.jobSpecifications = jobSpecifications;
//    }
//
//    public JobSpecification getJobSpecifications(int jobNumber) {
//        return jobSpecifications[jobNumber];
//    }

    public void setJobSpecificationLength(int length) {
        JobSpecification[] jobSpecifications = new JobSpecification[length+1];
        for (int i=1; i <= length; i++) {
            jobSpecifications[i] = new JobSpecification();
        }

        this.jobSpecifications = jobSpecifications;
    }


    public void makeJobSpecNumtasks(int jobNumber, int numTasks){
        this.jobSpecifications[jobNumber].setNumTasks(numTasks);
    }
    public void makeJobSpecTasks(int jobNumber, int[] tasks){
        this.jobSpecifications[jobNumber].setSpecificationsForTasks(tasks);
    }
    public int getJobNumTask(int jobNumber){
        return this.jobSpecifications[jobNumber].getNumTasks();
    }
    public int[] getJobSpecsForTasks(int jobNumber){
        return this.jobSpecifications[jobNumber].getSpecificationsForTasks();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<").append(numMachines).append(" machines, ");
        builder.append(numJobs).append(" jobs; ");
        builder.append("change overs: ").append(Arrays.toString(changeOverTimes));
        for (int i=1; i<=numJobs; ++i) {
            builder.append("; job ").append(i).append(" tasks: ");
            builder.append(Arrays.toString(jobSpecifications[i].getSpecificationsForTasks()));
        }

        builder.append(">");
        return builder.toString();
    }


    private static class JobSpecification {
        private int numTasks;
        private int[] specificationsForTasks;

        public void setNumTasks(int numTasks) {
            this.numTasks = numTasks;
        }

        public int getNumTasks() {
            return numTasks;
        }

        public void setSpecificationsForTasks(int[] specificationsForTasks) {
            this.specificationsForTasks = specificationsForTasks;
        }

        public int[] getSpecificationsForTasks() {
            return specificationsForTasks;
        }
    }
}
