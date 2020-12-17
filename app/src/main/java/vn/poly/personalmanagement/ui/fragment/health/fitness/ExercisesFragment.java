package vn.poly.personalmanagement.ui.fragment.health.fitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExerciseDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Exercise;

import java.util.ArrayList;
import java.util.List;


public class ExercisesFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {


    final String keyName = "idFrag";
    TextView tvBack, tvCurrentDate, tvCountItem;
    EditText edtSearch;
    ListView lvExercises;
    ImageView icAdd;
    List<Exercise> exerciseList;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    MyDatabase mydatabase;
    ExerciseDAO exerciseDAO;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        initializeDatabase();
        initializeViews(view);
        //tvCurrentDate.setText("Hôm nay, "+ CurrentDateTime.getCurrentDate());
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        lvExercises.setOnItemClickListener(this);
        icAdd.setOnClickListener(this);
        countItem();
        showExercises();

        return view;

    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
        } else if (icAdd.equals(v)) {
            addExercise();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn muốn sửa hay xóa");
        builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Exercise exercise = exerciseList.get(position);
                exerciseDAO.deleteData(exercise);
                showExercises();
                countItem();
                Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                updateExercise(position);
            }
        });
        builder.create().show();
    }

    @Override
    public void initializeViews(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvExercises = view.findViewById(R.id.lvExercises);
        //  tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        icAdd = view.findViewById(R.id.icAdd);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        exerciseDAO = new ExerciseDAO(mydatabase);
    }

    private void updateExercise(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_update_exercise, null);
        builder.setView(view);
        final Exercise exercise = exerciseList.get(position);
        final EditText edtExerciseName = view.findViewById(R.id.edtNewExerciseName);
        final TextView tvCancel = view.findViewById(R.id.tvCancel);
        final TextView tvSave = view.findViewById(R.id.tvSave);
        final TextView tvError = view.findViewById(R.id.tvError);
         builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        tvError.setText("");
        edtExerciseName.setText(exercise.getExerciseName());

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtExerciseName.getText().toString().trim();
                if (name.isEmpty()){
                    tvError.setText("Tên bài tập trống!");
                    return;
                }
                exercise.setExerciseName(name);
                exerciseDAO.updateData(exercise);
                showExercises();
                countItem();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addExercise() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_add_new_exercise, null);
        builder.setView(view);
        builder.setCancelable(false);
        final EditText edtAddNewExerciseName = view.findViewById(R.id.edtAddNewExercise);
        final TextView tvCancel = view.findViewById(R.id.tvCancel);
        final TextView tvSave = view.findViewById(R.id.tvSave);
        final TextView tvError = view.findViewById(R.id.tvError);

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

                String name = edtAddNewExerciseName.getText().toString().trim();
                if (name.isEmpty()){
                    tvError.setText("Tên bài tập trống!");
                    return;
                }
                tvError.setText("");
                Exercise exercise = new Exercise();
                exercise.setExerciseName(name);
                exerciseDAO.addData(exercise);
                showExercises();
                countItem();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showExercises() {
        exerciseList = exerciseDAO.getAll();
        List<String> stringList = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            stringList.add(exercise.getExerciseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringList);
        lvExercises.setAdapter(adapter);
    }

    private void countItem() {
        if (exerciseDAO.getAll().size() == 0) {
            tvCountItem.setText("Danh sách trống ");
        } else tvCountItem.setText("Số bài tập: " + exerciseDAO.getAll().size());
    }

    //    private void cancelSearch() {
//        layoutSearch.setVisibility(View.GONE);
//        edtSearch.setEnabled(false);
//    }
//
//    private void startSearch() {
//        layoutSearch.setVisibility(View.VISIBLE);
//        edtSearch.setEnabled(true);
//    }

}