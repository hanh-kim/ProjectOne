package vn.poly.personalmanagement.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.poly.personalmanagement.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail, edtPassword;
    TextView tvToSignin;
    Button btnSignup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignup = findViewById(R.id.btnSignup);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvToSignin = findViewById(R.id.tvToLogin);
        // views on click
        btnSignup.setOnClickListener(this);
        tvToSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btnSignup.equals(v)) {
            signup();
        } else if (tvToSignin.equals(v)) {
            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            SignupActivity.this.finish();
        }
    }

    private void signup() {

        // to do here
        String email = edtEmail.getText().toString().trim();
        String passowrd = edtPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Sai định dạng Email! VD: example@gmail.com");
            edtEmail.setFocusable(true);

        } else if (passowrd.length() < 6) {
            edtPassword.setError("Mời nhập mật khẩu dài ít nhất 6 kí tự!");
            edtPassword.setFocusable(true);

        } else {
            save();
        }


    }

    private void save() {
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.show();
        final View view1 = getLayoutInflater().inflate(R.layout.layout_progress_dialog, null);

        // Set content view
        progressDialog.setContentView(view1);
        final TextView tvProgress = view1.findViewById(R.id.tvProgress);
        final ProgressBar progressBar = view1.findViewById(R.id.progressBar);
        final ImageView icDone = view1.findViewById(R.id.icDone);
        // Set transparent background
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Insert to SQLite here

                progressBar.setVisibility(View.GONE);
                icDone.setVisibility(View.VISIBLE);
                tvProgress.setText("Đăng kí thành công");
                dismissProgress();
            }
        }, 2000);

    }

    private void dismissProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Đăng kí thành công!", Toast.LENGTH_LONG).show();
//                 startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                SignupActivity.this.finish();
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
    }


}