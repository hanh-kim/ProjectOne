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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
     //   updateUI(currentUser);
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        String userId = currentUser.getUid();
        clearDatabase();
        super.onDestroy();

    }

    private void clearDatabase() {
        plansDAO.clearAllData();
        notesDAO.clearAllData();
        incomesDAO.clearAllData();
        expensesDAO.clearAllData();
        mealsDAO.clearAllData();
        fitnessDAO.clearAllData();
        exerciseDAO.clearAllData();

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
        databaseReference.child(uid).child("Plans")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Plan plan = snapshot.getValue(Plan.class);
                        plansDAO.addData(plan);
                        Log.d("Plan:", "saved");
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
        databaseReference.child(uid).child("Notes")
                .addChildEventListener(new ChildEventListener() {
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