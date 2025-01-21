package com.example.tutorialfirebase;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FragmentTwo extends Fragment {

    private FirebaseFirestore db;
    private LinearLayout linearLayout;
    private static final String TAG = "FragmentTwo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        linearLayout = view.findViewById(R.id.linearLayout);

        // Load data from Firebase
        loadDataFromFirebase();

        return view;
    }

    private void loadDataFromFirebase() {
        db.collection("Lojas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully accessed the collection.");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String data = document.getString("nome");
                            if (data != null) {
                                Log.d(TAG, "Document ID: " + document.getId() + ", Data: " + data);

                                // Create a new TextView for each item
                                TextView textView = new TextView(getContext());
                                textView.setText(data);
                                textView.setTextSize(16);
                                textView.setPadding(8, 8, 8, 8);

                                // Add the TextView to the LinearLayout on the main thread
                                getActivity().runOnUiThread(() -> linearLayout.addView(textView));
                            } else {
                                Log.d(TAG, "No data found for document: " + document.getId());
                            }
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                        getActivity().runOnUiThread(() -> {
                            TextView errorTextView = new TextView(getContext());
                            errorTextView.setText("Error getting documents: " + task.getException());
                            linearLayout.addView(errorTextView);
                        });
                    }
                });
    }
}