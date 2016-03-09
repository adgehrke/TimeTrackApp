package com.example.adriangehrke.timetrackapp.models;

/**
 * Created by admin on 05.03.2016.
 */
public class Project {

    private int id;
    private String name;

    public Project(){

    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){ this.name = name; }

    @Override
    public String toString() {
        return name;
    }
}
