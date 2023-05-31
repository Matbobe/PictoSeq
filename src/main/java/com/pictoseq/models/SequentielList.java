package com.pictoseq.models;

public class SequentielList {
    private final Sequentiel[] sequentielList;

    public SequentielList(Sequentiel[] sequentielList) {
        this.sequentielList = sequentielList;
    }

    public Sequentiel[] getPictogrameList() {
        return sequentielList;
    }

    public Sequentiel get(int index) {
        return sequentielList[index];
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Sequentiel sequentiel : sequentielList) {
            result.append(sequentiel.toString()).append("\n");
        }
        return result.toString();
    }
}
