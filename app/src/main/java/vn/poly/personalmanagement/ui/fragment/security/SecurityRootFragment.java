package vn.poly.personalmanagement.ui.fragment.security;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.poly.personalmanagement.R;

public class SecurityRootFragment extends Fragment {



    public SecurityRootFragment() {
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
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_security_root,new SecurityFragment()).commit();
        return inflater.inflate(R.layout.fragment_security_root, container, false);
    }
}