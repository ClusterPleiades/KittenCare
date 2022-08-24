package com.pleiades.pleione.kittencare.controller;

import static android.content.Context.VIBRATOR_MANAGER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

import static com.pleiades.pleione.kittencare.Config.AMPLITUDE_CLICK;
import static com.pleiades.pleione.kittencare.Config.AMPLITUDE_STRONG;
import static com.pleiades.pleione.kittencare.Config.AMPLITUDE_WEAK;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_PERIOD_CLICK;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_PERIOD_LONG;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_PERIOD_SHORT;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_TYPE_CLICK;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_TYPE_LS;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_TYPE_LW;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_TYPE_SS;
import static com.pleiades.pleione.kittencare.Config.VIBRATION_TYPE_SW;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class VibrationController {
    Context context;

    VibratorManager vibratorManager;
    Vibrator vibrator;

    VibrationEffect ClickVibrationEffect;
    VibrationEffect SWVibrationEffect; // short weak
    VibrationEffect SSVibrationEffect; // short strong
    VibrationEffect LWVibrationEffect; // long weak
    VibrationEffect LSVibrationEffect; // long strong

    public VibrationController(Context context) {
        this.context = context;

        // initialize vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.vibratorManager = (VibratorManager) context.getSystemService(VIBRATOR_MANAGER_SERVICE);
            this.vibrator = vibratorManager.getDefaultVibrator();
        } else
            this.vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        // initialize vibration effect
        ClickVibrationEffect = VibrationEffect.createOneShot(VIBRATION_PERIOD_CLICK, AMPLITUDE_CLICK);
        SWVibrationEffect = VibrationEffect.createOneShot(VIBRATION_PERIOD_SHORT, AMPLITUDE_WEAK);
        SSVibrationEffect = VibrationEffect.createOneShot(VIBRATION_PERIOD_SHORT, AMPLITUDE_STRONG);
        LWVibrationEffect = VibrationEffect.createOneShot(VIBRATION_PERIOD_LONG, AMPLITUDE_WEAK);
        LSVibrationEffect = VibrationEffect.createOneShot(VIBRATION_PERIOD_LONG, AMPLITUDE_STRONG);
    }

    public void vibrate(int type) {
        switch (type) {
            case VIBRATION_TYPE_CLICK:
                vibrator.vibrate(ClickVibrationEffect);
                break;
            case VIBRATION_TYPE_SW:
                vibrator.vibrate(SWVibrationEffect);
                break;
            case VIBRATION_TYPE_SS:
                vibrator.vibrate(SSVibrationEffect);
                break;
            case VIBRATION_TYPE_LW:
                vibrator.vibrate(LWVibrationEffect);
                break;
            case VIBRATION_TYPE_LS:
                vibrator.vibrate(LSVibrationEffect);
                break;
        }
    }
}
