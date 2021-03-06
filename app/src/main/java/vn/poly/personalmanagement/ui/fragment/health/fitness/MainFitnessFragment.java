package vn.poly.personalmanagement.ui.fragment.health.fitness;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.fitness.FitnessAdapter;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Fitness;
import vn.poly.personalmanagement.ui.fragment.health.HealthFragment;


public class MainFitnessFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int ID_FRAG = 1;
    final String keyName = "idFrag";
    TextView tvBack, tvCurrentDate, tvCountItem, tvCountExerciseToday;
    CardView cardToday, cardExercisesList;
    ListView lvFitness;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    FitnessDAO fitnessDAO;
    MyDatabase mydatabase;
    List<Fitness> fitnessList;

    public MainFitnessFragment() {
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
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        initializeDatabase();
        initializeViews(view);

        //  lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        lvFitness.setOnItemClickListener(this);
        lvResultSearch.setOnItemClickListener(this);
        cardToday.setOnClickListener(this);
        cardExercisesList.setOnClickListener(this);
        countExercisedToday();
        countItem();
        showFitness();
        return view;
    }


    @Override
    public void initializeViews(View view) {
        cardToday = view.findViewById(R.id.cardExercisesToday);
        cardExercisesList = view.findViewById(R.id.cardExercisesList);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvFitness = view.findViewById(R.id.lvFitness);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvCountExerciseToday = view.findViewById(R.id.tvCountExerciseToday);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        fitnessDAO = new FitnessDAO(mydatabase);
    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new HealthFragment()).commit();
        } else if (cardToday.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new ExercisesTodayFragment()).commit();
        } else if (cardExercisesList.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new ExercisesFragment()).commit();
        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fitness fitness =  fitnessList.get(position);
        if (fitness.getDate().equals(CurrentDateTime.getCurrentDate())){
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new ExercisesTodayFragment()).commit();
        }else {
            ExerciseDateFragment exerciseDateFragment = new ExerciseDateFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, ID_FRAG);
            bundle.putString("date",fitness.getDate());
            exerciseDateFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, exerciseDateFragment).commit();
        }

    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setEnabled(false);
    }

    private List<Fitness> getFitnessList() {
        return fitnessDAO.getFitnessList();
    }

    private List<Fitness> getFitnessList(String date) {
        return fitnessDAO.getFitnessList(date);
    }

    private void showFitness() {
        final FitnessAdapter fitnessAdapter = new FitnessAdapter();
        fitnessList = getFitnessList();
        fitnessAdapter.setDataAdapter(fitnessList, new FitnessAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Fitness fitness, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa ngày tập luyện này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitnessDAO.deleteDataWithDate(fitness.getDate());
                        fitnessList.remove(position);
                        fitnessAdapter.notifyDataSetChanged();
                        countItem();
                        countExercisedToday();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });

        lvFitness.setAdapter(fitnessAdapter);

    }

    private void countItem() {
        if (getFitnessList().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Số ngày tập luyện: " + getFitnessList().size());

    }

    private void countExercisedToday() {
        tvCountExerciseToday.setText("" + fitnessDAO.getAllExerciseWithDate(CurrentDateTime.getCurrentDate()).size());
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void startSearch() {
        edtSearch.setHint("Nhập ngày dd/mm/yyyy");
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setEnabled(true);
        edtSearch.setText("");
        showResultSearch("");
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sDate = edtSearch.getText().toString().trim();
                showResultSearch(sDate);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void showResultSearch(String date) {
        final FitnessAdapter fitnessAdapter = new FitnessAdapter();
        fitnessList = getFitnessList(date);
        fitnessAdapter.setDataAdapter(fitnessList, new FitnessAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Fitness fitness, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa ngày tập này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitnessDAO.deleteDataWithDate(fitness.getDate());
                        fitnessList.remove(position);
                        fitnessAdapter.notifyDataSetChanged();
                        countItem();
                        countExercisedToday();
                        showFitness();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });
        lvResultSearch.setAdapter(fitnessAdapter);
    }

}