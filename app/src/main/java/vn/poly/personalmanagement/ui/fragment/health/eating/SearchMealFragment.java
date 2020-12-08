package vn.poly.personalmanagement.ui.fragment.health.eating;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.eating.MealAdapter;
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Meal;


public class SearchMealFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {


    final String keyName = "idFrag";
    public static final int ID_FRAG = 6;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountMealToday, tvCountEating;
    FrameLayout layoutSearch;
    EditText edtSearch;
    Mydatabase mydatabase;
    EatingDAO eatingDAO;
    List<Meal> mealList;
    MealAdapter mealAdapter;

    public SearchMealFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_meal, container, false);
        initializeDatabase();
        initializeViews(view);
        lvResultSearch.setOnItemClickListener(this);
        tvCancelSearch.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sDate = edtSearch.getText().toString().trim();
                mealList = getMealList(sDate);
                showMealList(mealList);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {

        if (tvCancelSearch.equals(v)){
            if (getArguments().getInt(keyName)==EatingFragment.ID_FRAG){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_health_root,new EatingFragment()).commit();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       DetailMealFragment detailMealFragment = new DetailMealFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName,ID_FRAG);
        bundle.putSerializable("meal", mealList.get(position));
        detailMealFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, detailMealFragment).commit();
    }

    private List<Meal> getMealList(String s) {
        return eatingDAO.getResultSearchedWithDate(s);
    }

    @Override
    public void initializeViews(View view) {
        mealAdapter = new MealAdapter();
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        eatingDAO = new EatingDAO(mydatabase);
    }
    private void showMealList(final List<Meal> mealList) {
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
                        //countItem();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });

        lvResultSearch.setAdapter(mealAdapter);
    }
}