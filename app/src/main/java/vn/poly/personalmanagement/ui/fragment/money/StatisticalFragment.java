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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;


public class StatisticalFragment extends Fragment implements Initialize, View.OnClickListener {

    TextView tvBack, tvTotalExpenses, tvTotalIncomes, tvCurrentDate, tvStartDate, tvEndDate;
    Button btnSearch;


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


        return view;
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new MoneyFragment()).commit();
        } else if (btnSearch.equals(view)) {
            search();
        } else if (tvStartDate.equals(view)) {
            chooseDate();
        } else if (tvEndDate.equals(view)) {
            chooseDate();
        }
    }

    private void chooseDate() {

    }

    private void search() {
    }

    @Override
    public void initializeViews(View view) {
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        btnSearch = view.findViewById(R.id.btnStatisticSearch);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvBack = view.findViewById(R.id.tvBack);
        tvTotalExpenses = view.findViewById(R.id.tvStatisticalTotalExpenses);
        tvTotalIncomes = view.findViewById(R.id.tvStatisticalTotalIncomes);

    }

    @Override
    public void initializeDatabase() {

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