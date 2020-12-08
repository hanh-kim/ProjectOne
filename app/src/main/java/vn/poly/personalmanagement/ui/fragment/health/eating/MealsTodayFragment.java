package vn.poly.personalmanagement.ui.fragment.health.eating;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.eating.MealAdapter;
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Meal;


public class MealsTodayFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int ID_FRAG = 2;
    final String keyName = "idFrag";
    TextView tvBack, tvCurrentDate, tvCount;
    EditText edtSearch;
    ListView lvMeal;
    ImageView icAdd;
    MyDatabase mydatabase;
    EatingDAO eatingDAO;
    MealAdapter mealAdapter;
    List<Meal> mealList;

    public MealsTodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_meals_today, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        lvMeal.setOnItemClickListener(this);
        icAdd.setOnClickListener(this);
        countItem();
        showMealList();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new EatingFragment()).commit();
        } else if (icAdd.equals(v)) {
            AddMealFragment addMealFragment = new AddMealFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, ID_FRAG);
            addMealFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, addMealFragment).commit();

        }
    }

    @Override
    public void initializeViews(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        tvBack = view.findViewById(R.id.tvBack);
        tvCount = view.findViewById(R.id.tvCountItem);
        lvMeal = view.findViewById(R.id.lvMeal);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        icAdd = view.findViewById(R.id.icAdd);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getContext());
        eatingDAO = new EatingDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailMealFragment detailMealFragment = new DetailMealFragment();
        Meal meal = mealList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(keyName, ID_FRAG);
        bundle.putSerializable("meal",meal);
        detailMealFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, detailMealFragment).commit();
    }

    private List<Meal> getMealList() {
        return eatingDAO.getAllMealWithDate(CurrentDateTime.getCurrentDate());
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
                        eatingDAO.deleteData(meal);
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

    private void removeItem(final Meal meal, final int position) {


    }

    private void countItem() {
        tvCount.setText("Số bữa ăn: " + getMealList().size());
    }
}