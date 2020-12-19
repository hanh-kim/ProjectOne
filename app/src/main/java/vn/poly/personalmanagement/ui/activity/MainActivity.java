package vn.poly.personalmanagement.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExerciseDAO;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.dao.MealsDAO;
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

public class MainActivity extends AppCompatActivity {
    private String keyEmail = "email";
    private String keyPassword = "password";
    private String keyCheck = "isRemember";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_plan_root, R.id.navigation_note_root, R.id.navigation_money_root,
                R.id.navigation_heath_root, R.id.navigation_security_root)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



    }

    private void initialize(){
        // init database
        myDatabase = new MyDatabase(MainActivity.this);
        plansDAO = new PlansDAO(myDatabase);
        notesDAO = new NotesDAO(myDatabase);
        incomesDAO = new IncomesDAO(myDatabase);
        expensesDAO = new ExpensesDAO(myDatabase);
        mealsDAO = new MealsDAO(myDatabase);
        fitnessDAO = new FitnessDAO(myDatabase);
        exerciseDAO = new ExerciseDAO(myDatabase);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getEmailSaved() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_email", MODE_PRIVATE);
        return sharedPreferences.getString(keyEmail, "");
    }

    private boolean checkConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        } else return false;
    }

    @Override
    protected void onPause() {
        String userId = currentUser.getUid();
        saveAllDataToFirebase(userId);
        super.onPause();
    }

    @Override
    protected void onResume() {
        String userId = currentUser.getUid();
        saveAllDataToFirebase(userId);
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        String userId = currentUser.getUid();
        saveAllDataToFirebase(userId);
        super.onDestroy();

    }

    private void saveAllDataToFirebase(String uid) {
        // init list
        planList = plansDAO.getAllData();
        noteList = notesDAO.getAllData();
        incomeList = incomesDAO.getAllIncomes();
        expenseList =expensesDAO.getAllExpenses();
        mealList = mealsDAO.getAllData();
        fitnessList = fitnessDAO.getAllData();
        exerciseList = exerciseDAO.getAllData();

        databaseReference.child(uid).child("Plans").setValue(planList);
        databaseReference.child(uid).child("Notes").setValue(noteList);
        databaseReference.child(uid).child("Incomes").setValue(incomeList);
        databaseReference.child(uid).child("Expenses").setValue(expenseList);
        databaseReference.child(uid).child("Meals").setValue(mealList);
        databaseReference.child(uid).child("Fitness").setValue(fitnessList);
        databaseReference.child(uid).child("Exercises").setValue(exerciseList);
    }


}