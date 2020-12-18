package vn.poly.personalmanagement.ui.fragment.health.fitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.fitness.DetailexerciseAdapter;
import vn.poly.personalmanagement.database.dao.ExerciseDAO;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Exercise;


public class ExercisesTodayFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int ID_FRAG = 2;
    final String keyName = "idFrag";
    TextView tvBack, tvCurrentDate, tvCountItem;
    EditText edtSearch;
    ListView lvExercises;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    MyDatabase mydatabase;
    ExerciseDAO exerciseDAO;
    FitnessDAO fitnessDAO;
    List<DetailExercise> detailExercisesList;

    public ExercisesTodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_exercises_today, container, false);
        initializeDatabase();
        initializeViews(view);
        tvCurrentDate.setText("Hôm nay, " + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        lvExercises.setOnItemClickListener(this);
        icAdd.setOnClickListener(this);
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        countItem();
        showExercised();
        return view;
    }


    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
        } else if (icAdd.equals(v)) {
            addExercise();

        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          DetailExercise detailExercise = detailExercisesList.get(position);
          updateExercise(detailExercise);
    }

    @Override
    public void initializeViews(View view) {
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvExercises = view.findViewById(R.id.lvExercises);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        icAdd = view.findViewById(R.id.icAdd);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        exerciseDAO = new ExerciseDAO(mydatabase);
        fitnessDAO = new FitnessDAO(mydatabase);
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setEnabled(false);
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setEnabled(true);
    }

    private void addExercise() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_add_exercise, null);
        builder.setView(view);
        List<Exercise> exerciseList = exerciseDAO.getAllData();
        List<String> stringList = new ArrayList<>();
        stringList.add("Mời chọn bài tập");
        for (Exercise exercise : exerciseList) {
            stringList.add(exercise.getExerciseName());
        }
        final Spinner spinner = view.findViewById(R.id.spnSelectExercise);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, stringList);
        spinner.setAdapter(adapter);

        final EditText edtDesciption = view.findViewById(R.id.edtAmountDoExercise);
        final TextView tvCancel = view.findViewById(R.id.tvCancel);
        final TextView tvSave = view.findViewById(R.id.tvSave);
        final TextView tvError = view.findViewById(R.id.tvError);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        tvError.setText("");

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edtDesciption.getText().toString().trim();
                String exercise = spinner.getSelectedItem().toString().trim();
                DetailExercise detailExercise = new DetailExercise();

                if (exercise.equals(spinner.getItemAtPosition(0).toString())) {
                    tvError.setText("Mời chọn bài tập!");
                    return;
                }
                if (description.isEmpty()) {
                    tvError.setText("Mời nhập số lượng, chi tiết bài tâp!");
                    return;
                }
                tvError.setText("");
                detailExercise.setDate(CurrentDateTime.getCurrentDate());
                detailExercise.setExercise(exercise);
                 detailExercise.setDescribe(description);
                fitnessDAO.addData(detailExercise);
                countItem();
                showExercised();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void updateExercise(final DetailExercise detailExercise) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_add_exercise, null);
        builder.setView(view);
        final EditText edtDesciption = view.findViewById(R.id.edtAmountDoExercise);
        final TextView tvCancel = view.findViewById(R.id.tvCancel);
        final TextView tvSave = view.findViewById(R.id.tvSave);
        final TextView tvError = view.findViewById(R.id.tvError);
        final Spinner spinner = view.findViewById(R.id.spnSelectExercise);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        tvError.setText("");

        List<Exercise> exerciseList = exerciseDAO.getAllData();
        List<String> stringList = new ArrayList<>();
        stringList.add("Mời chọn bài tập");
        for (Exercise exercise : exerciseList) {
            stringList.add(exercise.getExerciseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, stringList);
        spinner.setAdapter(adapter);

        //set selection for spinner
        for (int i = 0; i < stringList.size(); i++) {
            if (detailExercise.getExercise().equals(stringList.get(i).toString())){
                spinner.setSelection(i);
            }
        }

        // set detail exercise for EditText
        edtDesciption.setText(detailExercise.getDescribe());


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edtDesciption.getText().toString().trim();
                String exercise = spinner.getSelectedItem().toString().trim();
                if (exercise.equals(spinner.getItemAtPosition(0).toString())) {
                    tvError.setText("Mời chọn bài tập!");
                    return;
                }
                if (description.isEmpty()) {
                    tvError.setText("Mời nhập số lượng, chi tiết bài tâp!");
                    return;
                }
                tvError.setText("");
                detailExercise.setDate(CurrentDateTime.getCurrentDate());
                detailExercise.setExercise(exercise);
                detailExercise.setDescribe(description);
                fitnessDAO.updateData(detailExercise);
                countItem();
                showExercised();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Sửa thành công!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private List<DetailExercise> getDetailExercisesList() {
        return fitnessDAO.getAllExerciseWithDate(CurrentDateTime.getCurrentDate());
    }

    private void showExercised() {
        final DetailexerciseAdapter adapter = new DetailexerciseAdapter();
        detailExercisesList = getDetailExercisesList();
        adapter.setDataAdapter(detailExercisesList, new DetailexerciseAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final DetailExercise detailExercise, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn muốn xóa bài tập này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();

                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitnessDAO.deleteData(detailExercise);
                        detailExercisesList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                    }
                });
                builder.create().show();
            }
        });

        lvExercises.setAdapter(adapter);
    }

    private void countItem() {
        if (getDetailExercisesList().size() == 0) {
            tvCountItem.setText("Danh sách trống ");
        } else tvCountItem.setText("Số bài tập: " + getDetailExercisesList().size());
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}