package vn.poly.personalmanagement.ui.fragment.health;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.EatingDAO;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.ui.fragment.health.eating.AddMealFragment;
import vn.poly.personalmanagement.ui.fragment.health.eating.MainEatingFragment;
import vn.poly.personalmanagement.ui.fragment.health.eating.MealsTodayFragment;
import vn.poly.personalmanagement.ui.fragment.health.fitness.ExercisesFragment;
import vn.poly.personalmanagement.ui.fragment.health.fitness.ExercisesTodayFragment;
import vn.poly.personalmanagement.ui.fragment.health.fitness.MainFitnessFragment;


public class HealthFragment extends Fragment implements Initialize, View.OnClickListener {
    public static final int ID_FRAG = 5;
    final String keyName = "idFrag";

    CardView cardToEating, cardEatingToday, cardAddMeal, cardTofitness,
            cardExercisesToday, cardExercisesList, cardMealList, cardFitnessList;
    TextView tvCountMeal, tvCountExerciseToday;
    MyDatabase mydatabase;
    EatingDAO eatingDAO;
    FitnessDAO fitnessDAO;

    public HealthFragment() {
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
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCountMeal.setText("" + eatingDAO.getAllMealWithDate(CurrentDateTime.getCurrentDate()).size());

        tvCountExerciseToday.setText("" + fitnessDAO.getAllExerciseWithDate(CurrentDateTime.getCurrentDate()).size());

        cardTofitness.setOnClickListener(this);
        cardToEating.setOnClickListener(this);
        cardEatingToday.setOnClickListener(this);
        cardAddMeal.setOnClickListener(this);
        cardExercisesToday.setOnClickListener(this);
        cardExercisesList.setOnClickListener(this);
        cardFitnessList.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (cardToEating.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainEatingFragment()).commit();
        } else if (cardTofitness.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
        } else if (cardAddMeal.equals(v)) {

            AddMealFragment addMealFragment = new AddMealFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, ID_FRAG);
            addMealFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, addMealFragment).commit();
        } else if (cardEatingToday.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MealsTodayFragment()).commit();
        } else if (cardExercisesToday.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new ExercisesTodayFragment()).commit();
        } else if (cardExercisesList.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new ExercisesFragment()).commit();
        } else if (cardFitnessList.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
        }

    }

    @Override
    public void initializeViews(View view) {
        cardToEating = view.findViewById(R.id.cardToEating);
        cardTofitness = view.findViewById(R.id.cardToFitness);
        cardEatingToday = view.findViewById(R.id.cardMealMenuToday);
        cardAddMeal = view.findViewById(R.id.cardAddMeal);
        cardExercisesToday = view.findViewById(R.id.cardExercisesToday);
        cardExercisesList = view.findViewById(R.id.cardExercisesList);
        cardFitnessList = view.findViewById(R.id.cardFitnessList);
        tvCountMeal = view.findViewById(R.id.tvCountMealsToday);
        tvCountExerciseToday = view.findViewById(R.id.tvCountExerciseToday);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        eatingDAO = new EatingDAO(mydatabase);
        fitnessDAO = new FitnessDAO(mydatabase);
    }
}