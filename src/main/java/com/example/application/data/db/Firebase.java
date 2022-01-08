package com.example.application.data.db;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Firebase {
    private static FirebaseApp app;

    public static void setup() throws IOException {
        if (app != null) {
            return;
        }

        String serviceAccount = IOUtils.toString(
                Firebase.class.getResourceAsStream("serviceAccount.json"),
                StandardCharsets.UTF_8);
        JsonObject serviceAccountJson = Json.parse(serviceAccount);
        String projectId = serviceAccountJson.getString("project_id");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials
                        .fromStream(IOUtils.toInputStream(serviceAccount)))
                .setDatabaseUrl("https://" + projectId + ".firebaseio.com")
                .build();

        app = FirebaseApp.initializeApp(options);

        maybeGenerateData();

    }

    /**
     * Creates some initial data if the database is empty
     */
    private static void maybeGenerateData() {
        getDb().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Wait for initial data before deciding to create or not
                RunDB.maybeCreateInitialData(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static DatabaseReference getDb() {
        return FirebaseDatabase.getInstance(app).getReference();
    }

}
