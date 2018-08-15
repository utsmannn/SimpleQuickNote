package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kucingapes.simplequicknote.Adapter.HistoryAdapter;
import com.kucingapes.simplequicknote.Model.ModelHistory;
import com.kucingapes.simplequicknote.OnItemClickListener;
import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.SharedPreferences.SharedList;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListNotes extends AppCompatActivity {

    private ArrayList<ModelHistory> stringList;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        recyclerView = findViewById(R.id.list_note);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LayoutMarginDecoration marginDecoration = new LayoutMarginDecoration(2, 10);
        marginDecoration.setPadding(recyclerView, 10, 10, 10, 10);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(marginDecoration);
        setItemList();

    }

    private void setItemList() {
        SharedList sharedList = new SharedList();
        stringList = sharedList.getFavorites(getApplicationContext());

        if (stringList == null) {
            stringList = new ArrayList<>();
        }

        if (stringList.size() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }

        adapter = new HistoryAdapter(stringList, this);

        if (stringList.size() > 0) {
            sortListByDate(stringList);
        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String note = bundle.getString("note");
            String date = bundle.getString("date");
            int color = bundle.getInt("color");
            int size = stringList.size();

            ModelHistory modelHistory = new ModelHistory(note, date, color, size);
            //sharedList.addFavorite(getApplicationContext(), modelHistory);
            stringList.add(modelHistory);
            sharedList.saveFavorites(getApplicationContext(), stringList);

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //Toast.makeText(this, note, Toast.LENGTH_SHORT).show();
            //recreate();
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/
    }

    private void sortListByDate(ArrayList<ModelHistory> arrayList) {
        Collections.sort(arrayList, new Comparator<ModelHistory>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public int compare(ModelHistory modelHistory, ModelHistory t1) {
                return modelHistory.getId() > t1.getId() ? -1 : 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_backup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup_menu:
                startActivity(new Intent(this, OptionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                SharedList sharedList = new SharedList();
                Boolean status = data.getBooleanExtra("status", false);

                if (status) {
                    long mDate = System.currentTimeMillis();

                    String note = data.getStringExtra("note");
                    String date = data.getStringExtra("date");
                    int color = data.getIntExtra("color", getResources().getColor(R.color.card));
                    int pos = data.getIntExtra("position", 0);
                    int id = data.getIntExtra("id", (int) mDate);
                    //boolean timer = data.getBooleanExtra("timer", false);
                    String timerDate = data.getStringExtra("timerDate");
                    long futureMilis = data.getLongExtra("futuremilis", 0);


                    stringList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    ModelHistory modelHistory = new ModelHistory(note, date, color, id, timerDate, futureMilis);
                    stringList.add(modelHistory);
                    adapter.notifyItemRangeRemoved(pos, stringList.size());
                    sharedList.saveFavorites(getApplicationContext(), stringList);
                    finish();
                    startActivity(new Intent(getApplicationContext(), ListNotes.class));


                } if (!status) {
                    int position = data.getIntExtra("position", 0);
                    stringList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeRemoved(position, stringList.size());
                    sharedList.saveFavorites(getApplicationContext(), stringList);
                    finish();
                    startActivity(new Intent(getApplicationContext(), ListNotes.class));

                }

            }
        }
    }

}
