package vn.poly.personalmanagement.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
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

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.model.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail, edtPassword;
    TextView tvToSignin, tvSignup;
    ProgressBar progressBar;
    ImageView icSuccessful;
    RelativeLayout btnSignup;
    MyDatabase myDatabase;
    AccountDAO accountDAO;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignup = findViewById(R.id.btnSignup);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvToSignin = findViewById(R.id.tvToLogin);
        myDatabase = new MyDatabase(SignupActivity.this);
        accountDAO = new AccountDAO(myDatabase);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        tvSignup = findViewById(R.id.tvSignup);
        icSuccessful = findViewById(R.id.icSuccessful);


        // views on click
        btnSignup.setOnClickListener(this);
        tvToSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (btnSignup.equals(v)) {
            hideSoftKeyboard();
            signup();
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

    private void signup() {

        // to do here
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        tvSignup.setText("ĐĂNG KÝ...");
        tvSignup.setTextColor(Color.WHITE);
        progressBar.setVisibility(View.VISIBLE);
        icSuccessful.setVisibility(View.INVISIBLE);
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Sai định dạng Email! VD: example@gmail.com");
            edtEmail.setFocusable(true);
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);

            return;
        }else if (password.length() < 6) {
            edtPassword.setError("Mời nhập mật khẩu dài ít nhất 6 kí tự!");
            edtPassword.setFocusable(true);
            tvSignup.setText("ĐĂNG KÝ");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }  else if (accountDAO.checkExist(email)) {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvSignup.setText("ĐĂNG KÝ");
                    progressBar.setVisibility(View.INVISIBLE);
                    edtEmail.setError("Email đã tồn tại");
                    edtEmail.setFocusable(true);
                }
            }, 2000);
            return;
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

             accountDAO.addData(user);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvSignup.setTextColor(Color.parseColor("#00EF3F"));
                    tvSignup.setText("ĐĂNG KÝ THÀNH CÔNG");
                    progressBar.setVisibility(View.INVISIBLE);
                    icSuccessful.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SignupActivity.this,SigninActivity.class));
                            SignupActivity.this.finish();
                        }
                    }, 2000);

                }
            }, 3000);




//            tvSignup.setText("ĐĂNG KÝ...");
//            progressBar.setVisibility(View.VISIBLE);
//            mAthu.createUserWithEmailAndPassword(email,password)
//                    .addOnCompleteListener(SignupActivity.this,new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//                                tvSignup.setTextColor(Color.parseColor("#00EF3F"));
//                                tvSignup.setText("ĐĂNG KÝ THÀNH CÔNG");
//                                progressBar.setVisibility(View.INVISIBLE);
//                                icSuccessful.setVisibility(View.VISIBLE);
//                                Toast.makeText(SignupActivity.this,"Đăng kí thành công",Toast.LENGTH_LONG).show();
//                            }
//                            else {
//                                tvSignup.setText("ĐĂNG KÝ");
//                                tvSignup.setTextColor(Color.WHITE);
//                                Toast.makeText(SignupActivity.this,"Đăng kí thất bại",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
        }

    }

    private void loadingProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_progress_dialog, null);
        builder.setView(view);
        TextView tvProgress = view.findViewById(R.id.tvProgress);
        tvProgress.setText("Đăng nhập...");
        // Set transparent background
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        builder.create().show();


    }

    private void save(User user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        final View view1 = getLayoutInflater().inflate(R.layout.layout_progress_dialog, null);
        // Set content view
        builder.setView(view1);
        builder.show();
        final TextView tvProgress = view1.findViewById(R.id.tvProgress);
        final ProgressBar progressBar = view1.findViewById(R.id.progressBar);
        final ImageView icDone = view1.findViewById(R.id.icSuccessful);
        // Set transparent background

        //  accountDAO.addData(user);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Insert to SQLite here

                progressBar.setVisibility(View.GONE);
                icDone.setVisibility(View.VISIBLE);
                tvProgress.setText("Đăng kí thành công");
                dismissProgress();
                builder.create().cancel();
            }
        }, 2000);
        SignupActivity.this.finish();

    }

    private void dismissProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                Toast.makeText(getApplicationContext(), "Đăng kí thành công!", Toast.LENGTH_LONG).show();
//                 startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                SignupActivity.this.finish();
            }
        }, 500);
    }


}

