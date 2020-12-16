package vn.poly.personalmanagement.ui.fragment.money.expenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Expense;


public class DetailExpenseFragment extends Fragment
        implements Initialize, AdapterView.OnItemClickListener, View.OnClickListener {

    final String keyName = "fragName";
    TextView tvBack, tvEdit, tvDateTime, tvDelete;
    EditText edtTitle, edtAmount, edtDescribe;
    int isEditing = 0;
    MyDatabase mydatabase;
    ExpensesDAO expensesDAO;

    public DetailExpenseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_expense, container, false);
        initializeDatabase();
        initializeViews(view);

        tvEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Expense expense = (Expense) bundle.get("expense");
            if (expense.getDate().equals(CurrentDateTime.getCurrentDate())) {
                tvDateTime.setText("Hôm nay, " + expense.getTime());
            } else{
                tvEdit.setVisibility(View.GONE);
                tvDateTime.setText(expense.getTime() + ", Ngày " + expense.getDate());
            }

            edtTitle.setText(expense.getTitle());
            edtAmount.setText("" + expense.getAmount());
            edtDescribe.setText(expense.getDescription());
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        if (tvBack.equals(v)) {
            if (getArguments().getString(keyName).equals(ExpensesTodayFragment.FRAG_NAME)) {
                ExpensesTodayFragment expensesTodayFragment = new ExpensesTodayFragment();
                Bundle bundle = new Bundle();
                Expense expense = (Expense) getArguments().get("expense");
                bundle.putString("date", expense.getDate());
                expensesTodayFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_money_root, expensesTodayFragment).commit();

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_money_root, new ExpensesTodayFragment()).commit();
            } else if (getArguments().getString(keyName).equals(ExpensesTodayFragment.FRAG_NAME)) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_money_root, new ExpensesTodayFragment()).commit();
            }


        } else if (tvEdit.equals(v)) {
            if (getArguments().getString(keyName).equals(ExpensesTodayFragment.FRAG_NAME)) {
                edit();
            }


        } else if (tvDelete.equals(v)) {
            if (getArguments().getString(keyName).equals(ExpensesTodayFragment.FRAG_NAME)) {
                deleteExpenses(new ExpensesTodayFragment());
            }


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void initializeViews(View view) {
        tvDelete = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvDateTime = view.findViewById(R.id.tvDate);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtDescribe = view.findViewById(R.id.edtDescription);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    private void edit() {
        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtTitle.setEnabled(true);
            edtAmount.setEnabled(true);
            edtDescribe.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            updateExpenses();
        }

    }

    private void updateExpenses() {
        String title = edtTitle.getText().toString().trim();
        String sAmount = edtAmount.getText().toString().trim();
        long amount = Long.parseLong(sAmount);
        String description = edtDescribe.getText().toString().trim();
        String date = CurrentDateTime.getCurrentDate();
        String time = CurrentDateTime.getCurrentTime();
        if (title.isEmpty()) {
            edtTitle.setError("Mời nhập tên khoản chi");
            edtTitle.setFocusable(true);
            return;
        }
        if (sAmount.isEmpty()) {
            edtAmount.setError("Mời nhập số tiền đã chi");
            edtAmount.setFocusable(true);
            return;
        }
        if (description.isEmpty()) {
            description = " ";
        }
        Expense expense = (Expense) getArguments().get("expense");
        expense.setTitle(title);
        expense.setTime(time);
        expense.setDate(date);
        expense.setAmount(amount);
        expense.setDescription(description);
        expensesDAO.updateData(expense);
        Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_LONG).show();
        tvEdit.setText("Sửa");
        edtTitle.setEnabled(false);
        edtAmount.setEnabled(false);
        edtDescribe.setEnabled(false);
        isEditing = 0;
    }


    private void deleteExpenses(Fragment fragment) {
        Expense expense = (Expense) getArguments().get("expense");
        expensesDAO.deleteData(expense);
        Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, fragment).commit();
    }
}