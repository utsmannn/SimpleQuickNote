package com.kucingapes.simplequicknote;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class MainActivity extends AppCompatActivity {

    private String KEY = "resultKey";
    private String INDEX = "listhistory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = getSharedPreferences("ganteng", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String defaultString = preferences.getString(KEY, "");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_dialog, null);

        final EditText editText = dialogView.findViewById(R.id.text_edit);
        editText.setHint("Text here");
        editText.setText(defaultString);
        editText.setSelection(defaultString.length());

        final String result = editText.getText().toString();

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        addNotification(getApplicationContext());

        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                setupListHistory(editText, editor, preferences);
                MainActivity.this.finish();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogView.findViewById(R.id.option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupListHistory(editText, editor, preferences);
                startActivity(new Intent(getApplicationContext(), OptionActivity.class));
                finish();
            }
        });

        dialogView.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy",result);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                if (!result.equals("")) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                } else Toast.makeText(MainActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        });

        dialogView.findViewById(R.id.all_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupListHistory(editText, editor, preferences);
                startActivity(new Intent(getApplicationContext(), ListNotes.class));
                finish();
            }
        });

        dialogView.findViewById(R.id.notif_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNotificationManager != null) {
                    mNotificationManager.cancelAll();
                    MainActivity.this.finish();
                }
            }
        });

        dialogView.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    MainActivity.this.finish();
                } else {
                    if (mNotificationManager != null) {
                        mNotificationManager.cancelAll();
                    }
                    editText.setText("");
                    editor.remove(KEY);
                    editor.apply();
                }
            }
        });


        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                String result = editText.getText().toString();
                if (result.length() == 0) {
                    result = "";
                }

                editor.putString(KEY, result);
                editor.apply();

                addNotification(getApplicationContext());
            }
            @Override
            public void onHomeLongPressed() {
                String result = editText.getText().toString();
                if (result.length() == 0) {
                    result = "";
                }

                editor.putString(KEY, result);
                editor.apply();

                addNotification(getApplicationContext());
            }
        });
        mHomeWatcher.startWatch();
    }

    private void setupListHistory(EditText editText, SharedPreferences.Editor editor, SharedPreferences preferences) {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY / hh:mm");
        String strDate = sdf.format(c.getTime());

        String result = editText.getText().toString();
        if (result.length() == 0) {
            result = "";
        }

        editor.putString(KEY, result);
        editor.apply();

        String json = preferences.getString(INDEX, null);

        ModelHistory modelHistory = new ModelHistory(result, strDate);
        SharedList sharedList = new SharedList();

        List<ModelHistory> modelHistories = sharedList.getFavorites(getApplicationContext());

        if (json != null) {
            if (!result.equals("")) {
                HistoryAdapter adapter = new HistoryAdapter();
                if (!adapter.contains(modelHistories, result)) {
                    sharedList.addFavorite(getApplicationContext(), modelHistory);
                }
            }
        } else {
            if (!result.equals("")) {
                sharedList.addFavorite(getApplicationContext(), modelHistory);
            }
        }

        addNotification(getApplicationContext());
    }

    public static void addNotification(Context context) {
        String KEY = "resultKey";
        SharedPreferences preferences = context.getSharedPreferences("ganteng", MODE_PRIVATE);
        String defaultString = preferences.getString(KEY, "");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "notify_001");
        Intent ii = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.icon_anydpi);
        mBuilder.setContentText(defaultString);
        mBuilder.setStyle(bigText);
        mBuilder.setOngoing(true);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (defaultString.length() != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_LOW);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }

            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        } else {
            if (mNotificationManager != null) {
                mNotificationManager.cancelAll();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.stopWatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.stopWatch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.stopWatch();
    }
}
