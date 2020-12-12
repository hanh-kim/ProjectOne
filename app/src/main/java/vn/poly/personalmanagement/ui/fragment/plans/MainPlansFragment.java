package vn.poly.personalmanagement.ui.fragment.plans;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.money.MoneyAdapter;
import vn.poly.personalmanagement.adapter.plans.PlanDateAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.ObjectDate;
import vn.poly.personalmanagement.model.Plan;

import java.util.ArrayList;
import java.util.List;


public class MainPlansFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = MainPlansFragment.class.getName();
    final String keyName = "fragName";

    CardView cardPlansToday, cardPlansFuture;
    ListView lvPlans;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    TextView tvCurrentDay, tvCountPlansToday, tvCountPlansFuture, tvCountItem;
    String currentDay = CurrentDateTime.getCurrentDate().substring(0, 2);
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    List<ObjectDate> planDateList;
    List<ObjectDate> resultList;

    public MainPlansFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plans_main, container, false);
        initializeViews(view);
        initializeDatabase();
        cardPlansToday.setOnClickListener(this);
        cardPlansFuture.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);
        tvCurrentDay.setText(currentDay);

        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        lvResultSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlansDateFragment plansDateFragment = new PlansDateFragment();
                Bundle bundle = new Bundle();
                bundle.putString(keyName, FRAG_NAME);
                bundle.putString("date", resultList.get(position).getDate());
                plansDateFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_plans_root, plansDateFragment).commit();
            }
        });
        countItem();
        showPlanDate();

        return view;
    }

    @Override
    public void initializeViews(View view) {
        cardPlansToday = view.findViewById(R.id.cardPlansToday);
        cardPlansFuture = view.findViewById(R.id.cardPlansFuture);
        lvPlans = view.findViewById(R.id.lvPastPlans);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvCurrentDay = view.findViewById(R.id.tvCurrentDay);
        tvCountPlansFuture = view.findViewById(R.id.tvCountPlansFuture);
        tvCountPlansToday = view.findViewById(R.id.tvCountPlansToday);
        tvCountItem = view.findViewById(R.id.tvCountItem);


    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }

    @Override
    public void onClick(View view) {
        if (cardPlansToday.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (cardPlansFuture.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new PlansFutureFragment()).commit();
        } else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlansDateFragment plansDateFragment = new PlansDateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putString("date", planDateList.get(position).getDate());
        plansDateFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, plansDateFragment).commit();
    }



    private List<Plan> getPlansToday() {
        return plansDAO.getAllPlansWithDate(CurrentDateTime.getCurrentDate());
    }

    private List<Plan> getPlansFuture() {
        return plansDAO.getAllPlansFuture();
    }

    private List<ObjectDate> getPlansDate() {
        return plansDAO.getAllPlanDate();
    }

    private void showPlanDate() {
        planDateList = getPlansDate();
        final PlanDateAdapter adapter = new PlanDateAdapter();
        adapter.setDataAdapter(planDateList, new PlanDateAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final ObjectDate objectDate, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa những kế hoạch trong ngày này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        plansDAO.deleteDataWithDate(objectDate.getDate());
                        planDateList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();

            }
        });
        lvPlans.setAdapter(adapter);
    }

    private void countItem() {
        tvCountPlansToday.setText("" + getPlansToday().size());
        tvCountPlansFuture.setText("" + getPlansFuture().size());
        if (getPlansDate().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansDate().size());


    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        edtSearch.setHint("Nhập ngày dd/mm/yyyy");
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setEnabled(true);
        edtSearch.setText("");
        showResultSearch("");
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sDate = edtSearch.getText().toString().trim();
                showResultSearch(sDate);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void showResultSearch(String date) {
      //  planDateList = getPlansDate();
        resultList = plansDAO.getAllPlanDate(date);
        final PlanDateAdapter adapter = new PlanDateAdapter();
        adapter.setDataAdapter(resultList, new PlanDateAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final ObjectDate objectDate, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa những kế hoạch trong ngày này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        plansDAO.deleteDataWithDate(objectDate.getDate());
                        resultList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        showPlanDate();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();

            }
        });

        lvResultSearch.setAdapter(adapter);
    }


}