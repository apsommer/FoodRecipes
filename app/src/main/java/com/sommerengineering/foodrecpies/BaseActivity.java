package com.sommerengineering.foodrecpies;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Base activity class from which all activities extend. Interesting approach here to keep common
 * elements such as the progress bar as singleton, and accessible to all children activities. A
 * simple constraint layout contains a frame layout, this frame holds a specific activity's layout.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressBar progressBar;

    @Override
    public void setContentView(int layoutResID) {

        // get refs to the layout entities
        ConstraintLayout constraintLayout =
                (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);

        // put the specific activity layout passed to this method into the frame
        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        // continue with normal OS flow using this base activity constraint layout
        super.setContentView(constraintLayout);

        progressBar = findViewById(R.id.progress_bar);
    }

    // toggle the progress bar visibility
    public void showProgressBar(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
