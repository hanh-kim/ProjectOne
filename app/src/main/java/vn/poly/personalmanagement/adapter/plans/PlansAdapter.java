package vn.poly.personalmanagement.adapter.plans;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
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
        void onClick(Plan plan, int position);
    }

    private OnNotificationListener onNotificationListener;

    public void setDataAdapter(List<Plan> planList, OnNotificationListener onNotificationListener) {
        this.planList = planList;
        this.onNotificationListener = onNotificationListener;
        notifyDataSetChanged();
    }

    public void setDataAdapter(List<Plan> planList){
        this.planList = planList;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_plan, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvPlanTitle);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.tvTime = convertView.findViewById(R.id.tvTime);
            viewHolder.icAlarm = convertView.findViewById(R.id.icAlarm);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Plan plan = planList.get(position);
        if (plan.getDate().equals(CurrentDateTime.getCurrentDate())) {
            if (plan.getTime().compareTo(CurrentDateTime.getCurrentTime()) <= 0) {
                plan.setAlarmed(1);
                plansDAO.updateData(plan);
            } else {
                plan.setAlarmed(0);
                plansDAO.updateData(plan);
            }
        } else if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate()) > 0) {
            plan.setAlarmed(0);
            plansDAO.updateData(plan);
        }


        viewHolder.tvTitle.setText(plan.getPlanName());
        viewHolder.tvDate.setText("Ngày " + plan.getDate());


//        if (plan.getTime().compareTo(CurrentDateTime.getCurrentTime())<0){
//            viewHolder.tvTitle.setTextColor(R.color.colorGreen);
//        }else  viewHolder.tvTitle.setTextColor(R.color.colorRed);

        viewHolder.tvTime.setText("Thời gian: " + plan.getTime());
        if (plan.getAlarmed() == 1) {
            viewHolder.icAlarm.setImageResource(R.drawable.ic_baseline_notifications_off);
        } else if (plan.getAlarmed() == 0) {
            viewHolder.icAlarm.setImageResource(R.drawable.ic_baseline_notifications);
        }

        if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate()) < 0) {
            plan.setAlarmed(1);
            plansDAO.updateData(plan);
            viewHolder.icAlarm.setImageResource(R.drawable.ic_outline_remove);
        }

        viewHolder.icAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationListener.onClick(plan, position);

            }
        });

        notifyDataSetChanged();
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle, tvDate, tvTime;
        ImageView icAlarm;

    }
}
