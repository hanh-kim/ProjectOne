package vn.poly.personalmanagement.ui.fragment.money.expenses;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.money.MoneyAdapter;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.ObjectDate;
import vn.poly.personalmanagement.ui.fragment.money.MainMoneyFragment;


public class MainExpenseFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = MainExpenseFragment.class.getName();
    final String keyName = "fragName";
    TextView tvBack, tvCurrentDate, tvCountItem, tvCountExpensesToday;
    CardView cardToday;
    ListView lvExpenses;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    MyDatabase mydatabase;
    ExpensesDAO expensesDAO;
    List<ObjectDate> objectDateList;
    List<Expense> expenseList;

    public MainExpenseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_expense_main, container, false);
        initializeViews(view);
        initializeDatabase();
        cardToday.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        lvExpenses.setOnItemClickListener(this);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);

        countItem();
        showExpenseDate();

        return view;
    }


    @Override
    public void initializeViews(View view) {
        cardToday = view.findViewById(R.id.cardExpensesToday);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvExpenses = view.findViewById(R.id.lvExpenses);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvCountExpensesToday = view.findViewById(R.id.tvCountExpensesToday);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new MainMoneyFragment()).commit();
        } else if (cardToday.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new ExpensesTodayFragment()).commit();
        } else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpensesTodayFragment expensesTodayFragment = new ExpensesTodayFragment();
        ObjectDate objectDate = objectDateList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putString("date", objectDate.getDate());
        expensesTodayFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, expensesTodayFragment).commit();
    }

    private List<Expense> getExpensesListOfCurrentDate() {
        return expensesDAO.getAllExpensesWithDate(CurrentDateTime.getCurrentDate());
    }

    private List<ObjectDate> getMoneytDateList() {
        return expensesDAO.getExpensetDateList();
    }

    private void countItem() {
        if (getMoneytDateList().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getMoneytDateList().size());

        tvCountExpensesToday.setText("" + getExpensesListOfCurrentDate().size());
    }

    private void countExpensesToday() {
        tvCountExpensesToday.setText("" + getExpensesListOfCurrentDate().size());
    }

    private void showExpenseDate() {
        objectDateList = getMoneytDateList();
        final MoneyAdapter adapter = new MoneyAdapter();
        adapter.setDataAdapter(objectDateList, new MoneyAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final ObjectDate objectDate, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa những khoản chi trong ngày này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expensesDAO.deleteDataWithDate(objectDate.getDate());
                        objectDateList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();

            }
        });
        lvExpenses.setAdapter(adapter);
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void startSearch() {
        edtSearch.setHint("Nhập ngày dd/mm/yyyy");
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setEnabled(true);
        edtSearch.setText("");
        showResultSearch("");
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sDate = edtSearch.getText().toString().trim();
                showResultSearch(sDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void showResultSearch(String date) {

        final MoneyAdapter adapter = new MoneyAdapter();
        objectDateList = expensesDAO.getResultSearched(date);
        adapter.setDataAdapter(objectDateList, new MoneyAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final ObjectDate objectDate, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa ngày tập này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expensesDAO.deleteDataWithDate(objectDate.getDate());
                        objectDateList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        showExpenseDate();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });
        lvResultSearch.setAdapter(adapter);
    }
}
