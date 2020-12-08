package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.Initialize;


public class DetailPlansFragment extends Fragment implements Initialize, View.OnClickListener {

    final String keyName = "fragName";
    TextView tvBack, tvEdit, tvTime, tvDate, tvDelete;
    EditText edtTitle, edtDescibe;
    int isEditing = 0;

    public DetailPlansFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_plans, container, false);
        initializeDatabase();
        initializeViews(view);
        tvBack.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        if (getArguments().getString(keyName).equals( PlansFragment.FRAG_NAME)) {
            tvEdit.setEnabled(false);
        } else {
            tvEdit.setEnabled(true);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        if (tvBack.equals(v)) {
            if (getArguments().getString(keyName).equals(PlansFragment.FRAG_NAME)) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_plans_root, new PlansFragment()).commit();
            } else if (getArguments().getString(keyName).equals(PlansTodayFragment.FRAG_NAME)) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_plans_root, new PlansTodayFragment()).commit();
            } else if (getArguments().getString(keyName).equals(PlansFutureFragment.FRAG_NAME)) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_plans_root, new PlansFutureFragment()).commit();
            }

        } else if (tvEdit.equals(v)) {
            edit();
        } else if (tvTime.equals(v)) {
            chooseTime();
        } else if (tvDate.equals(v)) {
            chooseDate();
        }

    }


    @Override
    public void initializeViews(View view) {
        tvDate = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvDate = view.findViewById(R.id.tvPlanDate);
        tvTime = view.findViewById(R.id.tvPlanTime);
        edtTitle = view.findViewById(R.id.edtPlansTitle);
        edtDescibe = view.findViewById(R.id.edtDescription);
    }

    @Override
    public void initializeDatabase() {

    }

    private void chooseTime() {
    }

    private void chooseDate() {
    }

    private void edit() {

        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtTitle.setEnabled(true);
            tvDate.setEnabled(true);
            tvTime.setEnabled(true);
            edtDescibe.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            edtTitle.setEnabled(false);
            tvDate.setEnabled(false);
            tvTime.setEnabled(false);
            edtDescibe.setEnabled(false);
            isEditing = 0;
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_LONG).show();
        }

    }
}