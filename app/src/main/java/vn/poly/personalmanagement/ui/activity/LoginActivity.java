package vn.poly.personalmanagement.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.Initialize;

public class LoginActivity extends AppCompatActivity implements Initialize {

    EditText edtPIN;
    TextView tvNext, tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        edtPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String PIN = edtPIN.getText().toString().trim();
                if (PIN.length() == 6) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String PIN = edtPIN.getText().toString().trim();
                if (PIN.length() == 6) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });


    }


    public void initializeViews() {
        edtPIN = findViewById(R.id.edt_PIN);
        tvNext = findViewById(R.id.tv_next);
        tvForgot = findViewById(R.id.tv_forgotPIN);
    }

    @Override
    public void initializeViews(View view) {

    }

    @Override
    public void initializeDatabase() {

    }
}