package com.kucingapes.simplequicknote.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kucingapes.simplequicknote.Activity.OptionActivity;
import com.kucingapes.simplequicknote.Model.ModelBackup;
import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.SharedPreferences.SharedList;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.Holder> {
    private List<ModelBackup> modelBackupList;
    private Context context;
    private String PREFS_NAME = "ganteng";
    private String KEY = "listhistory";
    private ProgressDialog mProgressDialog;

    public BackupAdapter(List<ModelBackup> modelBackupList) {
        this.modelBackupList = modelBackupList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_backup, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ModelBackup modelBackup = modelBackupList.get(position);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Restore?")
                .setMessage("You will overwrite existing notes").setPositiveButton("Restore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showProgressDialog();
                String json = modelBackup.getJson();
                SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY, json);
                editor.apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Toast.makeText(context, "Restored", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        final AlertDialog dialog = builder.create();

        String fixDate = modelBackup.getDate().replaceAll("-","/");

        holder.textDate.setText(fixDate);
        holder.textCount.setText(modelBackup.getSize()+ " notes in backup");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelBackupList.size();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textDate, textCount;
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.date_list);
            textCount = itemView.findViewById(R.id.text_size);
            imageView = itemView.findViewById(R.id.download_btn);
        }
    }
}
