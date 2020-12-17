package vn.poly.personalmanagement.adapter.health.fitness;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.model.Fitness;

public class FitnessAdapter extends BaseAdapter {



    List<Fitness> fitnessList;
    private OnItemRemoveListener onItemRemoveListener;
    public FitnessAdapter() {
    }



    public interface OnItemRemoveListener {
        void onRemove(Fitness fitness, int position);
    }

    public void setDataAdapter(List<Fitness> fitnessList, OnItemRemoveListener onItemRemoveListener) {
        this.fitnessList = fitnessList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return fitnessList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fitness, parent, false);
            viewHolder.tvMain = convertView.findViewById(R.id.tv_main_item);
            viewHolder.tvSub = convertView.findViewById(R.id.tv_sub_item);
            viewHolder.icRemove = convertView.findViewById(R.id.ic_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Fitness fitness = fitnessList.get(position);

        if (fitness.getDate().equals(CurrentDateTime.getCurrentDate())){
            // set text color : green
            viewHolder.tvMain.setTextColor(Color.parseColor("#039E09"));
            viewHolder.tvMain.setText("Hôm nay, "+fitness.getDate());
        }else{
            // set text color : blue
            viewHolder.tvMain.setTextColor(Color.parseColor("#0371C8"));
            viewHolder.tvMain.setText("Ngày "+fitness.getDate());

        }

        viewHolder.tvSub.setText("Số bài tập: "+fitness.getAmountExercises());
        viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(fitness, position);
            }
        });

        return convertView;
    }
    public class ViewHolder {
        TextView tvMain, tvSub;
        ImageView icRemove;

    }
}
