package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.plans.PlansAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class PlansTodayFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansTodayFragment.class.getName();
    final String keyName = "fragName";
    TextView tvDateToday, tvDone;
    ListView lvPlansToday;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountItem;
    FrameLayout layoutSearch;
    EditText edtSearch;
    Mydatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;

    public PlansTodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plans_today, container, false);
        initializeViews(view);
        initializeDatabase();
        tvDateToday.setText("Hôm nay, "+CurrentDateTime.getCurrentDate());
        tvDone.setOnClickListener(this);
        lvPlansToday.setOnItemClickListener(this);
        icAdd.setOnClickListener(this);

        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        countItem();
        showPlans();

        return view;
    }

    @Override
    public void initializeViews(View view) {
        tvDateToday = view.findViewById(R.id.tvDateToday);
        tvDone = view.findViewById(R.id.tvDone);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        lvPlansToday = view.findViewById(R.id.lvPlansToday);
        icAdd = view.findViewById(R.id.icAdd);
        tvCountItem = view.findViewById(R.id.tvCountItem);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }

    @Override
    public void onClick(View view) {
        if (tvDone.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new PlansFragment()).commit();
        } else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            cancelSearch();
        } else if (icAdd.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new AddWorkFragment()).commit();
        }
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailPlansFragment detailPlansFragment = new DetailPlansFragment();

        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putSerializable("plan",getPlansToday().get(position));
        detailPlansFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, detailPlansFragment).commit();
    }

    private List<Plan> getPlansToday() {
        return plansDAO.getAllPlansWithDate(CurrentDateTime.getCurrentDate());
    }

    private void countItem() {
        if (getPlansToday().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansToday().size());

    }


    private void showPlans() {
        planList = getPlansToday();
        final PlansAdapter adapter = new PlansAdapter();

        adapter.setDataAdapter(planList, new PlansAdapter.OnNotificationListener() {
            @Override
            public void onClick(Plan plan, int position, ImageView ic) {
                if (plan.getAlarmed() == 1) {
                    plan.setAlarmed(0);
                    ic.setImageResource(R.drawable.ic_baseline_notifications_off);
                } else if (plan.getAlarmed() == 0) {
                    plan.setAlarmed(1);
                    ic.setImageResource(R.drawable.ic_baseline_notifications);
                }
                plansDAO.updateData(plan);
                adapter.notifyDataSetChanged();
                countItem();
            }
        });

        lvPlansToday.setAdapter(adapter);
    }

}