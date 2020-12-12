package vn.poly.personalmanagement.adapter.money.incomes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.money.expenses.ExpensesAdapter;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.Income;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class IncomesAdapter extends BaseAdapter {
    Context context;
    List<Income> incomeList;

    private OnItemRemoveListener onItemRemoveListener;
    public interface OnItemRemoveListener {
        void onRemove(Income income, int position);
    }

    public void setDataAdapter(List<Income> incomeList, OnItemRemoveListener onItemRemoveListener) {
        this.incomeList = incomeList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return incomeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_incomes, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvIncomesTitle);
            viewHolder.tvDateTime = convertView.findViewById(R.id.tvDateTimeIncomes);
            viewHolder.tvAmountMoney = convertView.findViewById(R.id.tvAmountIncomes);
            viewHolder.icDelete = convertView.findViewById(R.id.icDeleteIncomes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (IncomesAdapter.ViewHolder) convertView.getTag();
        }

       final   Income income = incomeList.get(position);
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String amount = format.format(income.getAmount());

        viewHolder.tvTitle.setText(income.getTitle());
        viewHolder.tvDateTime.setText(income.getTime() + ", ngày " + income.getDate());
        viewHolder.tvAmountMoney.setText("Số tiền thu: "+ amount);
        viewHolder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onItemRemoveListener.onRemove(income,position);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle, tvDateTime, tvAmountMoney;
        ImageView icDelete;
    }
}
