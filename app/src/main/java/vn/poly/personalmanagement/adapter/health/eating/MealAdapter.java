package vn.poly.personalmanagement.adapter.health.eating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Meal;
import vn.poly.personalmanagement.model.Note;

public class MealAdapter extends BaseAdapter {
    private Context context;
    private List<Meal> mealList;
    private OnItemRemoveListener onItemRemoveListener;
    private OnItemShowMenuClickListener onItemShowMenuClickListener;
    public MealAdapter() {
    }

    public MealAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemRemoveListener {
        void onRemove(Meal meal, int position);
    }
    public interface OnItemShowMenuClickListener {
        void onClick(Note note, int position, View view);
    }
    public void setDataAdapter(List<Meal> mealList, MealAdapter.OnItemRemoveListener onItemRemoveListener) {
        this.mealList = mealList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }

    public void setDataAdapter(List<Meal> mealList, MealAdapter.OnItemShowMenuClickListener onItemShowMenuClickListener) {
        this.mealList = mealList;
        this.onItemShowMenuClickListener = onItemShowMenuClickListener;
        notifyDataSetChanged();
    }
    public void setDataAdapter(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tvMain, tvSub;
        ImageView icRemove;

    }

    @Override
    public int getCount() {
        return mealList.size();
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
       // convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_eating, parent, false);
        final MealAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MealAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_eating, parent, false);
            viewHolder.tvMain = convertView.findViewById(R.id.tv_main_item);
            viewHolder.tvSub = convertView.findViewById(R.id.tv_sub_item);
            viewHolder.icRemove = convertView.findViewById(R.id.ic_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MealAdapter.ViewHolder) convertView.getTag();
        }

        final Meal meal = mealList.get(position);
        viewHolder.tvMain.setText(meal.getTitle());
        viewHolder.tvSub.setText(meal.getTime()+" ngày"+meal.getDate());
        viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(meal, position);
            }
        });
        notifyDataSetChanged();
        return convertView;
    }
}
