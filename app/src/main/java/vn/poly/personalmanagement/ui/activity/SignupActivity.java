package vn.poly.personalmanagement.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.model.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail, edtPassword, edtConfirmPassword;
    TextView tvToSignin, tvSignup, tvError;
    ProgressBar progressBar;
    ImageView icSuccessful;
    RelativeLayout btnSignup;
    MyDatabase myDatabase;
    AccountDAO accountDAO;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //initializeViews
        btnSignup = findViewById(R.id.btnSignup);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tvToSignin = findViewById(R.id.tvToLogin);
        myDatabase = new MyDatabase(SignupActivity.this);
        accountDAO = new AccountDAO(myDatabase);
        progressBar = findViewById(R.id.progressBar);
        tvSignup = findViewById(R.id.tvSignup);
        icSuccessful = findViewById(R.id.icSuccessful);

        // initialize firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        // views on click
        btnSignup.setOnClickListener(this);
        tvToSignin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (btnSignup.equals(v)) {
            hideSoftKeyboard();
            signupWithFirebaseAuth();
        } else if (tvToSignin.equals(v)) {
            hideSoftKeyboard();
            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            SignupActivity.this.finish();
        }
    }


    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void signupWithFirebaseAuth() {
        // to do here
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        tvSignup.setText("ĐĂNG KÝ...");
        tvSignup.setTextColor(Color.WHITE);
        progressBar.setVisibility(View.VISIBLE);
        icSuccessful.setVisibility(View.INVISIBLE);
        tvError.setText("");

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            tvError.setText("Thông tin đăng ký không được để trống!");
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ!\nVD: example@gmail.com");
            edtEmail.setFocusable(true);
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);

        } else if (password.length() < 6) {
            edtPassword.setError("Mời nhập mật khẩu dài ít nhất 6 kí tự!");
            edtPassword.setFocusable(true);
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);

        } else if (!confirmPassword.equals(password)) {
            edtConfirmPassword.setError("Mật khẩu nhập lại không khớp!");
            edtConfirmPassword.setFocusable(true);
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);

        } else {
            final User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            if (!checkConnected()) {
                tvSignup.setText("ĐĂNG KÝ");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignupActivity.this, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                return;
            }else {

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String uerId = firebaseAuth.getCurrentUser().getUid();
                                    databaseReference.child(uerId).child("InfoUser").setValue(user)
                                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){
                                                        tvSignup.setTextColor(Color.parseColor("#00EF3F"));
                                                        tvSignup.setText("ĐĂNG KÝ THÀNH CÔNG");
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        icSuccessful.setVisibility(View.VISIBLE);
                                                        Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(900);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                                                                SignupActivity.this.finish();
                                                            }
                                                        }).start();

                                                    }else {
                                                        tvSignup.setText("ĐĂNG KÝ");
                                                        tvSignup.setTextColor(Color.WHITE);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        icSuccessful.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(SignupActivity.this, "Đăng kí thất bại!\nHãy thử lại với Email khác.", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });


                                } else {
                                    tvSignup.setText("ĐĂNG KÝ");
                                    tvSignup.setTextColor(Color.WHITE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    icSuccessful.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignupActivity.this, "Đăng kí thất bại!\nHãy thử lại với Email khác.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }

//else if (accountDAO.checkExist(email)) {
//                Handler handler1 = new Handler();
//                handler1.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvSignup.setText("ĐĂNG KÝ");
//                        progressBar.setVisibility(View.INVISIBLE);
//                        edtEmail.setError("Email đã tồn tại");
//                        edtEmail.setFocusable(true);
//                    }
//                }, 2000);
//                return;
//            }
//            accountDAO.addData(user);
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    tvSignup.setTextColor(Color.parseColor("#00EF3F"));
//                    tvSignup.setText("ĐĂNG KÝ THÀNH CÔNG");
//                    progressBar.setVisibility(View.INVISIBLE);
//                    icSuccessful.setVisibility(View.VISIBLE);
//                    Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();
//                    Handler handler1 = new Handler();
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
//                            SignupActivity.this.finish();
//                        }
//                    }, 2000);
//
//                }
//            }, 3000);
        }

    }

    private boolean checkConnected() {
        ConnectivityManager manager = (ConnectivityManager) SignupActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        } else return false;
    }


}

