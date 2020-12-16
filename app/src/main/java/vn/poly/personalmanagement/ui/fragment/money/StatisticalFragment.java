package vn.poly.personalmanagement.ui.fragment.money;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.Income;


public class StatisticalFragment extends Fragment implements Initialize, View.OnClickListener {

    TextView tvBack, tvTotalExpenses, tvTotalIncomes,
            tvCurrentDate, tvStartDate, tvEndDate, tvError;
    Button btnSearch;

    MyDatabase mydatabase;
    IncomesDAO incomesDAO;
    ExpensesDAO expensesDAO;
    List<Income> incomeList;

    public StatisticalFragment() {
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
        View view = inflater.inflate(R.layout.fragment_statistical2, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new MainMoneyFragment()).commit();
        } else if (btnSearch.equals(view)) {
            search();
        } else if (tvStartDate.equals(view)) {
            chooseDate(tvStartDate);
        } else if (tvEndDate.equals(view)) {
            chooseDate(tvEndDate);
        }
    }

    private String formatCurrency(long amount) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String sAmount = format.format(amount);
       return sAmount;
    }

    private void search() {

        String from = tvStartDate.getText().toString().trim();
        String to = tvEndDate.getText().toString().trim();


        if (from.isEmpty() || to.isEmpty()){
            tvError.setText("Mời nhập đủ ngày");
            return;
        }else {
            tvError.setText("");
            if (from.compareTo(to)>0){
                from = tvEndDate.getText().toString().trim();
                to = tvStartDate.getText().toString().trim();
                tvStartDate.setText(from);
                tvEndDate.setText(to);
            }

            List<Income> incomeList = incomesDAO.getAllIncomes(from,to);
            List<Expense> expenseList = expensesDAO.getAllExpenses(from,to);
            long amountIncomes =0;
            long amountExpenses =0;
            for (Income income : incomeList){
                amountIncomes+=income.getAmount();
            }
            for (Expense expense : expenseList){
                amountExpenses += expense.getAmount();
            }

            String aIn = formatCurrency(amountIncomes);
            String aEx = formatCurrency(amountExpenses);
            tvTotalIncomes.setText(aIn);
            tvTotalExpenses.setText(aEx);
        }

    }

    @Override
    public void initializeViews(View view) {
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvError = view.findViewById(R.id.tvError);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        btnSearch = view.findViewById(R.id.btnStatisticSearch);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvBack = view.findViewById(R.id.tvBack);
        tvTotalExpenses = view.findViewById(R.id.tvStatisticalTotalExpenses);
        tvTotalIncomes = view.findViewById(R.id.tvStatisticalTotalIncomes);


    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        incomesDAO = new IncomesDAO(mydatabase);
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    private void chooseDate(final TextView tv) {
        String date = "";
        /* format date: 01/01/2020 */
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tv.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}