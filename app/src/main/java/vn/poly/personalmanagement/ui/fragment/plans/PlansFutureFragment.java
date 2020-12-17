package vn.poly.personalmanagement.ui.fragment.plans;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import java.util.Calendar;
import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.plans.PlansAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.AlarmNotificationReceiver;
import vn.poly.personalmanagement.methodclass.AlarmProvide;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.DateTimeFormat;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class PlansFutureFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansFutureFragment.class.getName();
    final String keyName = "fragName";
    TextView tvBack, tvDone;
    ImageView icAdd;
    ListView lvPlans;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountItem;
    FrameLayout layoutSearch;
    EditText edtSearch;
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;
    List<Plan> rerultList;

    Intent intent;
    PendingIntent pendingIntent;

    public PlansFutureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plans_future, container, false);
        initializeViews(view);
        initializeDatabase();
        intent = new Intent(getActivity(), AlarmNotificationReceiver.class);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);
        lvResultSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailPlansFragment detailPlansFragment = new DetailPlansFragment();
                Bundle bundle = new Bundle();
                bundle.putString(keyName, FRAG_NAME);
                bundle.putSerializable("plan", rerultList.get(position));
                detailPlansFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_plans_root, detailPlansFragment).commit();
            }
        });

        countItem();
        showPlansFuture();
        remindPlan();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (tvDone.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
        } else if (icAdd.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new AddPlansFragment()).commit();
        } else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }


    @Override
    public void initializeViews(View view) {
        tvDone = view.findViewById(R.id.tvBack);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        icAdd = view.findViewById(R.id.icAdd);
        lvPlans = view.findViewById(R.id.lvPlans);
        tvCountItem = view.findViewById(R.id.tvCountItem);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailPlansFragment detailPlansFragment = new DetailPlansFragment();
        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putSerializable("plan", getPlansFuture().get(position));
        detailPlansFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, detailPlansFragment).commit();
    }

    private List<Plan> getPlansFuture() {
        return plansDAO.getAllPlansFuture();
    }

    private void countItem() {
        if (getPlansFuture().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansFuture().size());

    }

    private void showPlansFuture() {
        planList = getPlansFuture();
        final PlansAdapter adapter = new PlansAdapter(plansDAO);
        adapter.setDataAdapter(planList, new PlansAdapter.OnNotificationListener() {
            @Override
            public void onClick(Plan plan, int position) {

            }
        });

        lvPlans.setAdapter(adapter);
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
        edtSearch.setHint("Nhập tên công việc, kế hoạch...");
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

    private void showResultSearch(String name) {
        rerultList = plansDAO.getPlansFutureSearched(name);
        final PlansAdapter adapter = new PlansAdapter(plansDAO);
        adapter.setDataAdapter(rerultList);
        lvResultSearch.setAdapter(adapter);
    }
    private List<Plan> getPlansToday() {
        return plansDAO.getAllPlansWithDate(CurrentDateTime.getCurrentDate());
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void remindPlan() {
        List<Plan> remindList = getPlansFuture();
        if (remindList != null) {
            for (Plan mPlan : remindList) {
                Calendar calendar = DateTimeFormat.parseCalendar(mPlan.getTime(), mPlan.getDate());
              //  Calendar calendar = DateTimeFormat.parseTimeToCalendar(mPlan.getTime());
                Bundle bundle = new Bundle();
                bundle.putSerializable("plan_key", mPlan);
                intent.putExtra("bundle_key", bundle);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), mPlan.getId(), intent, mPlan.getId());
                AlarmProvide.getAlarmManager(getActivity()).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }


    }
}