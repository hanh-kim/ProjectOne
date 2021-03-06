package vn.poly.personalmanagement.adapter.plans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.model.ObjectDate;

public class PlanDateAdapter extends BaseAdapter {

    private List<ObjectDate> objectDateList;
    private OnItemRemoveListener onItemRemoveListener;
    public PlanDateAdapter() {
    }

    public interface OnItemRemoveListener {
        void onRemove(ObjectDate objectDate, int position);
    }

    public void setDataAdapter(List<ObjectDate> objectDateList, PlanDateAdapter.OnItemRemoveListener onItemRemoveListener) {
        this.objectDateList = objectDateList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }

    public void setDataAdapter(List<ObjectDate> objectDateList) {
        this.objectDateList = objectDateList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tvMain, tvSub;
        ImageView icRemove;

    }

    @Override
    public int getCount() {
        return objectDateList.size();
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
        final PlanDateAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new PlanDateAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_object, parent, false);
            viewHolder.tvMain = convertView.findViewById(R.id.tv_main_item);
            viewHolder.tvSub = convertView.findViewById(R.id.tv_sub_item);
            viewHolder.icRemove = convertView.findViewById(R.id.ic_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlanDateAdapter.ViewHolder) convertView.getTag();
        }

        final ObjectDate objectDate = objectDateList.get(position);
//        if (eating.getDate().compareTo(CurrentDateTime.getCurrentDate())<0){
//            viewHolder.tvMain.setTextColor(Color.parseColor("red"));
//        }

        if (objectDate.getDate().equals(CurrentDateTime.getCurrentDate())){
            // viewHolder.tvMain.setTextColor(Color.parseColor("green"));
            viewHolder.tvMain.setText("Hôm nay, "+objectDate.getDate());
        }else viewHolder.tvMain.setText("Ngày "+objectDate.getDate());

        viewHolder.tvSub.setText("Tất cả: "+objectDate.getAmount());
        viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(objectDate, position);
            }
        });
        notifyDataSetChanged();
        return convertView;
    }
}
