package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    Machine() {
        jobQ = new LinkedQueue();
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

    public void updateTotalWait(int timeNow){
        this.totalWait = totalWait +timeNow - this.activeJob.getArrivalTime();
    }

    public void incrementNumTasks(){
        this.numTasks = numTasks +1;
    }
}
