package com.fri.series.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RattingsDatabase {
    static Rattings it1 = new Rattings(1,1,1,2.5);
    static Rattings it2 = new Rattings(2,1,2,3);
    static Rattings big1 = new Rattings(3,2,1,5);
    static Rattings big2 = new Rattings(4,2,3,5);
    private static List<Rattings> rattings = Arrays.asList(it1, it2, big1, big2);

    public static List<Rattings> getRattings() {
        System.out.println("List getted"); return rattings;
    }

    public static Rattings getRatting(int id) {
        for (Rattings ratting : rattings) {
            if (ratting.getId() == (id))
                return ratting;
        }

        return null;
    }

    public static void addRatting(Rattings ratting) {
        rattings.add(ratting);
    }

    public static void deleteRatting(int id) {
        for (Rattings ratting : rattings) {
            if (ratting.getId() == (id)){
                rattings.remove(ratting);
                break;
            }
        }
    }

    public static List<Rattings> getRattingsFromUser(int id) {
        return rattings.stream().filter(p -> p.getUserId() == id).collect(Collectors.toList());
    }

    public static List<Rattings> getRattingsFromEpisode(int id) {
        return rattings.stream().filter(p -> p.getEpisodeId() == id).collect(Collectors.toList());
    }
}
