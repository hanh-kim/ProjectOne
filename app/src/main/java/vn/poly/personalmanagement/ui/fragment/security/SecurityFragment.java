package vn.poly.personalmanagement.ui.fragment.security;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.ui.activity.SigninActivity;
import com.google.android.material.textfield.TextInputLayout;


public class SecurityFragment extends Fragment
        implements Initialize, View.OnClickListener {

    TextView tvLogout;
    TextInputLayout edtLEmail, edtLPassword;
    public SecurityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        initializeDatabase();
        initializeViews(view);
        tvLogout.setOnClickListener(this);
        edtLEmail.getEditText().setText(" hanh@gmail.com");
        edtLPassword.getEditText().setText("123456");


        return view;
    }

    @Override
    public void onClick(View v) {
         if (tvLogout.equals(v)){
             startActivity(new Intent(getActivity(), SigninActivity.class));
             getActivity().finish();
         }
    }

    @Override
    public void initializeViews(View view) {
        tvLogout = view.findViewById(R.id.tvLogout);
        edtLPassword=view.findViewById(R.id.edtlPassword);
        edtLEmail=view.findViewById(R.id.edtlEmail);

    }

    @Override
    public void initializeDatabase() {

    }
}