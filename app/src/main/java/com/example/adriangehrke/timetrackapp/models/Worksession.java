package com.example.adriangehrke.timetrackapp.models;

/**
 * Created by admin on 05.03.2016.
 */
public class Worksession {

    private int id;
    private int projectId;
    private int duration;

    public Worksession(){

    }


    public Worksession(int id, int projectId, int duration) {
        this.id = id;
        this.projectId = projectId;
        this.duration = duration;
    }


    public int getId() { return id; }
    public int getProjectId() { return projectId; }
    public int getDuration() { return duration; }



    public void setId(int id) {
        this.id = id;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return projectId+" ";
    }
}
