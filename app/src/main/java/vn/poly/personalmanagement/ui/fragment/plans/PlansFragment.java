package vn.poly.personalmanagement.ui.fragment.plans;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.money.MoneyAdapter;
import vn.poly.personalmanagement.adapter.money.incomes.IncomesAdapter;
import vn.poly.personalmanagement.adapter.plans.PlanDateAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.ObjectDate;
import vn.poly.personalmanagement.model.Plan;

import java.util.ArrayList;
import java.util.List;


public class PlansFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansFragment.class.getName();
    final String keyName = "fragName";

    CardView cardPlansToday, cardPlansFuture;
    ListView lvPlans;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    TextView tvCurrentDay, tvCountPlansToday, tvCountPlansFuture, tvCountItem;
    List<String> listString = new ArrayList<>();
    String currentDay = CurrentDateTime.getCurrentDate().substring(0, 2);
    Mydatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;
    List<ObjectDate> planDateList;

    public PlansFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        initializeViews(view);
        initializeDatabase();
        cardPlansToday.setOnClickListener(this);
        cardPlansFuture.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);
        tvCurrentDay.setText(currentDay);
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);

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
        mydatabase = new Mydatabase(getActivity());
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
            cancelSearch();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       PlansDateFragment plansDateFragment = new PlansDateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.getString("date",getPlansDate().get(position).getDate());
        plansDateFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, plansDateFragment).commit();
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setText("");
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
                builder.setMessage("Bạn muốn xóa những khoản chi trong ngày này?");
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
        tvCountPlansToday.setText("" + getPlansFuture().size());
        if (getPlansDate().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansDate().size());


    }

}