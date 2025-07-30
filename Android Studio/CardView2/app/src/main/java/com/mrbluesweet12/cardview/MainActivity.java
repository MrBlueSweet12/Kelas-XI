package com.mrbluesweet12.cardview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CardViews
        initializeCardViews();

        // Set click listeners
        setupClickListeners();
    }

    private void initializeCardViews() {
        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        cardView4 = findViewById(R.id.cardView4);
        cardView5 = findViewById(R.id.cardView5);
        cardView6 = findViewById(R.id.cardView6);
    }

    private void setupClickListeners() {
        // Basic CardView click listener
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Basic CardView clicked!", v);
            }
        });

        // High Elevation CardView click listener
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCard(v);
                showToast("High Elevation CardView clicked!", v);
            }
        });

        // Rounded Corners CardView click listener
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCard(v);
                showToast("Rounded Corners CardView clicked!", v);
            }
        });

        // Complex CardView click listener
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCard(v);
                showToast("Complex CardView clicked!", v);
            }
        });

        // Clickable CardView (already has foreground effect)
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Clickable CardView clicked! 🎉", v);
            }
        });

        // Minimal Elevation CardView click listener
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCard(v);
                showToast("Minimal Elevation CardView clicked!", v);
            }
        });
    }

    private void showToast(String message, View view) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void animateCard(View view) {
        // Simple scale animation
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .start();
                    }
                })
                .start();
    }

    // Method to demonstrate programmatic CardView manipulation
    private void demonstrateCardViewAttributes() {
        // Example: Changing CardView properties programmatically

        // Change corner radius
        cardView1.setRadius(16f);

        // Change elevation
        cardView1.setCardElevation(8f);

        // Change background color
        cardView1.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        // Set use compat padding
        cardView1.setUseCompatPadding(true);

        // Set prevent corner overlap
        cardView1.setPreventCornerOverlap(true);
    }

    // Method to get CardView attributes
    private void getCardViewAttributes(CardView cardView) {
        float radius = cardView.getRadius();
        float elevation = cardView.getCardElevation();
        boolean useCompatPadding = cardView.getUseCompatPadding();
        boolean preventCornerOverlap = cardView.getPreventCornerOverlap();

        String info = String.format(
                "Radius: %.1f\nElevation: %.1f\nCompat Padding: %b\nPrevent Overlap: %b",
                radius, elevation, useCompatPadding, preventCornerOverlap
        );

        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    // Optional: Method to handle long clicks for more information
    @Override
    protected void onResume() {
        super.onResume();

        // Set long click listeners to show CardView attributes
        cardView1.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView1);
            return true;
        });

        cardView2.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView2);
            return true;
        });

        cardView3.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView3);
            return true;
        });

        cardView4.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView4);
            return true;
        });

        cardView5.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView5);
            return true;
        });

        cardView6.setOnLongClickListener(v -> {
            getCardViewAttributes(cardView6);
            return true;
        });
    }
}