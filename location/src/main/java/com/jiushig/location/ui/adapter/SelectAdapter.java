package com.jiushig.location.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiushig.location.R;
import com.jiushig.location.location.LocationInfo;
import com.jiushig.location.ui.SelectActivity;

import java.util.List;

/**
 * Created by zk on 2018/3/4.
 */

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {

    private List<LocationInfo> locationInfos;
    private SelectActivity selectActivity;

    public SelectAdapter(SelectActivity activity, List<LocationInfo> locationInfos) {
        this.locationInfos = locationInfos;
        this.selectActivity = activity;

    }

    @Override
    public SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectViewHolder(LayoutInflater.from(selectActivity).inflate(R.layout.item_select, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectViewHolder holder, int position) {
        holder.textView.setText(locationInfos.get(position).poiName);
    }

    @Override
    public int getItemCount() {
        return locationInfos.size();
    }

    class SelectViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SelectViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
