package com.pleiades.pleione.kittencare.ui.activity.settings;

import static com.pleiades.pleione.kittencare.Config.DELAY_FACE_MAINTAIN;
import static com.pleiades.pleione.kittencare.Config.DIALOG_TYPE_SKIP_TUTORIAL;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_BLINK_1;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_BLINK_2;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_BLINK_3;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_DEFAULT;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_MEOW_1;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_MEOW_2;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SLEEP;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SMILE;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SPARKLE;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SURPRISED;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SWEAT_1;
import static com.pleiades.pleione.kittencare.Config.FACE_CODE_SWEAT_2;
import static com.pleiades.pleione.kittencare.Config.KEY_IS_TUTORIAL_COMPLETED;
import static com.pleiades.pleione.kittencare.Config.PERIOD_EXPLORE;
import static com.pleiades.pleione.kittencare.Config.PREFS;
import static com.pleiades.pleione.kittencare.Config.TUTORIAL_TYPE_DEFAULT;
import static com.pleiades.pleione.kittencare.Converter.getFaceResourceId;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.pleiades.pleione.kittencare.R;
import com.pleiades.pleione.kittencare.ui.fragment.dialog.DefaultDialogFragment;
import com.pleiades.pleione.kittencare.ui.fragment.tutorial.ImageTutorialFragment;
import com.pleiades.pleione.kittencare.ui.fragment.tutorial.PawTutorialFragment;
import com.pleiades.pleione.kittencare.ui.fragment.tutorial.ShowTutorialFragment;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TutorialActivity extends AppCompatActivity {
    private Timer timer;
    private TimerTask timerTask;
    private ImageView[] kittenFaceImageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // initialize context
        Context context = TutorialActivity.this;

        // set navigation color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.color_navigation_background));

        // initialize appbar
        View appbar = findViewById(R.id.appbar_tutorial);
        Toolbar toolbar = appbar.findViewById(R.id.toolbar_sub);
        setSupportActionBar(toolbar);

        // initialize action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // initialize view pager
        final ViewPager2 viewPager = findViewById(R.id.pager_tutorial);
        TutorialFragmentStateAdapter contentsPagerAdapter = new TutorialFragmentStateAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(contentsPagerAdapter);

        // initialize right image button
        final ImageButton rightImageButton = findViewById(R.id.next_tutorial);
        rightImageButton.setVisibility(View.VISIBLE);
        rightImageButton.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));

        // initialize left image button
        final ImageButton leftImageButton = findViewById(R.id.prev_tutorial);
        leftImageButton.setVisibility(View.INVISIBLE);
        leftImageButton.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));

        // set page change listener
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // set visibility
                if (position == 0) {
                    leftImageButton.setVisibility(View.INVISIBLE);
                    rightImageButton.setVisibility(View.VISIBLE);
                } else if (position == 5) {
                    leftImageButton.setVisibility(View.VISIBLE);
                    rightImageButton.setVisibility(View.INVISIBLE);
                } else {
                    leftImageButton.setVisibility(View.VISIBLE);
                    rightImageButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initializeTimerTask() {
        // initialize kitten face image view list
        kittenFaceImageViewList = new ImageView[8];
        kittenFaceImageViewList[0] = findViewById(R.id.kitten_face_1_tutorial);
        kittenFaceImageViewList[1] = findViewById(R.id.kitten_face_2_tutorial);
        kittenFaceImageViewList[2] = findViewById(R.id.kitten_face_3_tutorial);
        kittenFaceImageViewList[3] = findViewById(R.id.kitten_face_4_tutorial);
        kittenFaceImageViewList[4] = findViewById(R.id.kitten_face_5_tutorial);
        kittenFaceImageViewList[5] = findViewById(R.id.kitten_face_6_tutorial);
        kittenFaceImageViewList[6] = findViewById(R.id.kitten_face_7_tutorial);
        kittenFaceImageViewList[7] = findViewById(R.id.kitten_face_8_tutorial);

        // initialize timer task
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                switch (random.nextInt(15)) {
                    case 0:
                        // case sparkle
                        changeKittenFace(FACE_CODE_MEOW_2, 0);
                        changeKittenFace(FACE_CODE_MEOW_1, 40);
                        changeKittenFace(FACE_CODE_SURPRISED, 80);
                        changeKittenFace(FACE_CODE_SPARKLE, 120);
                        changeKittenFace(FACE_CODE_SURPRISED, 120 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_MEOW_1, 160 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_MEOW_2, 200 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_DEFAULT, 240 + DELAY_FACE_MAINTAIN);
                        break;
                    case 1:
                        // case smile
                        changeKittenFace(FACE_CODE_BLINK_1, 0);
                        changeKittenFace(FACE_CODE_BLINK_2, 40);
                        changeKittenFace(FACE_CODE_BLINK_3, 80);
                        changeKittenFace(FACE_CODE_SMILE, 120);
                        changeKittenFace(FACE_CODE_BLINK_3, 120 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_BLINK_2, 160 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_BLINK_1, 200 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_DEFAULT, 240 + DELAY_FACE_MAINTAIN);
                        break;
                    case 2:
                        // case sweat
                        changeKittenFace(FACE_CODE_MEOW_2, 0);
                        changeKittenFace(FACE_CODE_MEOW_1, 40);
                        changeKittenFace(FACE_CODE_SWEAT_1, 80);
                        changeKittenFace(FACE_CODE_SWEAT_2, 80 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_MEOW_1, 120 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_MEOW_2, 160 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_DEFAULT, 200 + DELAY_FACE_MAINTAIN);
                        break;
                    case 3:
                        // case meow
                        changeKittenFace(FACE_CODE_MEOW_2, 0);
                        changeKittenFace(FACE_CODE_MEOW_1, 40);
                        changeKittenFace(FACE_CODE_MEOW_2, 160); // + 120
                        changeKittenFace(FACE_CODE_DEFAULT, 200);
                        break;
                    case 4:
                        // case sleep
                        changeKittenFace(FACE_CODE_BLINK_1, 0);
                        changeKittenFace(FACE_CODE_BLINK_2, 40);
                        changeKittenFace(FACE_CODE_BLINK_3, 80);
                        changeKittenFace(FACE_CODE_SLEEP, 120);
                        changeKittenFace(FACE_CODE_BLINK_3, 120 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_BLINK_2, 160 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_BLINK_1, 200 + DELAY_FACE_MAINTAIN);
                        changeKittenFace(FACE_CODE_DEFAULT, 240 + DELAY_FACE_MAINTAIN);
                        break;
                    default:
                        // case blink
                        changeKittenFace(FACE_CODE_BLINK_1, 0);
                        changeKittenFace(FACE_CODE_BLINK_2, 40);
                        changeKittenFace(FACE_CODE_BLINK_3, 80);
                        changeKittenFace(FACE_CODE_BLINK_2, 160); // + 80
                        changeKittenFace(FACE_CODE_BLINK_1, 200);
                        changeKittenFace(FACE_CODE_DEFAULT, 240);
                        break;
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, PERIOD_EXPLORE / 2, PERIOD_EXPLORE);
    }

    private void changeKittenFace(final int faceCode, int delay) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            for (ImageView faceImageView : kittenFaceImageViewList) {
                if (faceImageView != null)
                    faceImageView.setImageResource(getFaceResourceId(faceCode));
            }
        }, delay);
    }

    private void cancelTimerTask() {
        // cancel timer task
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        // cancel timer
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // initialize timer task
        initializeTimerTask();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // cancel timer
        cancelTimerTask();
    }

    @Override
    public void onDestroy() {
        // cancel timer
        cancelTimerTask();

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            if (prefs.getBoolean(KEY_IS_TUTORIAL_COMPLETED, false))
                onBackPressed();
            else {
                // show skip tutorial dialog
                DefaultDialogFragment defaultDialogFragment = new DefaultDialogFragment(DIALOG_TYPE_SKIP_TUTORIAL);
                defaultDialogFragment.show(getSupportFragmentManager(), Integer.toString(DIALOG_TYPE_SKIP_TUTORIAL));
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(KEY_IS_TUTORIAL_COMPLETED, false)) {
            super.onBackPressed();
        } else {
            // return to home
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private static class TutorialFragmentStateAdapter extends FragmentStateAdapter {

        public TutorialFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ShowTutorialFragment();
                case 5:
                    return new PawTutorialFragment();
                default:
                    return ImageTutorialFragment.newInstance(TUTORIAL_TYPE_DEFAULT, position);
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }
    }
}
