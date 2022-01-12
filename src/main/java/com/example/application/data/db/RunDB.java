package com.example.application.data.db;

import com.example.application.data.entity.Run;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Logger;

public class RunDB {

    private static final String DATABASE_NAMESPACE = "runs";

    public static DatabaseReference getRunsDb() {
        return Firebase.getDb().child(DATABASE_NAMESPACE);
    }

    public static void maybeCreateInitialData(DataSnapshot snapshot) {
        if (snapshot.hasChild(DATABASE_NAMESPACE)) {
            return;
        }

        add(new Run("Paul", 34.4, 120, LocalDate.now().toEpochDay()));
        add(new Run("Basti", 20.4, 10, LocalDate.now().toEpochDay()));
    }

    public static void add(Run run) {
        getRunsDb().push().setValue(run)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Stored Run succesfully");
                    // Write was successful!
                    // ...
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error while storing run:");
                        e.printStackTrace();
                        // Write failed
                        // ...
                    }
                });;
    }

    protected static Logger getLogger() {
        return Logger.getLogger("RunsDB");
    }

    public static void update(String key, Run run) {
        getLogger().info("Set run " + key + " to " + run);
        HashMap<String, Object> toUpdate = new HashMap<>();
        toUpdate.put(key, run);
        getRunsDb().updateChildrenAsync(toUpdate);
    }

    public static void delete(Run run) {
        getLogger().info("Delete user " + run);
        HashMap<String, Object> toUpdate = new HashMap<>();
        toUpdate.put(run.getKey(), null);
        getRunsDb().updateChildrenAsync(toUpdate);

    }
}
