package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.plans.PlansAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class PlansDateFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansDateFragment.class.getName();
    final String keyName = "fragName";
    TextView tvDateToday, tvBack, tvDelete;
    ListView lvPlans;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountItem;
    FrameLayout layoutSearch;
    EditText edtSearch;
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;
    Bundle bundle;

    public PlansDateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plans_date, container, false);
        initializeViews(view);
        initializeDatabase();
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);

        bundle=getArguments();
        if (getArguments()!= null){
           String date = bundle.getString("date");
           if (getArguments().getString("date").equals(CurrentDateTime.getCurrentDate())){
               tvDateToday.setText("Hôm nay, "+date);
           }else tvDateToday.setText("Ngày "+date);

            countItem();
            showPlans();

        }

        return view;
    }

    @Override
    public void initializeViews(View view) {
        tvDateToday = view.findViewById(R.id.tvDateToday);
        tvBack = view.findViewById(R.id.tvBack);
        tvDelete = view.findViewById(R.id.tvDelete);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        lvPlans = view.findViewById(R.id.lvPlans);

        tvCountItem = view.findViewById(R.id.tvCountItem);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
        }  else if (tvDelete.equals(view)) {
           plansDAO.deleteDataWithDate(bundle.getString("date"));
            Toast.makeText(getActivity(),"Xóa thành công!",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
        }
    }

//    private void cancelSearch() {
//        layoutSearch.setVisibility(View.GONE);
//        edtSearch.setText("");
//    }
//
//    private void startSearch() {
//        layoutSearch.setVisibility(View.VISIBLE);
//        edtSearch.setText("");
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailPlansFragment detailPlansFragment = new DetailPlansFragment();

        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putSerializable("plan",getPlansList().get(position));
        detailPlansFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, detailPlansFragment).commit();
    }

    private List<Plan> getPlansList() {
        return plansDAO.getAllPlansWithDate(bundle.getString("date"));
    }

    private void countItem() {

        if (getPlansList().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansList().size());

    }

    private void showPlans() {
        planList = getPlansList();
        final PlansAdapter adapter = new PlansAdapter();
        adapter.setDataAdapter(planList, new PlansAdapter.OnNotificationListener() {
            @Override
            public void onClick(Plan plan, int position, ImageView ic) {

                if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate())<0){
                    plansDAO.deleteData(plan);
                    planList.remove(position);
                    adapter.notifyDataSetChanged();
                    countItem();
                }else {
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
            }
        });
        lvPlans.setAdapter(adapter);
    }



}