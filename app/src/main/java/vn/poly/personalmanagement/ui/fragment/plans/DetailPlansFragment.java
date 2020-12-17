package vn.poly.personalmanagement.ui.fragment.plans;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class DetailPlansFragment extends Fragment implements Initialize, View.OnClickListener {

    final String keyName = "fragName";
    TextView tvBack, tvEdit, tvPlanTime, tvDate, tvDelete;
    TextView tvPlanDate, tvDateToday;
    EditText edtTitle, edtDescription;
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    int isEditing = 0;
    Bundle bundle;


    public DetailPlansFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_plans, container, false);
        initializeDatabase();
        initializeViews(view);
        tvBack.setOnClickListener(this);
        tvPlanDate.setOnClickListener(this);
        tvPlanTime.setOnClickListener(this);

        tvDelete.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        bundle = getArguments();
        if (bundle != null) {
//            if (getArguments().getString(keyName).equals(MainPlansFragment.FRAG_NAME)) {
//                tvEdit.setEnabled(false);
//                tvEdit.setVisibility(View.GONE);
//            } else {
//                tvEdit.setEnabled(true);
//                tvEdit.setVisibility(View.VISIBLE);
//            }

            Plan plan = (Plan) getArguments().get("plan");
            edtTitle.setText(plan.getPlanName());
            tvPlanTime.setText(plan.getTime());

            tvPlanDate.setText(plan.getDate());
            edtDescription.setText(plan.getDescription());
        }


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if (tvBack.equals(v)) {
            back();

        } else if (tvEdit.equals(v)) {
            edit();
        } else if (tvPlanTime.equals(v)) {
            chooseTime(tvPlanTime);
        } else if (tvPlanDate.equals(v)) {
            chooseDate(tvPlanDate);
        } else if (tvDelete.equals(v)) {
            delete();
        }

    }


    @Override
    public void initializeViews(View view) {
        tvDelete = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
//        tvDate = view.findViewById(R.id.tvDate);
        tvPlanDate = view.findViewById(R.id.tvPlanDate);
        tvPlanTime = view.findViewById(R.id.tvPlanTime);
        edtTitle = view.findViewById(R.id.edtPlansTitle);
        edtDescription = view.findViewById(R.id.edtDescription);

    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }


    private void edit() {

        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtTitle.setEnabled(true);
            tvPlanTime.setEnabled(true);
            tvPlanDate.setEnabled(true);
            edtDescription.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            updatePlans();
        }

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


    private void updatePlans() {
        String title = edtTitle.getText().toString().trim();
        String time = tvPlanTime.getText().toString().trim();
        String date = tvPlanDate.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        String timeError = "Mời chọn thời gian";
        String dateError = "Mời chọn ngày";
        String dateError2 = "Mời chọn ngày sau ngày " + CurrentDateTime.getCurrentDate();


        if (title.isEmpty()) {
            edtTitle.setError("Mời nhập tên tên kế hoạch, công việc");
            edtTitle.setFocusable(true);
            return;
        }
        if (date.isEmpty() || date.equals(dateError) || date.equals(dateError2)) {
            tvPlanDate.setText(dateError);
            return;
        }


//        if (date.compareTo(CurrentDateTime.getCurrentDate()) < 0) {
//            tvPlanDate.setText(dateError2);
//            return;
//        }

        if (time.isEmpty() || time.equals(timeError)) {
            tvPlanTime.setText(timeError);
            return;
        }

        if (description.isEmpty()) {
            description = " ";
        }

        assert getArguments() != null;
        Plan plan = (Plan) getArguments().get("plan");
        assert plan != null;

        // ktra tgian gian ke hoach thay doi
        if (!time.equals(plan.getTime())) {
            if (plan.getDate().equals(CurrentDateTime.getCurrentDate())) {
                if (time.compareTo(CurrentDateTime.getCurrentTime()) <= 0) {
                    plan.setAlarmed(1);
                }else {
                    plan.setAlarmed(0);
                }
            }
        }

//        //ktra tgian nhắc nhở
//        if (!timeAlarm.equals(plan.getTimeAlarm())) {
//            if (plan.getDate().equals(CurrentDateTime.getCurrentDate())) {
//                if (plan.getTime().compareTo(CurrentDateTime.getCurrentTime()) <= 0) {
//                    plan.setAlarmed(0);
//                } else {
//                    if (timeAlarm.compareTo(CurrentDateTime.getCurrentTime()) > 0) {
//                        plan.setAlarmed(1);
//                    } else if (timeAlarm.compareTo(CurrentDateTime.getCurrentTime()) < 0) {
//                        plan.setAlarmed(0);
//                    }
//                }
//
//            } else if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate()) > 0) {
//                plan.setAlarmed(1);
//            }
//        }
        if (!date.equals(plan.getDate())){
            if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate())>0) {
                plan.setAlarmed(0);
            }
        }

        plan.setPlanName(title);
        plan.setDate(date);
        plan.setTime(time);
        plan.setDescribe(description);
        plansDAO.updateData(plan);
        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();

        tvEdit.setText("Sửa");
        edtTitle.setEnabled(false);
        tvPlanTime.setEnabled(false);
        edtDescription.setEnabled(false);
        tvPlanDate.setEnabled(false);
        isEditing = 0;
    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa công việc  này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Plan plan = (Plan) getArguments().get("plan");
                plansDAO.deleteData(plan);
                Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_LONG).show();
                back();
            }

        });
        builder.create().show();


    }

    private void back() {
        if (getArguments().getString(keyName).equals(MainPlansFragment.FRAG_NAME)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
        } else if (getArguments().getString(keyName).equals(PlansTodayFragment.FRAG_NAME)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (getArguments().getString(keyName).equals(PlansFutureFragment.FRAG_NAME)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_plans_root, new PlansFutureFragment()).commit();
        } else if (getArguments().getString(keyName).equals(PlansDateFragment.FRAG_NAME)) {
            Plan mPlan = (Plan) getArguments().get("plan");

            if (mPlan.getDate().equals(CurrentDateTime.getCurrentDate())) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_plans_root, new PlansTodayFragment())
                        .commit();
            } else if (mPlan.getDate().compareTo(CurrentDateTime.getCurrentDate()) > 0) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_plans_root, new PlansFutureFragment())
                        .commit();
            } else {
                PlansDateFragment plansDateFragment = new PlansDateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", mPlan.getDate());
                plansDateFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_plans_root, plansDateFragment)
                        .commit();
            }

        }
    }


}