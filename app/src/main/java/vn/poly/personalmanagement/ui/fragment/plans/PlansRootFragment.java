package vn.poly.personalmanagement.ui.fragment.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.poly.personalmanagement.R;


public class PlansRootFragment extends Fragment {


    public PlansRootFragment() {
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
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_plans_root,new MainPlansFragment()).commit();
        View view= inflater.inflate(R.layout.fragment_plans_root, container, false);

        return view;
    }
}