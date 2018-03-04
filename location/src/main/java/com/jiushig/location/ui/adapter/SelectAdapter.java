package com.jiushig.location.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiushig.location.R;
import com.jiushig.location.entity.Location;
import com.jiushig.location.ui.SelectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zk on 2018/3/4.
 */

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {

    private ArrayList<Location> locations;
    private SelectActivity selectActivity;

    public SelectAdapter(SelectActivity activity, ArrayList<Location> locations) {
        this.locations = locations;
        this.selectActivity = activity;

    }

    @Override
    public SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectViewHolder(LayoutInflater.from(selectActivity).inflate(R.layout.item_select, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectViewHolder holder, int position) {
        holder.textView.setText(locations.get(position).poiName);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class SelectViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SelectViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("info", locations.get(getAdapterPosition()));
                selectActivity.setResult(Activity.RESULT_OK, intent);
                selectActivity.finish();
            });
        }
    }
}
