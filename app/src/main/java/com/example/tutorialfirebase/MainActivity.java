package com.example.tutorialfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {
    private Button btn_perfil;
    private Button btn_switch_fragment;
    private ViewPager2 viewPager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    private int currentPage = 0;
    private boolean showingFragmentOne = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        IniciarComponentes();

        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 5000);
            }
        };

        // Add the initial fragment
        if (savedInstanceState == null) {
            FragmentOne fragmentOne = new FragmentOne();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, fragmentOne);
            transaction.commit();
        }

        handler.postDelayed(runnable, 5000);

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrParaPerfilActivity();
            }
        });

        btn_switch_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        });
    }

    private void switchFragment() {
        Fragment fragment;
        if (showingFragmentOne) {
            fragment = new FragmentTwo();
        } else {
            fragment = new FragmentOne();
        }
        showingFragmentOne = !showingFragmentOne;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void IrParaPerfilActivity() {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

    private void IniciarComponentes() {
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_switch_fragment = findViewById(R.id.btn_switch_fragment);
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}