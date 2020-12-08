package vn.poly.personalmanagement.ui.fragment.security;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.User;
import vn.poly.personalmanagement.ui.activity.MainActivity;
import vn.poly.personalmanagement.ui.activity.SigninActivity;

import com.google.android.material.textfield.TextInputLayout;


public class SecurityFragment extends Fragment
        implements Initialize, View.OnClickListener {
    MyDatabase myDatabase;
    AccountDAO accountDAO;
    TextView tvLogout;
    TextInputLayout edtLEmail, edtLPassword, edtlUsername;
    MainActivity mainActivity;
    TextView tvChangePassword;

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

        mainActivity = (MainActivity) getActivity();
        String emailSaved = mainActivity.getEmailSaved();
        User user = accountDAO.getUserWithEmail(emailSaved);

//        if (user.getUsername().isEmpty()){
//            edtlUsername.getEditText().setText("Chưa có tên người dùng");
//        }else edtlUsername.getEditText().setText(user.getUsername());
        edtLEmail.getEditText().setText(emailSaved);
        edtLPassword.getEditText().setText(user.getPassword());
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_security_root, new ChangePasswordFragment());
//                transaction.commit();
                Toast.makeText(getActivity(),"toasssssssssss",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvLogout.equals(v)) {
            startActivity(new Intent(getActivity(), SigninActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void initializeViews(View view) {
        tvLogout = view.findViewById(R.id.tvLogout);
        edtLPassword = view.findViewById(R.id.edtlPassword);
        edtLEmail = view.findViewById(R.id.edtlEmail);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
//        edtlUsername=view.findViewById(R.id.edtlUsername);
    }

    @Override
    public void initializeDatabase() {
        myDatabase = new MyDatabase(getActivity());
        accountDAO = new AccountDAO(myDatabase);
    }


}