package vn.poly.personalmanagement.adapter.money.expenses;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.model.Expense;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExpensesAdapter extends BaseAdapter {
    Context context;
    List<Expense> expenseList;
    private OnItemRemoveListener onItemRemoveListener;
    public interface OnItemRemoveListener {
        void onRemove(Expense expense, int position);
    }

    public void setDataAdapter(List<Expense> expenseList, OnItemRemoveListener onItemRemoveListener) {
        this.expenseList = expenseList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return expenseList.size();
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_expenses, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvExpensesTitle);
            viewHolder.tvDateTime = convertView.findViewById(R.id.tvDateTimeExpenses);
            viewHolder.tvAmountMoney = convertView.findViewById(R.id.tvAmountExpenses);
            viewHolder.icDelete = convertView.findViewById(R.id.icDeleteExpenses);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExpensesAdapter.ViewHolder) convertView.getTag();
        }

        final Expense expense = expenseList.get(position);

        // format currency
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String amount = format.format(expense.getAmount());

        viewHolder.tvTitle.setText(expense.getTitle());
        viewHolder.tvDateTime.setText(expense.getTime() + ", ngày " + expense.getDate());
        viewHolder.tvAmountMoney.setText("Tổng số tiền: "+ amount);
        viewHolder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoveListener.onRemove(expense, position);
            }
        });
        return convertView;
    }


    public class ViewHolder {
        TextView tvTitle, tvDateTime, tvAmountMoney;
        ImageView icDelete;
    }
}
