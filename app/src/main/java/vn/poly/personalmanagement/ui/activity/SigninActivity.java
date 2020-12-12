package vn.poly.personalmanagement.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword, tvToSignup, tvError;
    RelativeLayout btnSignin;

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
    TextView tvSignin;
    ImageView icSuccessful;


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
        restoreInfoAccountRemembered();
        // views on click
        btnSignin.setOnClickListener(this);
        tvToSignup.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        //  tvForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSignin) {
            hideSoftKeyboard();
            //  signin(); // sign in with firebase

            login();

        } else if (v == tvToSignup) {
            startActivity(new Intent(SigninActivity.this, SignupActivity.class));
        }
    }

    private void initViews() {
        icSuccessful = findViewById(R.id.icSuccessful);
        progressBar = findViewById(R.id.progressBar);
        tvSignin = findViewById(R.id.tvSignin);
        tvError = findViewById(R.id.tvError);
        btnSignin = findViewById(R.id.btnSignin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvToSignup = findViewById(R.id.tvToSignup);
        cboRememberAcc = findViewById(R.id.cboRemamberAcc);
        myDatabase = new MyDatabase(SigninActivity.this);
        accountDAO = new AccountDAO(myDatabase);
    }

    private void signin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SigninActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(SigninActivity.this, MainActivity.class));
                            SigninActivity.this.finish();
                        } else {
                            Toast.makeText(SigninActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        tvError.setText("");
        tvError.setTextColor(Color.RED);
        tvSignin.setText("ĐĂNG NHẬP...");
        tvSignin.setTextColor(Color.WHITE);
        progressBar.setVisibility(View.VISIBLE);
        icSuccessful.setVisibility(View.INVISIBLE);
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Mời nhập đầy đủ thông tin đăng nhập!", Toast.LENGTH_LONG).show();
            if (email.isEmpty()) {
                edtEmail.setError("Mời nhập Email!");
                edtEmail.setFocusable(true);


            } else if (password.isEmpty()) {
                edtPassword.setError("Mời nhập mật khẩu!");
                edtPassword.setFocusable(true);

            }
            tvSignin.setText("ĐĂNG NHẬP");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        } else if (!accountDAO.checkExist(email, password)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvSignin.setText("ĐĂNG NHẬP");
                    progressBar.setVisibility(View.INVISIBLE);
                    tvError.setText("Email hoặc Mật khẩu không chính xác!");
                }
            }, 2000);

            return;
        } else {
            saveInfoAccountRemembered();
            saveEmailLogin();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvSignin.setText("ĐĂNG NHẬP THÀNH CÔNG");
                    tvSignin.setTextColor(Color.GREEN);
                    icSuccessful.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    tvError.setTextColor(Color.parseColor("#04DA3C"));
                    tvError.setText("");
//                    tvError.setText("Đăng nhập thành công");


                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SigninActivity.this, MainActivity.class));
                            SigninActivity.this.finish();
                        }
                    }, 1000);
                }
            }, 2000);

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

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


}