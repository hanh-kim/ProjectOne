package vn.poly.personalmanagement.adapter.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.model.Plan;

import java.util.List;

public class PlansAdapter extends BaseAdapter {

    Context context;
    List<Plan> planList;

    public PlansAdapter(Context context, List<Plan> planList) {
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan,parent,false);

        return convertView;
    }
}
