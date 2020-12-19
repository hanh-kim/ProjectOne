package vn.poly.personalmanagement.ui.fragment.security;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExerciseDAO;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.dao.MealsDAO;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Exercise;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.Meal;
import vn.poly.personalmanagement.model.Note;
import vn.poly.personalmanagement.model.Plan;
import vn.poly.personalmanagement.model.User;
import vn.poly.personalmanagement.ui.activity.MainActivity;
import vn.poly.personalmanagement.ui.activity.SigninActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class SecurityFragment extends Fragment
        implements Initialize, View.OnClickListener {


    TextView tvLogout, tvResetEmail;
    TextInputLayout edtLEmail, edtLPassword, edtlUsername;
    MainActivity mainActivity;
    TextView tvChangePassword;
    ProgressBar progressBar;

    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    MyDatabase myDatabase;
    PlansDAO plansDAO;
    NotesDAO notesDAO;
    IncomesDAO incomesDAO;
    ExpensesDAO expensesDAO;
    MealsDAO mealsDAO;
    FitnessDAO fitnessDAO;
    ExerciseDAO exerciseDAO;

    List<Plan> planList;
    List<Note> noteList;
    List<Income> incomeList;
    List<Expense> expenseList;
    List<Meal> mealList;
    List<DetailExercise> fitnessList;
    List<Exercise> exerciseList;

    public SecurityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        initializeDatabase();
        initializeViews(view);
        mainActivity = (MainActivity) getActivity();
        tvResetEmail.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (!checkConnected()) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
        }

        showCurrentUser();


        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvLogout.equals(v)) {
            uSignOut();
        } else if (tvChangePassword.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_security_root, new ChangePasswordFragment()).commit();
            //    changePassword();
        } else if (tvResetEmail.equals(v)) {
            resetEmail();
        }
    }

    @Override
    public void initializeViews(View view) {
        tvLogout = view.findViewById(R.id.tvLogout);
        progressBar = view.findViewById(R.id.progress_bar);
        tvResetEmail = view.findViewById(R.id.tvResetEmail);
        edtLPassword = view.findViewById(R.id.edtlPassword);
        edtLEmail = view.findViewById(R.id.edtlEmail);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
//        edtlUsername=view.findViewById(R.id.edtlUsername);

        // init list
        planList = plansDAO.getAllData();
        noteList = notesDAO.getAllData();
        incomeList = incomesDAO.getAllIncomes();
        expenseList =expensesDAO.getAllExpenses();
        mealList = mealsDAO.getAllData();
        fitnessList = fitnessDAO.getAllData();
        exerciseList = exerciseDAO.getAllData();
    }

    @Override
    public void initializeDatabase() {
        myDatabase = new MyDatabase(getActivity());
        // init database
        plansDAO = new PlansDAO(myDatabase);
        notesDAO = new NotesDAO(myDatabase);
        incomesDAO = new IncomesDAO(myDatabase);
        expensesDAO = new ExpensesDAO(myDatabase);
        mealsDAO = new MealsDAO(myDatabase);
        fitnessDAO = new FitnessDAO(myDatabase);
        exerciseDAO = new ExerciseDAO(myDatabase);

    }

    private void hideSoftKeyboard() {
        View view = mainActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private boolean checkConnected() {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        } else return false;
    }

    private void resetEmail() {
        final String uid = currentUser.getUid();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        View view = getLayoutInflater().inflate(R.layout.layout_email_reset, null);

        builder.setCancelable(false);
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

                if (!checkConnected()) {
                    progressbar.setVisibility(View.VISIBLE);
                    Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                    return;
                }

                final String email = edtEmailReset.getText().toString().trim();
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

                    currentUser.updateEmail(email)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseReference.child(uid).child("InfoUser")
                                                .child("email").setValue(email)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            tvError.setBackgroundColor(Color.GREEN);
                                                            tvError.setText("Thay đổi email thành công!");
                                                            Toast.makeText(getActivity(), "Thay đổi email thành công!", Toast.LENGTH_LONG).show();
                                                            progressbar.setVisibility(View.INVISIBLE);
                                                            showCurrentUser();
                                                            dialog.dismiss();
                                                        } else {
                                                            tvError.setText("");
                                                            Toast.makeText(getActivity(), "Thay đổi email không thành công!", Toast.LENGTH_LONG).show();
                                                            progressbar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });

                                    } else {
                                        tvError.setText("");
                                        Toast.makeText(getActivity(), "Thay đổi email không thành công!", Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                }
            }
        });


    }

    private void showCurrentUser() {
        if (!checkConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
            return;
        }

        String cuEmail = currentUser.getEmail();
        edtLEmail.getEditText().setText(cuEmail);
        String uid = currentUser.getUid();


        databaseReference.child(uid).child("InfoUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User mUser = snapshot.getValue(User.class);
                if (mUser != null) {
                    String uEmail = mUser.getEmail();
                    String uPassword = mUser.getPassword();
                    edtLEmail.getEditText().setText(uEmail);
                    edtLPassword.getEditText().setText(uPassword);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Không lấy được thông tin người dùng!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void uSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage("Bạn muốn đăng xuất?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checkConnected()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(mainActivity, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                saveAllDataToFirebase(currentUser.getUid());

                // current user sign out
                FirebaseAuth.getInstance().signOut();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                startActivity(new Intent(getActivity(), SigninActivity.class));
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveAllDataToFirebase(String uid) {

       databaseReference.child(uid).child("Plans").setValue(planList);
       databaseReference.child(uid).child("Notes").setValue(noteList);
       databaseReference.child(uid).child("Incomes").setValue(incomeList);
       databaseReference.child(uid).child("Expenses").setValue(expenseList);
       databaseReference.child(uid).child("Meals").setValue(mealList);
       databaseReference.child(uid).child("Fitness").setValue(fitnessList);
       databaseReference.child(uid).child("Exercises").setValue(exerciseList);
    }

}