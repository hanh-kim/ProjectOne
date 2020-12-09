package vn.poly.personalmanagement.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.model.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail, edtPassword;
    TextView tvToSignin;
    Button btnSignup;
    ProgressDialog progressDialog;
    MyDatabase myDatabase;
    AccountDAO accountDAO;



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
    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view!=null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

    private void signup() {

        // to do here
        String email = edtEmail.getText().toString().trim();
        String passowrd = edtPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Sai định dạng Email! VD: example@gmail.com");
            edtEmail.setFocusable(true);
            return;
        }else if (accountDAO.checkExist(email)) {
            edtEmail.setError("Email đã tồn tại");
            edtEmail.setFocusable(true);
            return;
        } else if (passowrd.length() < 6) {
            edtPassword.setError("Mời nhập mật khẩu dài ít nhất 6 kí tự!");
            edtPassword.setFocusable(true);
            return;
        }  else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passowrd);
            save(user);
        }


    }

    private void loadingProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_progress_dialog,null);
        builder.setView(view);
        TextView  tvProgress = view.findViewById(R.id.tvProgress);
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
        final ImageView icDone = view1.findViewById(R.id.icDone);
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