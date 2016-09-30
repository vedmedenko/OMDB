package com.dataroottrainee.rxomdb.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dataroottrainee.rxomdb.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

    private LinkedHashMap<String, String> mDetails;

    @Inject
    public DetailAdapter() {
        this.mDetails = new LinkedHashMap<>();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.detail_item, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailHolder holder, final int position) {

        String key = (new ArrayList<>(mDetails.keySet())).get(position);
        String value = (new ArrayList<>(mDetails.values())).get(position);

        holder.keyText.setText(key);
        holder.valueText.setText(value);
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    public void setDetails(LinkedHashMap<String, String> details) {
        mDetails = details;
        notifyDataSetChanged();
    }

    class DetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.key)
        TextView keyText;

        @BindView(R.id.value)
        TextView valueText;

        public DetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
