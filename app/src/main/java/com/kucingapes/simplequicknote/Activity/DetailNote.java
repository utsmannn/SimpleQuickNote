package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kucingapes.simplequicknote.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DetailNote extends AppCompatActivity {

    private EditText editText;
    private String note;
    private int position;
    private int color;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private boolean timerOn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        Bundle bundle = getIntent().getExtras();
        note = Objects.requireNonNull(bundle).getString("note");
        String date = bundle.getString("date");
        color = bundle.getInt("color");
        position = bundle.getInt("position");


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

    }

    @SuppressLint("SimpleDateFormat")
    private void setupPickerTimer(int actionColor) {
        final TextView textDate = findViewById(R.id.date_detail);
        //Button setDate = findViewById(R.id.select_date);

        final String on = "Timer on";
        final String off = "Timer off";

        final Switch timerSwitch = findViewById(R.id.switch_toggle);
        timerSwitch.setChecked(false);
        timerSwitch.setText(off);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        /*Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm");
        String defaultDate = dateFormat.format(date);
        textDate.setText(defaultDate);*/


        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                String time = hourOfDay + ":" + minute;

                mHour = hourOfDay;
                mMinute = minute;

                timerSwitch.setChecked(true);
                timerSwitch.setText(on);
                textDate.setText(mDay+"/"+mMonth+"/"+mYear+" "+mHour+":"+mMinute);

            }
        }, hour, minute, true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                timerSwitch.setChecked(false);
                timerSwitch.setText(off);
            }
        });
        timePickerDialog.vibrate(false);
        timePickerDialog.setAccentColor(actionColor);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

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
                timerSwitch.setText(off);
            }
        });
        datePickerDialog.vibrate(false);
        datePickerDialog.setAccentColor(actionColor);

        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    datePickerDialog.show(getFragmentManager(), "Select date");
                }
            }
        });

        /*setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(getFragmentManager(), "Select date");
            }
        });*/
    }

    public int getColorDarken(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }

    @Override
    public void onBackPressed() {
        String change = editText.getText().toString();
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY / HH:mm");
        final String strDate = sdf.format(c.getTime());

        long date = System.currentTimeMillis();
        int bah = (int) date;

        if (!change.equals(note)) {
            Intent intent = new Intent();
            intent.putExtra("note", change);
            intent.putExtra("date", strDate);
            intent.putExtra("color", color);
            intent.putExtra("position", position);
            intent.putExtra("id", bah);
            intent.putExtra("status", true);
            setResult(RESULT_OK, intent);
            finish();

        } else {
            finish();
        }
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
}
