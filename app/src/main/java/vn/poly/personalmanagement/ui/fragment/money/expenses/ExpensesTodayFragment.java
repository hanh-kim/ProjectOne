package vn.poly.personalmanagement.ui.fragment.money.expenses;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.money.expenses.ExpensesAdapter;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Expense;


public class ExpensesTodayFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = ExpensesTodayFragment.class.getName();
    final String keyName = "fragName";
    TextView tvBack, tvCurrentDate, tvCountItem, tvTotalExpenses, tvDelete;
    ListView lvExpenses;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    Mydatabase mydatabase;
    ExpensesDAO expensesDAO;
    List<Expense> expenseList;
    Bundle bundle;
    public ExpensesTodayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses_today, container, false);
        initializeViews(view);
        initializeDatabase();
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        lvExpenses.setOnItemClickListener(this);
        icAdd.setOnClickListener(this);
//       lvResultSearch.setOnItemClickListener(this);
//        tvToSearch.setOnClickListener(this);
//        tvCancelSearch.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            tvCurrentDate.setText("Ngày " + bundle.getString("date"));
            icAdd.setVisibility(View.GONE);
        }

        countItem();
        totalExpenses();
        showExpenses();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new ExpenseFragment()).commit();
        } else if (icAdd.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new AddExpenseFragment()).commit();
        } else if (tvDelete.equals(view)) {
            if (bundle != null) {
                if (getArguments().getString(keyName).equals(ExpenseFragment.FRAG_NAME)) {
                    deleteAll(getArguments().getString("date"));
                    getActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_money_root, new ExpenseFragment()).commit();
                } else  deleteAll(CurrentDateTime.getCurrentDate());
            }
            else  deleteAll(CurrentDateTime.getCurrentDate());

        }
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DetailExpenseFragment detailExpenseFragment = new DetailExpenseFragment();
        expenseList = getExpenseList();
        Expense expense = expenseList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putSerializable("expense", expense);
        detailExpenseFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, detailExpenseFragment).commit();
    }

    @Override
    public void initializeViews(View view) {
        icAdd = view.findViewById(R.id.icAdd);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvExpenses = view.findViewById(R.id.lvExpenses);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvTotalExpenses = view.findViewById(R.id.tvTotal);
        tvDelete = view.findViewById(R.id.tvDelete);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getActivity());
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    private List<Expense> getExpenseList() {
        if (getArguments() != null) {
            String date = getArguments().getString("date");
            return expensesDAO.getAllExpensesWithDate(date);
        }
        return expensesDAO.getAllExpensesWithDate(CurrentDateTime.getCurrentDate());
    }

    private void countItem() {
        if (getExpenseList().size() == 0) {
            tvCountItem.setText("Không có khoản chi nào");
        } else tvCountItem.setText("Có " + getExpenseList().size() + " khoản chi");
    }

    private void totalExpenses() {

        double total = 0;
        for (Expense expense : getExpenseList()) {
            total += expense.getAmount();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String sTotal = format.format(total);
        tvTotalExpenses.setText(sTotal);
    }


    private void showExpenses() {
        expenseList = getExpenseList();
        final ExpensesAdapter adapter = new ExpensesAdapter();
        adapter.setDataAdapter(expenseList, new ExpensesAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Expense expense, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa khoản chi này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expensesDAO.deleteData(expense);
                        expenseList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        totalExpenses();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();

            }
        });
        lvExpenses.setAdapter(adapter);
    }

    private void deleteAll(final String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa tất cả khoản chi này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                expensesDAO.deleteDataWithDate(date);
                countItem();
                totalExpenses();
                showExpenses();
                Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
            }

        });
        builder.create().show();
    }
}