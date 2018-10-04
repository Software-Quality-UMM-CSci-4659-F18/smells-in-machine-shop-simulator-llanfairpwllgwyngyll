package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private int machineNumber;
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    Machine(int machineNumber) {
        jobQ = new LinkedQueue();
        this.machineNumber = machineNumber;
    }

    public LinkedQueue getJobQ() {
        return jobQ;
    }

    public int getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(int totalWait) {
        this.totalWait = totalWait;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }

    public Job getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(Job activeJob) {
        this.activeJob = activeJob;
    }

    public void putJobOnQ(Job theJob){
        jobQ.put(theJob);
    }

    public boolean jobQIsEmpty(){
        return jobQ.isEmpty();
    }

    public void beginNextJob(){
        this.activeJob = (Job) jobQ.remove();
    }

    public void updateTotalWait(){
        this.totalWait = totalWait + MachineShop.timeNow - this.activeJob.getArrivalTime();
    }

    public void incrementNumTasks(){
        this.numTasks = numTasks +1;
    }
    /**
     * change the state of theMachine
     *
     * @return last job run on this machine
     */
    public Job changeState() {// Task on theMachine has finished,
        // schedule next one.
        Job lastJob;
        if (this.activeJob== null) {// in idle or change-over
            // state
            lastJob = null;
            // wait over, ready for new job
            if (this.jobQIsEmpty()) // no waiting job
                MachineShop.eList.setFinishTime(this.machineNumber, MachineShop.largeTime);
            else {// take job off the queue and work on it
                this.beginNextJob();
                this.updateTotalWait();
                this.incrementNumTasks();
                int t = this.getActiveJob().removeNextTask();
                MachineShop.eList.setFinishTime(this.machineNumber, MachineShop.timeNow + t);
            }
        } else {// task has just finished on machineArray[theMachine]
            // schedule change-over time
            lastJob = this.getActiveJob();
            this.setActiveJob(null);
            MachineShop.eList.setFinishTime(this.machineNumber, MachineShop.timeNow
                    + this.getChangeTime());
        }

        return lastJob;
    }

}
