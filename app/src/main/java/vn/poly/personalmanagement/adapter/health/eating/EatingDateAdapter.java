package vn.poly.personalmanagement.adapter.health.eating;

import android.content.Context;
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
import vn.poly.personalmanagement.model.Eating;

public class EatingDateAdapter extends BaseAdapter {
    private Context context;
    private List<Eating> eatingList;
    private OnItemRemoveListener onItemRemoveListener;

    public EatingDateAdapter() {
    }

    public EatingDateAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemRemoveListener {
        void onRemove(Eating eating, int position);
    }

    public void setDataAdapter(List<Eating> eatingList, EatingDateAdapter.OnItemRemoveListener onItemRemoveListener) {
        this.eatingList = eatingList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }

    public void setDataAdapter(List<Eating> eatingList) {
        this.eatingList = eatingList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tvMain, tvSub;
        ImageView icRemove;

    }

    @Override
    public int getCount() {
        return eatingList.size();
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
        final EatingDateAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new EatingDateAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_eating, parent, false);
            viewHolder.tvMain = convertView.findViewById(R.id.tv_main_item);
            viewHolder.tvSub = convertView.findViewById(R.id.tv_sub_item);
            viewHolder.icRemove = convertView.findViewById(R.id.ic_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EatingDateAdapter.ViewHolder) convertView.getTag();
        }

        final Eating eating = eatingList.get(position);
//        if (eating.getDate().compareTo(CurrentDateTime.getCurrentDate())<0){
//            viewHolder.tvMain.setTextColor(Color.parseColor("red"));
//        }

        if (eating.getDate().equals(CurrentDateTime.getCurrentDate())){

            // set text color : green
            viewHolder.tvMain.setTextColor(Color.parseColor("#039E09"));
            viewHolder.tvMain.setText("Hôm nay, "+eating.getDate());
        }else{

            viewHolder.tvMain.setText("Ngày "+eating.getDate());
            // set text color : blue
            viewHolder.tvMain.setTextColor(Color.parseColor("#0371C8"));
        }

        viewHolder.tvSub.setText("Số bữa ăn: "+eating.getMeal());
        viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(eating, position);
            }
        });



        return convertView;
    }
}
