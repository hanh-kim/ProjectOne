package vn.poly.personalmanagement.ui.fragment.money.incomes;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Income;


public class AddIncomesFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    TextView tvBack, tvDone, tvDate, tvChooseDate;
    EditText edtTitle, edtAmount, edtDescribe;
    Mydatabase mydatabase;
    IncomesDAO incomesDAO;

    public AddIncomesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_incomes, container, false);
        initializeDatabase();
        initializeViews(view);
        tvDate.setText("Hôm nay," + CurrentDateTime.getCurrentDate());
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvChooseDate.setText(CurrentDateTime.getCurrentDate());

        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new IncomeFragment()).commit();
        } else if (tvDone.equals(v)) {
            addIncome();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void initializeViews(View view) {
        tvBack = view.findViewById(R.id.tvBack);
        tvDone = view.findViewById(R.id.tvDone);
        tvDate = view.findViewById(R.id.tvCurrentDate);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtDescribe = view.findViewById(R.id.edtDescription);
        tvChooseDate = view.findViewById(R.id.tvChooseDate);

    }

    @Override
    public void initializeDatabase() {
        mydatabase= new Mydatabase(getActivity());
        incomesDAO = new IncomesDAO(mydatabase);
    }

    private void chooseDate() {
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
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addIncome(){
        String title =edtTitle.getText().toString().trim();
        String sAmount = edtAmount.getText().toString().trim();
        double amount =0;
        String description = edtDescribe.getText().toString().trim();
        String date = CurrentDateTime.getCurrentDate();
        String time = CurrentDateTime.getCurrentTime();
        if (title.isEmpty()){
            edtTitle.setError("Mời nhập tên khoản thu");
            edtTitle.setFocusable(true);
            return;
        }
        if (sAmount.isEmpty()){
            edtAmount.setError("Mời nhập số tiền đã thu");
            edtAmount.setFocusable(true);
            return;
        }else amount= Double.parseDouble(sAmount);
        if (description.isEmpty()){
            description=" ";
        }
        Income income = new Income();
        income.setTitle(title);
        income.setTime(time);
        income.setDate(date);
        income.setAmount(amount);
        income.setDescription(description);
        incomesDAO.addData(income);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, new IncomeFragment()).commit();
    }
}