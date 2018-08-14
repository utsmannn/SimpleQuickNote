package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.kucingapes.simplequicknote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DetailNote extends AppCompatActivity {

    private EditText editText;
    private String note;
    private int position;
    private int color;

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
            setResult(RESULT_OK, intent);
            finish();

        } else {
            finish();
        }
    }
}
