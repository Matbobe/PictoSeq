package com.pictoseq.models;

import java.util.LinkedList;
import java.util.List;

public class SequentielList {
    private final LinkedList<Sequentiel> sequentielList;

    public SequentielList(List<Sequentiel> sequentielList) {
        this.sequentielList = new LinkedList<>(sequentielList);
    }

    public List<Sequentiel> getPictogrameList() {
        return sequentielList;
    }

    public Sequentiel get(int index) {
        return sequentielList.get(index);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Sequentiel sequentiel : sequentielList) {
            result.append(sequentiel.toString()).append("\n");
        }
        return result.toString();
    }

    public int size() {
        return sequentielList.size();
    }

    public void add(Sequentiel sequentiel) {
        sequentielList.add(sequentiel);
    }
}
