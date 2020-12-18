package vn.poly.personalmanagement.ui.fragment.health.eating;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.eating.MealAdapter;
import vn.poly.personalmanagement.database.dao.MealsDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Meal;

public class MealsDateFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int ID_FRAG = 3;
    public static final String NAME_FRAG = MealsDateFragment.class.getName();
    final String keyName = "idFrag";

    TextView tvBack, tvEdit, tvDate, tvDelete, tvCountItem;
    ListView lvMeal;
    ImageView icAdd;
    int isEditing = 0;
    MyDatabase mydatabase;
    MealsDAO mealsDAO;
    List<Meal> mealList;
    MealAdapter mealAdapter;
    Bundle bundle;

    public MealsDateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_meals_date, container, false);
        initializeDatabase();
        initializeViews(view);

        tvEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        lvMeal.setOnItemClickListener(this);


        bundle = getArguments();
        if (bundle != null) {
            tvDate.setText(bundle.getString("date"));
        }


        countItem();
        showMealList();
        if (tvDate.getText().toString().trim().compareTo(CurrentDateTime.getCurrentDate()) < 0) {
            icAdd.setVisibility(View.GONE);
        } else icAdd.setVisibility(View.VISIBLE);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainEatingFragment()).commit();
        } else if (tvEdit.equals(v)) {
            edit();
        } else if (tvDelete.equals(v)) {
            mealsDAO.deleteDataWithDate(tvDate.getText().toString().trim());
            Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainEatingFragment()).commit();
        } else if (icAdd.equals(v)) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddMealFragment addMealFragment = new AddMealFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, ID_FRAG);
            bundle.putString("date", tvDate.getText().toString().trim());
            addMealFragment.setArguments(bundle);
            transaction.replace(R.id.fragment_health_root, addMealFragment);
            transaction.commit();
        } else if (tvDate.equals(v)) {
            chooseDate();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        DetailMealFragment detailMealFragment = new DetailMealFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName, ID_FRAG);
        Meal meal = mealList.get(position);
        bundle.putSerializable("meal", meal);
        detailMealFragment.setArguments(bundle);
        transaction.addToBackStack(NAME_FRAG);
        transaction.replace(R.id.fragment_health_root, detailMealFragment);
        transaction.commit();
    }

    @Override
    public void initializeViews(View view) {
        mealAdapter = new MealAdapter();
        tvDelete = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvDate = view.findViewById(R.id.tvChooseDate);
        lvMeal = view.findViewById(R.id.lvMeal);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        icAdd = view.findViewById(R.id.icAdd);

    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getContext());
        mealsDAO = new MealsDAO(mydatabase);
    }

    private void edit() {
        if (isEditing == 0) {
            tvEdit.setText("Xong");
            tvDate.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            tvDate.setEnabled(false);
            isEditing = 0;
            update();

        }

    }

    private List<Meal> getMealList() {
        String date = tvDate.getText().toString().trim();
        return mealsDAO.getAllMealWithDate(date);
    }

    private void countItem() {
        tvCountItem.setText("Số bữa ăn: " + getMealList().size());
    }

    private void showMealList() {
        mealList = getMealList();
        mealAdapter = new MealAdapter();
        mealAdapter.setDataAdapter(mealList, new MealAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Meal meal, final int position) {
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
                        mealsDAO.deleteData(meal);
                        mealList.remove(position);
                        mealAdapter.notifyDataSetChanged();
                        countItem();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });

        lvMeal.setAdapter(mealAdapter);
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

    private void update() {
        String errorDate = "Mời chọn ngày";
        String date = tvDate.getText().toString().trim();
        if (date.isEmpty() || date.equalsIgnoreCase(errorDate)) {
            tvDate.setText(errorDate);
            Toast.makeText(getActivity(), errorDate, Toast.LENGTH_LONG).show();
            return;
        }
        mealsDAO.updateMealDate(bundle.getString("date"),date);
        showMealList();
        if (tvDate.getText().toString().trim().compareTo(CurrentDateTime.getCurrentDate()) < 0) {
            icAdd.setVisibility(View.GONE);
        } else icAdd.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_LONG).show();
    }
}