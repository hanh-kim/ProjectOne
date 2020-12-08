package vn.poly.personalmanagement.ui.fragment.money;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.ui.fragment.money.expenses.ExpenseFragment;
import vn.poly.personalmanagement.ui.fragment.money.incomes.IncomeFragment;


public class MoneyFragment extends Fragment implements Initialize, View.OnClickListener {

    CardView card_income, card_expense, card_statistic;
    TextView tvCurrentDate, tvCountIncomes, tvCountExpenses;
    MyDatabase mydatabase;
    IncomesDAO incomesDAO;
    ExpensesDAO expensesDAO;

    public MoneyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        initializeViews(view);
        initializeDatabase();
        card_expense.setOnClickListener(this);
        card_income.setOnClickListener(this);
        card_statistic.setOnClickListener(this);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvCountIncomes.setText(""+incomesDAO.getAllIncomes().size());
        tvCountExpenses.setText(""+expensesDAO.getAllExpenses().size());
        return view;
    }


    @Override
    public void onClick(View view) {
        if (card_expense.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_money_root, new ExpenseFragment()).commit();
        } else if (card_income.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_money_root, new IncomeFragment()).commit();
        } else if (card_statistic.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_money_root, new StatisticalFragment()).commit();
        }
    }

    @Override
    public void initializeViews(View view) {
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        card_expense = view.findViewById(R.id.cardExpense);
        card_income = view.findViewById(R.id.cardIncome);
        card_statistic = view.findViewById(R.id.cardStatistic);
        tvCountExpenses = view.findViewById(R.id.tvCountExpenses);
        tvCountIncomes = view.findViewById(R.id.tvCountIncomes);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        incomesDAO = new IncomesDAO(mydatabase);
        expensesDAO = new ExpensesDAO(mydatabase);
    }
}