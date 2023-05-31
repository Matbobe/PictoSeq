package com.pictoseq.controllers;

import com.pictoseq.models.SequentielList;

public interface PersistentModelManager {
   public void  save(SequentielList model);
    public SequentielList load();
}
