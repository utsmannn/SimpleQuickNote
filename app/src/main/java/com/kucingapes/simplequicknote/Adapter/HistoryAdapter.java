package com.kucingapes.simplequicknote.Adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kucingapes.simplequicknote.Activity.DetailNote;
import com.kucingapes.simplequicknote.Model.ModelHistory;
import com.kucingapes.simplequicknote.OnItemClickListener;
import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.SharedPreferences.SharedList;

import java.util.List;

public class
HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    private List<ModelHistory> stringList;
    private OnItemClickListener listener;
    private Context context;
    private View view;

    public HistoryAdapter(List<ModelHistory> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    /*public HistoryAdapter(List<ModelHistory> stringList, OnItemClickListener listener, Context context) {
        this.stringList = stringList;
        this.listener = listener;
        this.context = context;
    }*/

    public HistoryAdapter() {
        super();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, @SuppressLint("RecyclerView") final int i) {
        final ModelHistory modelHistory = stringList.get(i);

        @SuppressLint("InflateParams") final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.delete_dialog, null);

        TextView textView = dialogView.findViewById(R.id.messege);
        textView.setText(modelHistory.getText());

        holder.item.setText(modelHistory.getText());
        holder.date.setText(modelHistory.getDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.onItemClick(modelHistory);

                /*ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(modelHistory.getDate(), modelHistory.getText());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                }*/
                Intent intent = new Intent(context, DetailNote.class);
                intent.putExtra("note", modelHistory.getText());
                intent.putExtra("date", modelHistory.getDate());
                intent.putExtra("color", modelHistory.getColor());
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("size", stringList.size());
                intent.putExtra("id", modelHistory.getId());
                //context.startActivity(intent);
                ((AppCompatActivity)context).startActivityForResult(intent,  1);
                //((AppCompatActivity)context).finish();
            }
        });
        holder.cardView.setCardBackgroundColor(modelHistory.getColor());
        /*view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Remove item");
                builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedList sharedList = new SharedList();

                        stringList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, stringList.size());
                        sharedList.clearFavorite(context);
                        sharedList.saveFavorites(context, stringList);
                        Toast.makeText(context, "item deleted", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

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
            }
        });*/
    }


    public boolean contains(List<ModelHistory> list, String result) {
        for (ModelHistory model : list) {
            if (model.getText().equals(result)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView item, date;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_history);
            date = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.card);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            //Toast.makeText(context, String.valueOf(stringList.size()), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
