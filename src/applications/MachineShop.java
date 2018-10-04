package applications;

public class MachineShop {
    // data members of MachineShopSimulator
    public static int timeNow; // current time
    static int numMachines; // number of machines
    static int numJobs; // number of jobs

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
}
