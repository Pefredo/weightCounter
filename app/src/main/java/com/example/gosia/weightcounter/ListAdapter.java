package com.example.gosia.weightcounter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gosia.weightcounter.model.WeightData;
import com.facebook.drawee.view.SimpleDraweeView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<WeightData> weightDataList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, weight;
        public SimpleDraweeView image;

        public ViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.text_view_date);
            weight = v.findViewById(R.id.text_view_weight);
            image = v.findViewById(R.id.image);
        }
    }

    public ListAdapter(List<WeightData> list, Context context) {
        this.weightDataList = list;
        this.context = context;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //set data
        WeightData weightData = weightDataList.get(position);
        holder.date.setText(weightData.getLastDayWeightMeasurement());
        holder.weight.setText(weightData.getWeight());

        List<WeightData> data = SQLite.select().
                from(WeightData.class).queryList();

        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorTransparentBlack));
        }

        ImageView imageView = holder.image;

        if ((data.size() > 1)) {

            for (int i = data.size() - 1; i > 0; i--) {

                if (position > 0) {
                    if (Double.parseDouble(weightDataList.get(position).getWeight()) < Double.parseDouble(weightDataList.get(position - 1).getWeight())) {
                        imageView.setImageResource(R.drawable.ic_arrow);
                        imageView.setRotation(imageView.getRotation() + 90);
                        imageView.setColorFilter(context.getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_IN);

                    } else if (Double.parseDouble(weightDataList.get(position).getWeight()) > Double.parseDouble(weightDataList.get(position - 1).getWeight())) {
                        imageView.setImageResource(R.drawable.ic_arrow);
                        imageView.setRotation(imageView.getRotation() + 270);
                        imageView.setColorFilter(context.getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);

                    } else {
                        imageView.setImageResource(0);
                        imageView.setColorFilter(context.getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return weightDataList.size();
    }
}