package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.Services.NotifReceiver;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class DetailNote extends AppCompatActivity {

    private EditText editText;
    private String note;
    private int position;
    private int color;
    private String timerDate;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private BottomSheetBehavior sheetBehavior;
    //private boolean statTimer;
    private long nowMilis = 0;
    private long futureMilis;

    private Switch timerSwitch;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        LinearLayout layoutBotom = findViewById(R.id.layout_bottom);
        timerSwitch = findViewById(R.id.switch_toggle);


        sheetBehavior = BottomSheetBehavior.from(layoutBotom);

        Bundle bundle = getIntent().getExtras();
        note = Objects.requireNonNull(bundle).getString("note");
        String date = bundle.getString("date");
        color = bundle.getInt("color");
        position = bundle.getInt("position");
        futureMilis = bundle.getLong("futuremilis");
        //timer = bundle.getBoolean("timer");
        //statTimer = bundle.getBoolean("timer");
        timerDate = bundle.getString("timerDate");




        if (timerDate != null) {
            Date dates;
            try {
                //dates = new SimpleDateFormat("yyyy.MMMMM.dd HH:mm", Locale.ENGLISH).parse(input);
                dates = new SimpleDateFormat("dd MMMMM yyyy / HH:mm", Locale.ENGLISH).parse(timerDate);
                futureMilis = dates.getTime();
                nowMilis = System.currentTimeMillis();

                if (nowMilis <= futureMilis) {
                    //Toast.makeText(this, "depannya", Toast.LENGTH_SHORT).show();
                    timerSwitch.setChecked(true);
                } else {
                    //Toast.makeText(this, "belakang", Toast.LENGTH_SHORT).show();
                    timerSwitch.setChecked(false);
                }

                Log.d("milisdate-anjay", String.valueOf(futureMilis));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d("tanggaln-anjay", String.valueOf(timerDate));
        }

        int actionColor = getColorDarken(color, 0.8f);
        int statusColor = getColorDarken(color, 0.7f);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(actionColor));
            actionBar.setTitle(date);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
            window.setBackgroundDrawable(new ColorDrawable(color));
        }

        editText = findViewById(R.id.note_text);
        editText.setText(note);


        setupPickerTimer(actionColor);

        findViewById(R.id.option_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        layoutBotom.setBackgroundColor(actionColor);
        setupButtonOption();


    }

    private void setupButtonOption() {
        ImageView mCopy = findViewById(R.id.copy_btn);
        ImageView mShare = findViewById(R.id.share_btn);

        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy",result);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(DetailNote.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                if (!result.equals("")) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                } else Toast.makeText(DetailNote.this, "No text to share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setupPickerTimer(int actionColor) {
        final TextView textDate = findViewById(R.id.date_detail);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textDate.setText(timerDate);
        //timerSwitch.setChecked(timer);

        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                mHour = hourOfDay;
                mMinute = minute;

                String sMonth = getMonth(mMonth);

                timerDate = mDay + " " + sMonth + " " + mYear + " / " + mHour + ":" + mMinute;

                timerSwitch.setChecked(true);
                textDate.setText(timerDate);
                //timer = true;

                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay, mHour, mMinute, 0);
                long timeMilis = cal.getTimeInMillis();

                long nowTime = System.currentTimeMillis();

                int delay = (int) (timeMilis - nowTime);

                String content = editText.getText().toString();
                scheduleNotification(content, delay);


            }
        }, hour, minute, true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                timerSwitch.setChecked(false);
            }
        });
        timePickerDialog.vibrate(false);
        timePickerDialog.setAccentColor(actionColor);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                //textDate.setText(date);
                timePickerDialog.show(getFragmentManager(), "Select time");

            }
        }, year, month, day);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                timerSwitch.setChecked(false);
            }
        });
        datePickerDialog.vibrate(false);
        datePickerDialog.setAccentColor(actionColor);

        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    datePickerDialog.show(getFragmentManager(), "Select date");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //timer = false;
                    timerDate = "Timer off";
                    textDate.setText(timerDate);
                }
            }
        });
    }

    public int getColorDarken(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    @Override
    public void onBackPressed() {
        final String change = editText.getText().toString();
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY / HH:mm");
        final String strDate = sdf.format(c.getTime());

        long date = System.currentTimeMillis();
        final int bah = (int) date;


        if (!change.equals(note) || nowMilis <= futureMilis) {
            String KEY = "resultKey";
            SharedPreferences preferences = DetailNote.this.getSharedPreferences("ganteng", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY, change);
            editor.apply();
            MainActivity.addNotification(DetailNote.this);

            Intent intent = new Intent();
            intent.putExtra("note", change);
            intent.putExtra("date", strDate);
            intent.putExtra("color", color);
            intent.putExtra("position", position);
            intent.putExtra("id", bah);
            //intent.putExtra("timer", timer);
            intent.putExtra("futuremilis", futureMilis);
            intent.putExtra("timerDate", timerDate);
            intent.putExtra("status", true);
            setResult(RESULT_OK, intent);
        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu:

                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailNote.this);
                builder.setMessage("Remove item?");
                builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("position", position);
                        intent.putExtra("status", false);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleNotification(String content, int delay) {
        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        //notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotifReceiver.CONTENT, content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

    }
}
