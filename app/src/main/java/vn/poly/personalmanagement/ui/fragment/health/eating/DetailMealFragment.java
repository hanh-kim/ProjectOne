package vn.poly.personalmanagement.ui.fragment.health.eating;

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
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Meal;


public class DetailMealFragment extends Fragment implements Initialize, View.OnClickListener {

    TextView tvBack, tvEdit, tvTime, tvDate, tvDelete;
    EditText edtMealTitle, edtDesciption;
    int isEditing = 0;
    final String keyName = "idFrag";
    Meal meal;
    Mydatabase mydatabase;
    EatingDAO eatingDAO;

    public DetailMealFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_meal, container, false);
        initializeDatabase();
        initializeViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tvEdit.setOnClickListener(this);
            tvBack.setOnClickListener(this);
            tvDate.setOnClickListener(this);
            tvTime.setOnClickListener(this);
            tvDelete.setOnClickListener(this);
            meal = (Meal) bundle.get("meal");
            edtMealTitle.setText(meal.getTitle());
            tvTime.setText(meal.getTime());
            edtDesciption.setText(meal.getDetailMeal());
            if (meal.getDate().equals(CurrentDateTime.getCurrentDate())){
                tvDate.setText("Hôm nay, " + meal.getDate());
            }else tvDate.setText("Ngày " + meal.getDate());

        }

        return view;
    }

    @Override
    public void onClick(View v) {

        if (tvBack.equals(v)) {
            if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
                MealsFragment mealsFragment = new MealsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", meal.getDate());
                mealsFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_health_root, mealsFragment).commit();
            } else if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG) {
                MealsTodayFragment mealsTodayFragment = new MealsTodayFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", meal.getDate());
                mealsTodayFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_health_root, mealsTodayFragment).commit();
            }else if (getArguments().getInt(keyName) == EatingFragment.ID_FRAG) {

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_health_root, new EatingFragment()).commit();
            }

        } else if (tvEdit.equals(v)) {
            if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
                edit(new MealsFragment());

            } else if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG) {
                edit(new MealsTodayFragment());

            } else if (getArguments().getInt(keyName) == EatingFragment.ID_FRAG) {
                edit(new EatingFragment());

            }


        } else if (tvTime.equals(v)) {
            chooseTime();


        } else if (tvDelete.equals(v)) {

            if (getArguments().getInt(keyName) == MealsFragment.ID_FRAG) {
                delete(meal,new MealsFragment());
            } else if (getArguments().getInt(keyName) == MealsTodayFragment.ID_FRAG) {
                delete(meal, new MealsTodayFragment());
            }else if (getArguments().getInt(keyName) == EatingFragment.ID_FRAG) {
                delete(meal, new EatingFragment());
            }

        }


    }


    @Override
    public void initializeViews(View view) {
        tvDelete = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        //  tvDate = view.findViewById(R.id.tvChooseDate);
        tvTime = view.findViewById(R.id.tvChooseTime);
        tvDate = view.findViewById(R.id.tvCurrentDate);
        edtMealTitle = view.findViewById(R.id.edtMealTitle);
        edtDesciption = view.findViewById(R.id.edtDescription);


    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        eatingDAO = new EatingDAO(mydatabase);
    }

    private void edit(Fragment fragment) {
        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtMealTitle.setEnabled(true);
            tvDate.setEnabled(true);
            tvTime.setEnabled(true);
            edtDesciption.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            edtMealTitle.setEnabled(false);
            tvDate.setEnabled(false);
            tvTime.setEnabled(false);
            edtDesciption.setEnabled(false);
            isEditing = 0;
            updateMeal(fragment);

            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_LONG).show();
        }

    }

    private void chooseTime() {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute);
                tvTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
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

    private void delete(final Meal meal, final Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa bữa ăn này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eatingDAO.deleteData(meal);
                Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("date", meal.getDate());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_health_root, fragment).commit();
            }
        });
        builder.create().show();

    }

    private void updateMeal(Fragment fragment) {
        String errorDate = "Mời chọn ngày";
        String errorTime = "Mời chọn thời gian";
        String title = edtMealTitle.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        String description = edtDesciption.getText().toString().trim();
        if (time.isEmpty() || time.equalsIgnoreCase(errorTime)) {
            tvTime.setText(errorTime);
            Toast.makeText(getActivity(), errorTime, Toast.LENGTH_LONG).show();
            return;
        }

        if (title.isEmpty()) {
            title = "Không có tiêu đề";
        }
        meal.setTitle(title);
        meal.setTime(time);
        meal.setDetailMeal(description);
        eatingDAO.updateData(meal);

    }

}