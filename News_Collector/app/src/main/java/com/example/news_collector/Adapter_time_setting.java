package com.example.news_collector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Adapter_time_setting extends RecyclerView.Adapter<Adapter_time_setting.ViewHolder> {

    private ArrayList<recyclerviewItem_time_setting> mDatalist;

    Adapter_time_setting(ArrayList<recyclerviewItem_time_setting> mdataList){
        this.mDatalist = mdataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_data, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter_time_setting.ViewHolder holder, int position) {
        int hour, hour_tmp;
        final int Position = position;

        hour_tmp = mDatalist.get(Position).getHour();
        hour = hour_tmp % 12;

        if(hour_tmp / 12 == 1){
            holder.meridiem.setText("오후");
        } else{
            holder.meridiem.setText("오전");
        }

        holder.hour.setText(String.valueOf(hour));
        holder.minute.setText(String.valueOf(mDatalist.get(Position).getMinute()));

        holder.linearLayout_time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("DELETE");
                builder.setMessage("해당 시간을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatalist.remove(Position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mDatalist ? mDatalist.size(): 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView  hour;
        protected TextView  minute;
        protected TextView  meridiem;
        protected LinearLayout linearLayout_time;

        public ViewHolder(View itemView) {
            super(itemView);

            this.hour               = (TextView)itemView.findViewById(R.id.textview_HOUR);
            this.minute             = (TextView)itemView.findViewById(R.id.textview_MINUTE);
            this.meridiem           = (TextView)itemView.findViewById(R.id.textview_meridiem);
            this.linearLayout_time  = (LinearLayout)itemView.findViewById(R.id.linearlayout_Time);
        }
    }
}
