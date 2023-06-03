package com.pictoseq.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class SequentielList extends ArrayList<Sequentiel> implements Serializable {
    private final LinkedList<Sequentiel> sequentielList;

    public SequentielList(List<Sequentiel> sequentielList) {
        this.sequentielList = new LinkedList<>(sequentielList);
    }

    public SequentielList() {
        this.sequentielList = new LinkedList<>();
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

    public boolean add(Sequentiel sequentiel) {
        sequentielList.add(sequentiel);
        return false;
    }

}
