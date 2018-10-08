/** machineArray shop simulation */

package applications;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";

    // methods
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    static boolean moveToNextMachine(Job theJob, SimulationResults simulationResults) {
        if (theJob.getTaskQ().isEmpty()) {// no next task
            simulationResults.setJobCompletionData(theJob.getId(), MachineShop.timeNow, MachineShop.timeNow - theJob.getLength());
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int p = theJob.getFirstTaskMachine();
            // put on machine p's wait queue
            MachineShop.machineArray[p].putJobOnQ(theJob);
            theJob.setArrivalTime(MachineShop.timeNow);
            // if p idle, schedule immediately
            if (MachineShop.eList.nextEventTime(p) == MachineShop.largeTime) {// machineArray is idle
                MachineShop.getMachine(p).changeState();
            }
            return true;
        }
    }


    /** process all jobs to completion
     * @param simulationResults*/
    static void simulate(SimulationResults simulationResults) {
        while (MachineShop.numJobs > 0) {// at least one job left
            int nextToFinish = MachineShop.eList.nextEventMachine();
            MachineShop.timeNow = MachineShop.eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = MachineShop.getMachine(nextToFinish).changeState();
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob, simulationResults))
                MachineShop.numJobs--;
        }
    }

    /** output wait times at machines
     * @param simulationResults*/
    static void outputStatistics(SimulationResults simulationResults) {
        simulationResults.setFinishTime(MachineShop.timeNow);
        simulationResults.setNumMachines(MachineShop.numMachines);
        setNumTasksPerMachine(simulationResults);
        setTotalWaitTimePerMachine(simulationResults);
    }

    private static void setTotalWaitTimePerMachine(SimulationResults simulationResults) {
        int[] totalWaitTimePerMachine = new int[MachineShop.numMachines+1];
        for (int i=1; i<=MachineShop.numMachines; ++i) {
            totalWaitTimePerMachine[i] = MachineShop.machineArray[i].getTotalWait();
        }
        simulationResults.setTotalWaitTimePerMachine(totalWaitTimePerMachine);
    }

    private static void setNumTasksPerMachine(SimulationResults simulationResults) {
        int[] numTasksPerMachine = new int[MachineShop.numMachines+1];
        for (int i=1; i<=MachineShop.numMachines; ++i) {
            numTasksPerMachine[i] = MachineShop.machineArray[i].getNumTasks();
        }
        simulationResults.setNumTasksPerMachine(numTasksPerMachine);
    }

    public static SimulationResults runSimulation(SimulationSpecification specification) {
        MachineShop.largeTime = Integer.MAX_VALUE;
        MachineShop.timeNow = 0;
        MachineShop.startShop(specification); // initial machine loading
        SimulationResults simulationResults = new SimulationResults(MachineShop.numJobs);
        simulate(simulationResults); // run all jobs through shop
        outputStatistics(simulationResults);
        return simulationResults;
    }

}
