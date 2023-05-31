package com.pictoseq.models;

import com.pictoseq.controllers.PersistentModelManager;

import java.io.*;

public class PersistenceBySerialization implements PersistentModelManager {
    private static final String SAVE_FILE = "saveFile";
    @Override
    public void save(SequentielList model) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE)))
        {
            objectOutputStream.writeObject(model);
            objectOutputStream.flush();
            System.out.println("SAVE OK");
        } catch (IOException e) {
            System.err.println("Saving file error");
        }
    }

    @Override
    public SequentielList load() {
        SequentielList model = null;

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(SAVE_FILE)))
        {
            model = (SequentielList) input.readObject();
            System.out.println("LOAD OK");
        } catch (IOException e) {
            System.err.println("Save file does not exist");
            System.err.println("Creation of empty model");
            model = new SequentielList();
        } catch (ClassNotFoundException e) {
            System.err.println("Loading save file error");
        }
        return model;
    }
}
