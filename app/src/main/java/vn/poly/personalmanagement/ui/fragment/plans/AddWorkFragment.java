package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.Initialize;


public class AddWorkFragment extends Fragment implements View.OnClickListener, Initialize {
  TextView tvDone, tvBack;

    public AddWorkFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_work, container, false);
        initializeViews(view);
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvDone.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
        }else  if (tvBack.equals(v)){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_plans_root,new PlansTodayFragment()).commit();
        }
    }

    @Override
    public void initializeViews(View view) {
        tvDone = view.findViewById(R.id.tvDone);
        tvBack= view.findViewById(R.id.tvBack);
    }

    @Override
    public void initializeDatabase() {

    }
}