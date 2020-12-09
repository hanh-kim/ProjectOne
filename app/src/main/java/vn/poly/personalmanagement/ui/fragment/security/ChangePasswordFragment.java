package vn.poly.personalmanagement.ui.fragment.security;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.User;
import vn.poly.personalmanagement.ui.activity.MainActivity;


public class ChangePasswordFragment extends Fragment implements Initialize {


    EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    TextView tvBack, tvError;
    Button btnSave;
    MyDatabase myDatabase;
    AccountDAO accountDAO;
    MainActivity mainActivity;

    public ChangePasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        initializeDatabase();
        initializeViews(view);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_security_root,new SecurityFragment()).commit();
            }
        });
        mainActivity = (MainActivity) getActivity();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                savePass();
            }
        });


        return view;
    }

    @Override
    public void initializeViews(View view) {
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        tvBack = view.findViewById(R.id.tvBack);
        btnSave = view.findViewById(R.id.btnSave);
        tvError = view.findViewById(R.id.tvError);
    }

    @Override
    public void initializeDatabase() {
        myDatabase = new MyDatabase(getActivity());
        accountDAO = new AccountDAO(myDatabase);
    }

    @SuppressLint("ResourceAsColor")
    private void savePass() {
        String email = mainActivity.getEmailSaved();
        tvError.setText("");
      //  tvError.setTextColor(R.color.colorRed);
        User user = accountDAO.getUserWithEmail(email);
        String oldPass = edtOldPassword.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            tvError.setText("Các ô không được để trông!");
            return;
        }

        if (!oldPass.equals(user.getPassword())) {
            edtOldPassword.setError("");
            tvError.setText("Mật khẩu cũ không chính xác!");
            return;
        }

        if (newPass.length() < 6) {
            edtNewPassword.setError("");
            tvError.setText("Mật khẩu mới gồm ít nhất 6 kí tự!");
            return;
        }
        if (!confirmPass.equals(newPass)) {
            edtConfirmPassword.setError("");
            tvError.setText("Mật khẩu nhập lại không khớp!");
            return;
        }

        tvError.setTextColor(Color.parseColor("#019807"));
        tvError.setText("Đổi mật khẩu thành công!");

        accountDAO.changePassword(email,newPass);
        Toast.makeText(getActivity(),"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_security_root,new SecurityFragment())
                .commit();
    }

    private void hideSoftKeyboard(){
       View view = getActivity().getCurrentFocus();
       if (view!=null){
           InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
       }

    }
}