package vn.poly.personalmanagement.adapter.health.fitness;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Fitness;

public class DetailexerciseAdapter extends BaseAdapter {



    List<DetailExercise> detailExerciseList;
    private OnItemRemoveListener onItemRemoveListener;
    public DetailexerciseAdapter() {
    }



    public interface OnItemRemoveListener {
        void onRemove(DetailExercise detailExercise, int position);
    }

    public void setDataAdapter(List<DetailExercise> detailExerciseList, OnItemRemoveListener onItemRemoveListener) {
        this.detailExerciseList = detailExerciseList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return detailExerciseList.size();
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

        final DetailExercise detailExercise =detailExerciseList.get(position);
//        if (eating.getDate().compareTo(CurrentDateTime.getCurrentDate())<0){
//            viewHolder.tvMain.setTextColor(Color.parseColor("red"));
//        }

        viewHolder.tvMain.setText(detailExercise.getExercise());

        viewHolder.tvSub.setText(detailExercise.getDescribe());
        viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(detailExercise, position);
            }
        });

        return convertView;
    }
    public class ViewHolder {
        TextView tvMain, tvSub;
        ImageView icRemove;

    }
}
