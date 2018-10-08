package applications;

public class MachineShop {
    public static int timeNow; // current time
    public static Machine[] machineArray; // array of machines
    public static int numMachines; // number of machines
    // data members of MachineShopSimulator
    public static int numJobs; // number of jobs
    public static EventList eList; // pointer to event list
    public static int largeTime; // all machines finish before this


    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        final SpecificationReader specificationReader = new SpecificationReader();
        SimulationSpecification specification = specificationReader.readSpecification();
        SimulationResults simulationResults = MachineShopSimulator.runSimulation(specification);
        simulationResults.print();
    }

    /** load first jobs onto each machine
     * @param specification*/
    static void startShop(SimulationSpecification specification) {
        // Move this to startShop when ready
        numMachines = specification.getNumMachines();
        numJobs = specification.getNumJobs();
        createEventAndMachineQueues(specification);

        // Move this to startShop when ready
        setMachineChangeOverTimes(specification);

        // Move this to startShop when ready
        setUpJobs(specification);

        for (int p = 1; p <= numMachines; p++)
            machineArray[p].changeState();
    }

    private static void createEventAndMachineQueues(SimulationSpecification specification) {
        // create event and machine queues
        eList = new EventList(specification.getNumMachines(), largeTime);
        machineArray = new Machine[specification.getNumMachines() + 1];
        for (int i = 1; i <= specification.getNumMachines(); i++)
            machineArray[i] = new Machine(i);
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
            machineArray[firstMachine].putJobOnQ(theJob);
        }
    }

    //gets a machine using its machine number
    public static Machine getMachine(int machineNum){
        return machineArray[machineNum];
    }
}
