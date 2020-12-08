package vn.poly.personalmanagement.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword, tvToSignup, tvError;
    Button btnLogin;
    CheckBox cboRememberAcc;
    private String fileName = "account.txt";
    private String filePath = "MyAccount";
    private String keyEmail = "email";
    private String keyPassword = "password";
    private String keyCheck = "isRemember";
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    MyDatabase myDatabase;
    AccountDAO accountDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
        restoreInfoAccountRemembered();
        // views on click
        btnLogin.setOnClickListener(this);
        tvToSignup.setOnClickListener(this);
        //  tvForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            hideSoftKeyboard();
            login();
        } else if (v == tvToSignup) {
            startActivity(new Intent(SigninActivity.this, SignupActivity.class));
        }
    }

    private void initViews() {
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvToSignup = findViewById(R.id.tvToSignup);
        cboRememberAcc = findViewById(R.id.cboRemamberAcc);
        progressBar = findViewById(R.id.progressBar);
        myDatabase = new MyDatabase(SigninActivity.this);
        accountDAO = new AccountDAO(myDatabase);
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        tvError.setText("");
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Mời nhập đầy đủ thông tin đăng nhập!", Toast.LENGTH_LONG).show();
            if (email.isEmpty()) {
                edtEmail.setError("Mời nhập Email!");
                edtEmail.setFocusable(true);

            } else if (password.isEmpty()) {
                edtPassword.setError("Mời nhập mật khẩu!");
                edtPassword.setFocusable(true);

            }
            return;
        } else if (!accountDAO.checkExist(email, password)) {
            tvError.setText("Email hoặc Mật khẩu không chính xác");
            return;
        } else {
            saveInfoAccountRemembered();
            saveEmailLogin();
//            loading();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog.dismiss();
//                }
//            }, 1600);
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            SigninActivity.this.finish();

        }

    }

    public void saveInfoAccountRemembered() {
        boolean isRemember = cboRememberAcc.isChecked();
        SharedPreferences sharedPreferences = getSharedPreferences("my_account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (!isRemember) {
            editor.clear();
        } else {
            editor.putString(keyEmail, email);
            editor.putString(keyPassword, password);
            editor.putBoolean(keyCheck, isRemember);
        }
        editor.commit();

    }

    public void saveEmailLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_email", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = edtEmail.getText().toString().trim();
        editor.putString(keyEmail, email);
        editor.commit();

    }

    public void restoreInfoAccountRemembered() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_account", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean(keyCheck, false);
        if (isRemember) {
            edtEmail.setText(sharedPreferences.getString(keyEmail, ""));
            edtPassword.setText(sharedPreferences.getString(keyPassword, ""));
        }
        cboRememberAcc.setChecked(isRemember);
    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view!=null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

    private void loading() {
        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.show();
        // Set content view
        View view = getLayoutInflater().inflate(R.layout.layout_progress_dialog, null);
        progressDialog.setContentView(view);
        TextView tvProgress = view.findViewById(R.id.tvProgress);
        tvProgress.setText("Đăng nhập...");
        // Set transparent background
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

    private void loadingProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_progress_dialog, null);
        builder.setView(view);
        TextView tvProgress = view.findViewById(R.id.tvProgress);
        tvProgress.setText("Đăng nhập...");
        // Set transparent background
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        builder.create().show();


    }

}