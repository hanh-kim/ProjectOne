package vn.poly.personalmanagement.ui.fragment.money.expenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Expense;


public class AddExpenseFragment extends Fragment
        implements Initialize, AdapterView.OnItemClickListener, View.OnClickListener {


    TextView tvBack, tvDone, tvCurrentDate;
    EditText edtTitle, edtAmount, edtDescribe;
    Mydatabase mydatabase;
    ExpensesDAO expensesDAO;

    public AddExpenseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        initializeDatabase();
        initializeViews(view);
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvCurrentDate.setText("Hôm nay, "+CurrentDateTime.getCurrentDate());


        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new ExpensesTodayFragment()).commit();
        } else if (tvDone.equals(v)) {
           addExpenses();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void initializeViews(View view) {
        tvBack = view.findViewById(R.id.tvBack);
        tvDone = view.findViewById(R.id.tvDone);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtDescribe = view.findViewById(R.id.edtDescription);


    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getActivity());
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    private void addExpenses(){
        String title =edtTitle.getText().toString().trim();
        String sAmount = edtAmount.getText().toString().trim();
        double amount =0;
        String description = edtDescribe.getText().toString().trim();
        String date = CurrentDateTime.getCurrentDate();
        String time = CurrentDateTime.getCurrentTime();
        if (title.isEmpty()){
            edtTitle.setError("Mời nhập tên khoản chi");
            edtTitle.setFocusable(true);
            return;
        }
        if (sAmount.isEmpty()){
            edtAmount.setError("Mời nhập số tiền đã chi");
            edtAmount.setFocusable(true);
            return;
        }else amount= Double.parseDouble(sAmount);
        if (description.isEmpty()){
            description=" ";
        }
        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setTime(time);
        expense.setDate(date);
        expense.setAmount(amount);
        expense.setDescription(description);
        expensesDAO.addData(expense);

        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, new ExpensesTodayFragment()).commit();
    }
}