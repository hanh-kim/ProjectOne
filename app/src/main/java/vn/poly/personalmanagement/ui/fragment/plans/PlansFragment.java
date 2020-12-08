package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

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

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;

import java.util.ArrayList;
import java.util.List;


public class PlansFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int idFrag=1;
    final String keyName = "idFrag";

    CardView cardPlansToday, cardPlansFuture;
    ListView lvPlans;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    TextView tvCurrentDay;
    List<String> listString = new ArrayList<>();
    String currentDay = CurrentDateTime.getCurrentDate().substring(0, 2);

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
        cardPlansToday.setOnClickListener(this);
        cardPlansFuture.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);
        tvCurrentDay.setText(currentDay);
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
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
    }

    @Override
    public void initializeDatabase() {

    }

    @Override
    public void onClick(View view) {
        if (cardPlansToday.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        } else if (cardPlansFuture.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new PlansFutureFragment()).commit();
        }else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            cancelSearch();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailPlansFragment detailPlansFragment = new DetailPlansFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName,idFrag);
        detailPlansFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, detailPlansFragment).commit();
    }
    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setText("");
    }
}