/** machineArray shop simulation */

package applications;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machineArray number or task time";

    // data members of MachineShopSimulator
    public static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    private static EventList eList; // pointer to event list
    private static Machine[] machineArray; // array of machines
    private static int largeTime; // all machines finish before this

    // methods
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    static boolean moveToNextMachine(Job theJob, SimulationResults simulationResults) {
        if (theJob.getTaskQ().isEmpty()) {// no next task
            simulationResults.setJobCompletionData(theJob.getId(), timeNow, timeNow - theJob.getLength());
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int p = theJob.getFirstTaskMachine();
            // put on machine p's wait queue
            machineArray[p].getJobQ().put(theJob);
            theJob.setArrivalTime(timeNow);
            // if p idle, schedule immediately
            if (eList.nextEventTime(p) == largeTime) {// machineArray is idle
                changeState(p);
            }
            return true;
        }
    }

    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    static Job changeState(int machineWithFinishedTask) {// Task on theMachine has finished,
                                            // schedule next one.
        Job lastJob;
        if (machineArray[machineWithFinishedTask].getActiveJob() == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            if (machineArray[machineWithFinishedTask].getJobQ().isEmpty()) // no waiting job
                eList.setFinishTime(machineWithFinishedTask, largeTime);
            else {// take job off the queue and work on it
                machineArray[machineWithFinishedTask].setActiveJob((Job) machineArray[machineWithFinishedTask].getJobQ()
                        .remove());
                machineArray[machineWithFinishedTask].setTotalWait(machineArray[machineWithFinishedTask].getTotalWait() + timeNow
                        - machineArray[machineWithFinishedTask].getActiveJob().getArrivalTime());
                machineArray[machineWithFinishedTask].setNumTasks(machineArray[machineWithFinishedTask].getNumTasks() + 1);
                int t = machineArray[machineWithFinishedTask].getActiveJob().removeNextTask();
                eList.setFinishTime(machineWithFinishedTask, timeNow + t);
            }
        } else {// task has just finished on machineArray[theMachine]
                // schedule change-over time
            lastJob = machineArray[machineWithFinishedTask].getActiveJob();
            machineArray[machineWithFinishedTask].setActiveJob(null);
            eList.setFinishTime(machineWithFinishedTask, timeNow
                    + machineArray[machineWithFinishedTask].getChangeTime());
        }

        return lastJob;
    }

    private static void setMachineChangeOverTimes(SimulationSpecification specification) {
        for (int i = 1; i<=specification.getNumMachines(); ++i) {
            machineArray[i].setChangeTime(specification.getChangeOverTimes(i));
        }
    }

    private static void setUpJobs(SimulationSpecification specification) {
        // input the jobs
        Job theJob;
        for (int i = 1; i <= specification.getNumJobs(); i++) {
            int tasks = specification.getJobNumTask(i);
            int firstMachine = 0; // machine for first task

            // create the job
            theJob = new Job(i);
            for (int j = 1; j <= tasks; j++) {
                int theMachine = specification.getJobSpecsForTasks(i)[2*(j-1)+1];
                int theTaskTime = specification.getJobSpecsForTasks(i)[2*(j-1)+2];
                if (j == 1)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            machineArray[firstMachine].getJobQ().put(theJob);
        }
    }

    private static void createEventAndMachineQueues(SimulationSpecification specification) {
        // create event and machine queues
        eList = new EventList(specification.getNumMachines(), largeTime);
        machineArray = new Machine[specification.getNumMachines() + 1];
        for (int i = 1; i <= specification.getNumMachines(); i++)
            machineArray[i] = new Machine();
    }

    /** load first jobs onto each machine
     * @param specification*/
    static void startShop(SimulationSpecification specification) {
        // Move this to startShop when ready
        MachineShopSimulator.numMachines = specification.getNumMachines();
        MachineShopSimulator.numJobs = specification.getNumJobs();
        createEventAndMachineQueues(specification);

        // Move this to startShop when ready
        setMachineChangeOverTimes(specification);

        // Move this to startShop when ready
        setUpJobs(specification);

        for (int p = 1; p <= numMachines; p++)
            changeState(p);
    }

    /** process all jobs to completion
     * @param simulationResults*/
    static void simulate(SimulationResults simulationResults) {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob, simulationResults))
                numJobs--;
        }
    }

    /** output wait times at machines
     * @param simulationResults*/
    static void outputStatistics(SimulationResults simulationResults) {
        simulationResults.setFinishTime(timeNow);
        simulationResults.setNumMachines(numMachines);
        setNumTasksPerMachine(simulationResults);
        setTotalWaitTimePerMachine(simulationResults);
    }

    private static void setTotalWaitTimePerMachine(SimulationResults simulationResults) {
        int[] totalWaitTimePerMachine = new int[numMachines+1];
        for (int i=1; i<=numMachines; ++i) {
            totalWaitTimePerMachine[i] = machineArray[i].getTotalWait();
        }
        simulationResults.setTotalWaitTimePerMachine(totalWaitTimePerMachine);
    }

    private static void setNumTasksPerMachine(SimulationResults simulationResults) {
        int[] numTasksPerMachine = new int[numMachines+1];
        for (int i=1; i<=numMachines; ++i) {
            numTasksPerMachine[i] = machineArray[i].getNumTasks();
        }
        simulationResults.setNumTasksPerMachine(numTasksPerMachine);
    }

    public static SimulationResults runSimulation(SimulationSpecification specification) {
        largeTime = Integer.MAX_VALUE;
        timeNow = 0;
        startShop(specification); // initial machine loading
        SimulationResults simulationResults = new SimulationResults(numJobs);
        simulate(simulationResults); // run all jobs through shop
        outputStatistics(simulationResults);
        return simulationResults;
    }

}
