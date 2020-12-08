package vn.poly.personalmanagement.ui.fragment.health.eating;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Meal;
import vn.poly.personalmanagement.ui.fragment.health.HealthFragment;


public class AddMealFragment extends Fragment implements Initialize, View.OnClickListener {

    final String keyName = "idFrag";
    TextView tvBack, tvCurrentDate, tvDone, tvTime, tvDate;
    EditText edtTitle, edtDecription;
    ListView lvMeal;
    Mydatabase mydatabase;
    EatingDAO eatingDAO;


    public AddMealFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        //.........
        if (getArguments()!=null){
            if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG ||
                    getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
                tvDate.setVisibility(View.GONE);
            } else {
                tvDate.setVisibility(View.VISIBLE);
            }
            //.............
            if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
                tvCurrentDate.setText("Ngày "+getArguments().getString("date"));
            }
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if (getArguments().getInt(keyName) == NewEatingFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new NewEatingFragment()).commit();
            } else if (tvDone.equals(v)) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new NewEatingFragment()).commit();
            }
        } else if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new MealsTodayFragment()).commit();
            } else if (tvDone.equals(v)) {
                addMeal(new MealsTodayFragment());
            }
        } else if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
              back(new MealsFragment());
            } else if (tvDone.equals(v)) {
                addMeal(new MealsFragment());

            }
        } else if (getArguments().getInt(keyName) == EatingFragment.ID_FRAG||getArguments().getInt(keyName) == HealthFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new EatingFragment()).commit();
            } else if (tvDone.equals(v)) {
                addMeal( new MealsFragment());
            }
        }

        if (tvDate.equals(v)) {
          chooseDate();
        } else if (tvTime.equals(v)) {
            chooseTime();
        }


    }

    @Override
    public void initializeViews(View view) {
        tvBack = view.findViewById(R.id.tvBack);
        tvDone = view.findViewById(R.id.tvDone);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        edtTitle = view.findViewById(R.id.edtMealTitle);
        tvDate = view.findViewById(R.id.tvChooseDate);
        tvTime = view.findViewById(R.id.tvChooseTime);
        edtDecription  = view.findViewById(R.id.edtDescription);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        eatingDAO = new EatingDAO(mydatabase);

    }


    private void addMeal(Fragment fragment){
        String errorDate ="Mời chọn ngày";
        String errorTime ="Mời chọn thời gian";
        String title = edtTitle.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        String date="";
        String description = edtDecription.getText().toString().trim();
        if (time.isEmpty()|| time.equalsIgnoreCase(errorTime)){
            tvTime.setText(errorTime);
            Toast.makeText(getActivity(),errorTime,Toast.LENGTH_LONG).show();
            return;
        }
        if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG) {
            date = CurrentDateTime.getCurrentDate();
        }else  if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {

            date = getArguments().getString("date");
        }
        else {
            date = tvDate.getText().toString().trim();
            if (date.isEmpty()|| date.equalsIgnoreCase(errorDate)){
                tvDate.setText(errorDate);
                Toast.makeText(getActivity(),errorDate,Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (title.isEmpty()){
            title="Không có tiêu đề";
        }
        Meal  meal = new Meal();
        meal.setTitle(title);
        meal.setDate(date);
        meal.setTime(time);
        meal.setDetailMeal(description);
        eatingDAO.addData(meal);
        Bundle bundle = new Bundle();
        bundle.putString("date",date);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_health_root,fragment).commit();
    }

    private void chooseTime(){
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0,0,0,hourOfDay,minute);
                tvTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },hour,minute,true);

        timePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chooseDate(){
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
               tvDate.setText(simpleDateFormat.format( calendar.getTime()));
           }
       },year,month,day);
       datePickerDialog.show();
    }

    private void back(Fragment fragment){
        String date = getArguments().getString("date");
        Bundle bundle = new Bundle();
        bundle.putString("date",date);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_health_root,fragment).commit();

    }

}