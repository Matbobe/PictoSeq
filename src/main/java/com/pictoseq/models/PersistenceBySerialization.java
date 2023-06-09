package com.pictoseq.models;

import com.pictoseq.controllers.PersistentModelManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersistenceBySerialization implements PersistentModelManager {
    private String path;

    public PersistenceBySerialization() {
        String userDirectory = System.getProperty("user.home");
        Path path = Paths.get(userDirectory, "AppData", "Local", "PictoSeq");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            Log.printError("Error while creating save directory");
            e.printStackTrace();
        }
        path = Paths.get(path.toString(), "save");
        this.path = path.toString();
    }

    @Override
    public void save(SequentielList model) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path)))
        {
            objectOutputStream.writeObject(model);
            objectOutputStream.flush();
            Log.println("Save file saved");
        } catch (IOException e) {
            Log.printError("Saving file error");
            e.printStackTrace();
        }
    }

    @Override
    public SequentielList load() {
        SequentielList model = null;

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(path)))
        {
            model = (SequentielList) input.readObject();
            Log.println("Save file loaded");
        } catch (IOException e) {
            Log.printError("Save file does not exist");
            Log.printError("Creation of empty model");
            model = new SequentielList();
        } catch (ClassNotFoundException e) {
            Log.printError("Loading save file error");
        }
        return model;
    }
}
