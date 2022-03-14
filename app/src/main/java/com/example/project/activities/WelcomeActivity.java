package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.databinding.ActivityWelcomeBinding;

/**
 * Display welcome screen. This activity requires username in
 * SharedPreferences to forward to MainActivity.
 *
 * @author Dat Pham, Sanosh
 * @version 0.0.1
 * @since 2022-03-04
 */
public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding mBinding;
    public static final String PREF_FILE_NAME = "pref_file";
    public static final String PREF_NAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        /* Data binding */
        mBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View mView = mBinding.getRoot();
        setContentView(mView);

        /* Init SharedPreferences and editor */
        SharedPreferences sp = getSharedPreferences(PREF_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        String username = sp.getString(PREF_NAME, "");

        /* Check username in Preferences. If found, automatically start MainActivity after 3 seconds */
        if (username.equals("")) {
            mBinding.txtWelcome.setText(R.string.first_time);
            mBinding.layoutName.setVisibility(View.VISIBLE);
            mBinding.txtCopyright.setVisibility(View.GONE);
        } else {
            mBinding.txtWelcome.setText(getString(R.string.wecome) + ", " + username + "!");
            Handler handler = new Handler();
            handler.postDelayed(() -> launchMainActivity(), 2500);
        }

        /* Register calendar vector's onClick and start animation */
        mBinding.myAnimatedView.setOnClickListener(view -> {
            AnimatedVectorDrawable animation;
            Drawable d = mBinding.myAnimatedView.getDrawable();
            if (d instanceof AnimatedVectorDrawable) {
                animation = (AnimatedVectorDrawable) d;
                animation.start();
            }
        });
        mBinding.myAnimatedView.performClick();

        /* Register onClick of btnHandle */
        mBinding.btnHandle.setOnClickListener(view -> {
            if (username.equals("")) {
                String etUsername = String.valueOf(mBinding.tilName.getEditText().getText());
                if (etUsername.equals("")) {
                    Toast.makeText(this, getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
                } else {
                    spEditor.putString(PREF_NAME, etUsername);
                    spEditor.apply();
                    launchMainActivity();
                }
            } else {
                launchMainActivity();
            }
        });
    }

    /**
     * Start MainActivity.
     */
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}