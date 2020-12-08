package vn.poly.personalmanagement.ui.fragment.health.eating;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.eating.EatingAdapter;
import vn.poly.personalmanagement.adapter.health.eating.MealAdapter;
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Meal;
import vn.poly.personalmanagement.ui.fragment.health.HealthFragment;


public class EatingFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    TextView tvBack, tvCurrentDate, tvCountItem;
    CardView cardToday;
    ListView lvEating;
    ImageView icAdd;
    final String keyName = "idFrag";
    public static final int ID_FRAG = 0;
    public static final String NAME_FRAG = EatingFragment.class.getName();

    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountMealToday, tvCountEating;
    FrameLayout layoutSearch;
    EditText edtSearch;
    EatingAdapter eatingAdapter;
    List<Eating> eatingList;
    List<Eating> eatingListSearch;
    Mydatabase mydatabase;
    EatingDAO eatingDAO;
    MealAdapter mealAdapter;
    List<Meal> mealList;

    public EatingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_eating, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        showEatingList();
        countMealsToday();
        countItem();
        tvBack.setOnClickListener(this);
        lvEating.setOnItemClickListener(this);
        cardToday.setOnClickListener(this);
        icAdd.setOnClickListener(this);

        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        lvResultSearch.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new HealthFragment()).commit();
        } else if (cardToday.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new MealsTodayFragment()).commit();
        } else if (icAdd.equals(v)) {
            AddMealFragment addMealFragment = new AddMealFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, ID_FRAG);
            addMealFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, addMealFragment).commit();

        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
            hideSoftKeyboard();
            cancelSearch();
        }

    }


    @Override
    public void initializeViews(View view) {
        eatingAdapter = new EatingAdapter();
        mealAdapter = new MealAdapter();
        cardToday = view.findViewById(R.id.cardEatingToday);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvEating = view.findViewById(R.id.lvEating);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        icAdd = view.findViewById(R.id.icAdd);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvCountMealToday = view.findViewById(R.id.tvCountMealsToday);

    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        eatingDAO = new EatingDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Bundle bundle = new Bundle();
        MealsFragment mealsFragment = new MealsFragment();
        bundle.putString("date", eatingList.get(position).getDate());
        mealsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, mealsFragment).commit();

    }

    private void countMealsToday() {
        List<Meal> mealList = eatingDAO.getAllMealWithDate(CurrentDateTime.getCurrentDate());
        tvCountMealToday.setText("" + mealList.size());
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
        // hideSoftKeyboard();
        showEatingList();

    }

    private void startSearch() {
        edtSearch.setHint("Nhập ngày dd/mm/yyyy");
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setEnabled(true);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sDate = edtSearch.getText().toString().trim();
//                mealList = getMealList(sDate);
//                showMealList(mealList);
                showEatingListSearch(sDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private List<Meal> getMealList(String s) {
        return eatingDAO.getResultSearchedWithDate(s);
    }

    private List<Eating> getEatingList() {
        return eatingDAO.getEatingList();
    }

    private List<Eating> getEatingList(String date) {
        return eatingDAO.getEatingList(date);
    }

    private void showEatingListSearch(String date) {
        eatingList = getEatingList(date);
        eatingAdapter.setDataAdapter(eatingList, new EatingAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Eating eating, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa tất cả bữa ăn ngày này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eatingDAO.deleteDataWithDate(eating.getDate());
                        eatingList.remove(position);
                        eatingAdapter.notifyDataSetChanged();
                        countItem();
                        countMealsToday();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });
        lvResultSearch.setAdapter(eatingAdapter);
    }

    private void showEatingList() {
        eatingList = getEatingList();
        eatingAdapter.setDataAdapter(eatingList, new EatingAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Eating eating, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa tất cả bữa ăn ngày này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eatingDAO.deleteDataWithDate(eating.getDate());
                        eatingList.remove(position);
                        eatingAdapter.notifyDataSetChanged();
                        countItem();
                        countMealsToday();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });
        lvEating.setAdapter(eatingAdapter);
    }

    private void countItem() {
        tvCountItem.setText("Có " + getEatingList().size() + " mục");
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

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}