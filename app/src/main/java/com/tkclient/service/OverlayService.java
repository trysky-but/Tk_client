package com.tkclient.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.tkclient.R;
import com.tkclient.config.ModuleManager;

public class OverlayService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private LinearLayout panelContainer;
    private boolean isExpanded = false;
    private ModuleManager moduleManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createOverlay();
        moduleManager = new ModuleManager(this);
        return START_STICKY;
    }

    private void createOverlay() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = LayoutInflater.from(this);
        overlayView = inflater.inflate(R.layout.overlay_menu, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(overlayView, params);

        setupMenuToggle();
        setupModules();
    }

    private void setupMenuToggle() {
        TextView titleView = overlayView.findViewById(R.id.menu_title);
        LinearLayout panel = overlayView.findViewById(R.id.overlay_panel);

        titleView.setOnClickListener(v -> {
            isExpanded = !isExpanded;
            panel.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        });

        panel.setVisibility(View.GONE);
    }

    private void setupModules() {
        LinearLayout modulesContainer = overlayView.findViewById(R.id.modules_container);

        String[] modules = {
            "Modo Zoom",
            "Nvision (Fullbright)",
            "Kill Aura",
            "Player ESP",
            "Player Cord",
            "Bridge",
            "Auto Totem",
            "CPVP (Crystal PVP)",
            "Aimbot"
        };

        for (String module : modules) {
            LinearLayout moduleView = new LinearLayout(this);
            moduleView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                80
            ));
            moduleView.setOrientation(LinearLayout.HORIZONTAL);
            moduleView.setPadding(8, 8, 8, 8);

            TextView label = new TextView(this);
            label.setText(module);
            label.setTextColor(getResources().getColor(R.color.text_white));
            label.setTextSize(14);
            label.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            Switch toggle = new Switch(this);
            toggle.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            moduleView.addView(label);
            moduleView.addView(toggle);
            modulesContainer.addView(moduleView);

            applyTextOutline(label);
        }
    }

    private void applyTextOutline(TextView textView) {
        textView.setShadowLayer(2, 0, 0, getResources().getColor(R.color.text_black));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
        }
    }
}