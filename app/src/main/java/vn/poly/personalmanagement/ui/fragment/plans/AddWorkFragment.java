package vn.poly.personalmanagement.ui.fragment.plans;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.Plan;
import vn.poly.personalmanagement.ui.fragment.money.incomes.IncomeFragment;


public class AddWorkFragment extends Fragment implements View.OnClickListener, Initialize {


    TextView tvDone, tvBack, tvPlanTime, tvPlanTimeAlarm, tvPlanDate, tvDateToday;
    EditText edtTitle, edtDescription;
    Mydatabase mydatabase;
    PlansDAO plansDAO;

    public AddWorkFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_work, container, false);
        initializeViews(view);
        initializeDatabase();
        tvDateToday.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvPlanTimeAlarm.setOnClickListener(this);
        tvPlanDate.setOnClickListener(this);
        tvPlanTime.setOnClickListener(this);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (tvDone.equals(v)) {
            addPlans();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (tvPlanTime.equals(v)) {
            chooseTime(tvPlanTime);
        } else if (tvPlanTimeAlarm.equals(v)) {
            chooseTime(tvPlanTimeAlarm);
        } else if (tvPlanDate.equals(v)) {
            chooseDate(tvPlanDate);
        }
    }

    @Override
    public void initializeViews(View view) {
        tvDateToday = view.findViewById(R.id.tvDateToday);
        tvDone = view.findViewById(R.id.tvDone);
        tvBack = view.findViewById(R.id.tvBack);
        edtTitle = view.findViewById(R.id.edtPlansTitle);
        edtDescription = view.findViewById(R.id.edtDescription);
        tvPlanTime = view.findViewById(R.id.tvPlanTime);
        tvPlanTimeAlarm = view.findViewById(R.id.tvPlanTimeAlarm);
        tvPlanDate = view.findViewById(R.id.tvPlanDate);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }

    private void addPlans() {
        String title = edtTitle.getText().toString().trim();
        String time = tvPlanTime.getText().toString().trim();
        String timeAlarm = tvPlanTimeAlarm.getText().toString().trim();
        String date = CurrentDateTime.getCurrentDate();
        String description = edtDescription.getText().toString().trim();

        String timeError = "Mời chọn thời gian";
        String dateError = "Mời chọn ngày";
        if (title.isEmpty()) {
            edtTitle.setError("Mời nhập tên tên kế hoạch, công việc");
            edtTitle.setFocusable(true);
            return;
        }
        if (time.isEmpty() || time.equals(timeError)) {
            tvPlanTime.setText(timeError);
            return;
        }
        if (timeAlarm.isEmpty()) {
            tvPlanTimeAlarm.setText(time);
            timeAlarm=time;
        }
//        if (date.isEmpty() || date.equals(dateError)) {
//            tvPlanDate.setText(dateError);
//            return;
//        }

        if (description.isEmpty()) {
            description = " ";
        }

        int isAlarm = 1;
        Plan plan = new Plan();
        plan.setPlanName(title);
        plan.setDate(date);
        plan.setTime(time);
        plan.setTimeAlarm(timeAlarm);
        plan.setDescribe(description);
        plan.setAlarmed(isAlarm);
        plansDAO.addData(plan);
        Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_LONG).show();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
    }

    private void chooseTime(final TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute);
                tv.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
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