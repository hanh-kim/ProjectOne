package vn.poly.personalmanagement.ui.fragment.security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressBar progressBar;


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

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_security_root, new SecurityFragment()).commit();
            }
        });



        mainActivity = (MainActivity) getActivity();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
              //  savePass();
                startResetPass();
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
        progressBar = view.findViewById(R.id.progress_bar);


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

        String userId = currentUser.getUid();


        tvError.setTextColor(Color.parseColor("#019807"));
        tvError.setText("Đổi mật khẩu thành công!");

        accountDAO.changePassword(email, newPass);
        Toast.makeText(getActivity(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_security_root, new SecurityFragment())
                .commit();
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    private void startResetPass() {
        if (!checkConnected()) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
            return;
        }
       String uid = currentUser.getUid();
       databaseReference.child(uid).child("InfoUser").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
               if (user != null){
                  resetPassword(user);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private void resetPassword(final User user){

        progressBar.setVisibility(View.VISIBLE);
        if (!checkConnected()) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
            return;
        }

        tvError.setText("");
        //  tvError.setTextColor(R.color.colorRed);


        String oldPass = edtOldPassword.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();


        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            tvError.setText("Các ô không được để trông!");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if (!oldPass.equals(user.getPassword())) {
            progressBar.setVisibility(View.INVISIBLE);
            edtOldPassword.setError("Mời nhập lại");
            tvError.setText("Mật khẩu không chính xác!");
            return;
        }

        if (newPass.length() < 6) {
            progressBar.setVisibility(View.INVISIBLE);
            edtNewPassword.setError("Mời nhập lại");
            tvError.setText("Mật khẩu mới gồm ít nhất 6 kí tự!");
            return;
        }
        if (!confirmPass.equals(newPass)) {
            progressBar.setVisibility(View.INVISIBLE);
            edtConfirmPassword.setError("Mời nhập lại");
            tvError.setText("Mật khẩu nhập lại không khớp!");
            return;
        }
        hideSoftKeyboard();
        user.setPassword(newPass);
        currentUser.updatePassword(user.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            //setValue to firebase database
                            databaseReference.child(currentUser.getUid()).child("InfoUser").setValue(user)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                tvError.setTextColor(Color.parseColor("#019807"));
                                                tvError.setText("Đổi mật khẩu thành công!");
                                                //  accountDAO.changePassword(email, newPass);
                                                Toast.makeText(getActivity(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.fragment_security_root, new SecurityFragment())
                                                        .commit();

                                            }else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getActivity(), "Đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }

    private boolean checkConnected() {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        } else return false;
    }
}