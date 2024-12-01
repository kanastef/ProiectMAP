package org.example.proiect_gradle.Domain;

import java.util.ArrayList;
import java.util.List;

public class User extends Account implements Identifiable {
    private int id;
    private double score;
    protected List<Integer> favourites;
    protected List<Integer> listedProducts;
    public int nrOfFlaggedActions;

    public User(String userName, String password, String email, String phone, double score){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.score=score;
        this.favourites=new ArrayList<>();
        this.listedProducts=new ArrayList<>();
        this.nrOfFlaggedActions=0;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<Integer> getFavourites() {
        return favourites;
    }

    public List<Integer> getListedProducts(){
        return listedProducts;
    }

    public void incrementFlaggedActions() {
        this.nrOfFlaggedActions++;
    }

    // Getter for flagged actions
    public int getFlaggedActions() {
        return nrOfFlaggedActions;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
