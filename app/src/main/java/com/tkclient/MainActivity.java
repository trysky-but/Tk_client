package com.tkclient;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tkclient.service.OverlayService;

public class MainActivity extends AppCompatActivity {

    private Button btnInject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInject = findViewById(R.id.btn_inject);

        btnInject.setOnClickListener(v -> {
            injectMinecraft();
        });

        startOverlayService();
    }

    private void startOverlayService() {
        Intent serviceIntent = new Intent(this, OverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void injectMinecraft() {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.mojang.minecraftpe");
            if (intent != null) {
                startActivity(intent);
                Toast.makeText(this, "Injetando...", Toast.LENGTH_SHORT).show();
                
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                Toast.makeText(this, "Minecraft não instalado!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}