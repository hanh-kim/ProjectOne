package vn.poly.personalmanagement.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.AccountDAO;
import vn.poly.personalmanagement.database.dao.MealsDAO;
import vn.poly.personalmanagement.database.dao.ExerciseDAO;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Exercise;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.Meal;
import vn.poly.personalmanagement.model.Note;
import vn.poly.personalmanagement.model.Plan;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword, tvToSignup, tvError;
    RelativeLayout btnSignin;

    CheckBox cboRememberAcc;
    private String keyEmail = "email";
    private String keyPassword = "password";
    private String keyCheck = "isRemember";

    ProgressBar progressBar;
    MyDatabase myDatabase;
    AccountDAO accountDAO;
    TextView tvSignin;
    ImageView icSuccessful;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initialize();
        restoreInfoAccountRemembered();
        // views on click
        btnSignin.setOnClickListener(this);
        tvToSignup.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        if (currentUser != null) {
            if (!checkConnected()) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SigninActivity.this, "Không có kết nối internet !", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            SigninActivity.this.finish();

        }

    }

    private void initialize() {
        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        // init views
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
        // init database
        myDatabase = new MyDatabase(SigninActivity.this);
        accountDAO = new AccountDAO(myDatabase);
        plansDAO = new PlansDAO(myDatabase);
        notesDAO = new NotesDAO(myDatabase);
        incomesDAO = new IncomesDAO(myDatabase);
        expensesDAO = new ExpensesDAO(myDatabase);
        mealsDAO = new MealsDAO(myDatabase);
        fitnessDAO = new FitnessDAO(myDatabase);
        exerciseDAO = new ExerciseDAO(myDatabase);
        // init list


    }

    @Override
    public void onClick(View v) {
        if (btnSignin.equals(v)) {
            hideSoftKeyboard();
            signinWithfirebaseAuth(); // sign in with firebase

        } else if (tvToSignup.equals(v)) {
            startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            this.finish();
        } else if (tvForgotPassword.equals(v)) {
            forgotPassword();
        }
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

    // get user's data on firebase

    private void clearDatabase() {
        plansDAO.clearAllData();
        notesDAO.clearAllData();
        incomesDAO.clearAllData();
        expensesDAO.clearAllData();
        mealsDAO.clearAllData();
        fitnessDAO.clearAllData();
        exerciseDAO.clearAllData();

    }

    private void updateUI(FirebaseUser currentUser) {
        String uid = currentUser.getUid();
        clearDatabase();
        restorePlans(uid);
        restoreNotes(uid);
        restoreIncomes(uid);
        restoreExpenses(uid);
        restoreMeals(uid);
        restoreFitness(uid);
        restoreExercises(uid);

    }

    private void restorePlans(String uid) {
       List<Plan>   planList1 = new ArrayList<>();
        databaseReference.child(uid).child("Plans")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Plan plan = snapshot.getValue(Plan.class);
                      //  planList.add(plan);
                        plansDAO.addData(plan);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void restoreNotes(String uid) {
        databaseReference.child(uid).child("Notes").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Note note = snapshot.getValue(Note.class);
                        notesDAO.addData(note);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void restoreIncomes(String uid) {
        databaseReference.child(uid).child("Incomes")
               .addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                       Income income = snapshot.getValue(Income.class);
                       incomesDAO.addData(income);
                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


    }

    private void restoreExpenses(String uid) {

        databaseReference.child(uid).child("Expenses")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Expense expense = snapshot.getValue(Expense.class);
                        expensesDAO.addData(expense);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void restoreMeals(String uid) {
        databaseReference.child(uid).child("Meals")
               .addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                       Meal meal = snapshot.getValue(Meal.class);
                       mealsDAO.addData(meal);
                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

    }

    private void restoreFitness(String uid) {

        databaseReference.child(uid).child("Fitness")
               .addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                       DetailExercise exercise = snapshot.getValue(DetailExercise.class);
                       fitnessDAO.addData(exercise);
                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


    }

    private void restoreExercises(String uid) {
        databaseReference.child(uid).child("Exercises")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Exercise exercise = snapshot.getValue(Exercise.class);
                        exerciseDAO.addData(exercise);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}


