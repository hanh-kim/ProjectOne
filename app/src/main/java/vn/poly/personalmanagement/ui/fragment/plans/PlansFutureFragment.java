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
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class PlansFutureFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansFutureFragment.class.getName();
    final String keyName = "fragName";
    TextView tvBack, tvDone;
    ImageView icAdd;
    ListView lvPlans;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch,tvCountItem;
    FrameLayout layoutSearch;
    EditText edtSearch;
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;
    public PlansFutureFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plans_future, container, false);
        initializeViews(view);
        initializeDatabase();
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);

        countItem();
        showPlans();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (tvDone.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new PlansFragment()).commit();
        } else if (icAdd.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new AddPlansFragment()).commit();
        }else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            cancelSearch();
        }
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setFocusable(true);
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


    private void showPlans() {
        planList = getPlansFuture();
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

        lvPlans.setAdapter(adapter);
    }
}