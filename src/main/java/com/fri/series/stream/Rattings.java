package com.fri.series.stream;

public class Rattings {

    private int id;
    private int episodeId;
    private int userId;
    private double ratting;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getEpisodeId() {
        return episodeId;
    }
    public void setEpisodeId(int episode) {
        this.episodeId = episode;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int id) {
        this.userId = id;
    }

    public double getRatting() {
        return ratting;
    }
    public void setRatting(double ratting) {
        this.ratting = ratting;
    }

    public Rattings(int id, int ep, int user, double ratting){
        setId(id);
        setEpisodeId(ep);
        setUserId(user);
        setRatting(ratting);
    }
}
