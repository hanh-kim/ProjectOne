package vn.poly.personalmanagement.adapter.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.notes.NotesAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.model.Note;
import vn.poly.personalmanagement.model.Plan;

import java.util.List;

public class PlansAdapter extends BaseAdapter {

    Context context;
    List<Plan> planList;
    PlansDAO plansDAO;

    public PlansAdapter() {
    }

    public PlansAdapter(PlansDAO plansDAO) {
        this.plansDAO = plansDAO;
    }

    public interface OnNotificationListener {
        void onClick(Plan plan, int position,ImageView ic);
    }

    private OnNotificationListener onNotificationListener;
    public void setDataAdapter(List<Plan> planList, OnNotificationListener onNotificationListener) {
        this.planList = planList;
        this.onNotificationListener = onNotificationListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return planList.size();
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_plan,parent,false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvPlanTitle);
            viewHolder.tvDateTime = convertView.findViewById(R.id.tvDateTime);
            viewHolder.tvTimeAlarm = convertView.findViewById(R.id.tvTimeAlarm);
            viewHolder.icNotifycation = convertView.findViewById(R.id.icNotification);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Plan plan = planList.get(position);

        viewHolder.tvTitle.setText(plan.getPlanName());
        viewHolder.tvDateTime.setText(plan.getTime() + ", " + plan.getDate());
        viewHolder.tvTimeAlarm.setText("Nhắc nhở lúc: "+plan.getTimeAlarm());
        if (plan.getAlarmed()==0){
            viewHolder.icNotifycation.setImageResource(R.drawable.ic_baseline_notifications_off);
        }else  if (plan.getAlarmed()==1){
            viewHolder.icNotifycation.setImageResource(R.drawable.ic_baseline_notifications);
        }

        viewHolder.icNotifycation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onNotificationListener.onClick(plan,position,viewHolder.icNotifycation);

               if (plan.getAlarmed()==0){
                   viewHolder.icNotifycation.setImageResource(R.drawable.ic_baseline_notifications_off);
               }else  if (plan.getAlarmed()==1){
                   viewHolder.icNotifycation.setImageResource(R.drawable.ic_baseline_notifications);
               }

            }
        });

        notifyDataSetChanged();
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle, tvDateTime, tvTimeAlarm;
        ImageView icNotifycation;

    }
}
