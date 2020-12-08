package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.Plan;
import vn.poly.personalmanagement.ui.fragment.money.incomes.IncomeFragment;


public class AddWorkFragment extends Fragment implements View.OnClickListener, Initialize {


    TextView tvDone, tvBack, tvPlanTime, tvPlanTimeAlarm, tvPlanDate;
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
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvDone.equals(v)) {

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        }
    }

    @Override
    public void initializeViews(View view) {
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
        String date = tvPlanDate.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        String timeError="Mời chọn thời gian";
        String dateError ="Mời chọn ngày";
        if (title.isEmpty()) {
            edtTitle.setError("Mời nhập tên tên kế hoạch, công việc");
            edtTitle.setFocusable(true);
            return;
        }
        if (time.isEmpty()|| time.equals(timeError)) {
            tvPlanTime.setText(timeError);
            return;
        }
        if (timeAlarm.isEmpty()) {
            tvPlanTimeAlarm.setText(time);
        }
        if (date.isEmpty()||date.equals(dateError)) {
            tvPlanDate.setText(dateError);
            return;
        }

        if (description.isEmpty()) {
            description = " ";
        }
        int isAlarm =1;
        Plan plan = new Plan();
        plan.setPlanName(title);
        plan.setDate(date);
        plan.setTime(time);
        plan.setTimeAlarm(timeAlarm);
        plan.setDescribe(description);
        plan.setAlarmed(isAlarm);
        plansDAO.addData(plan);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, new PlansTodayFragment()).commit();
    }
}