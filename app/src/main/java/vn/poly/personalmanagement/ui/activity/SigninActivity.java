package vn.poly.personalmanagement.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
        restoreInfoAccountRemembered();
        // views on click
        btnSignin.setOnClickListener(this);
        tvToSignup.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){
            updateUI(currentUser);
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            SigninActivity.this.finish();
        }

    }

    @Override
    public void onClick(View v) {
        if (btnSignin.equals(v)) {
            hideSoftKeyboard();
            signinWithfirebaseAuth(); // sign in with firebase

            // login();

        } else if (tvToSignup.equals(v)) {
            startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            this.finish();
        } else if (tvForgotPassword.equals(v)) {
            forgotPassword();
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

    private void signinWithfirebaseAuth() {
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
        } else {
            // check internet is connected
            if (!checkConnected()) {
                tvSignin.setText("ĐĂNG NHẬP");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SigninActivity.this, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                             // get current user
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                updateUI(currentUser);
                                // check email is verified
                                if (!currentUser.isEmailVerified()) {
                                    currentUser.sendEmailVerification();
                                }
                                tvSignin.setText("ĐĂNG NHẬP THÀNH CÔNG");
                                tvSignin.setTextColor(Color.GREEN);
                                icSuccessful.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                                tvError.setTextColor(Color.parseColor("#04DA3C"));
                                tvError.setText("");
//                              tvError.setText("Đăng nhập thành công");
                                saveInfoAccountRemembered();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(new Intent(SigninActivity.this, MainActivity.class));
                                        SigninActivity.this.finish();
                                    }
                                }).start();

                            } else {

                                tvSignin.setText("ĐĂNG NHẬP");
                                progressBar.setVisibility(View.INVISIBLE);
                                tvError.setText("Email hoặc Mật khẩu không chính xác!");
                            }

                        }
                    });

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

    private boolean checkConnected() {
        ConnectivityManager manager = (ConnectivityManager) SigninActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        } else return false;
    }

    private void forgotPassword() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);

        View view = getLayoutInflater().inflate(R.layout.layout_email_reset_password, null);

        //  builder.setCancelable(false);
        final EditText edtEmailReset = view.findViewById(R.id.edtEmailResetPasword);
        final TextView tvError = view.findViewById(R.id.tvError);
        final TextView tvCancel = view.findViewById(R.id.tvCancel);
        final TextView tvSend = view.findViewById(R.id.tvSend);
        final ProgressBar progressbar = view.findViewById(R.id.progressbar);
        builder.setView(view);
        tvError.setText("");
        progressbar.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = builder.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();
                String email = edtEmailReset.getText().toString().trim();
                progressbar.setVisibility(View.VISIBLE);

                if (email.isEmpty()) {
                    tvError.setText("Không được để trống !");
                    progressbar.setVisibility(View.INVISIBLE);
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvError.setText("Email không hợp lệ !");
                    progressbar.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    if (!checkConnected()) {
                        progressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SigninActivity.this, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SigninActivity.this, "Đã gửi liên kết đổi mật khẩu!", Toast.LENGTH_LONG).show();
                                } else {
                                    progressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SigninActivity.this, "Gửi liên kết không thành công!\nHãy thử lại.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
            }
        });


    }

    private void updateUI(FirebaseUser currentUser){

    }

}