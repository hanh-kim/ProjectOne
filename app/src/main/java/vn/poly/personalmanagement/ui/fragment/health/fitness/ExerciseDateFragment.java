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


public class ExerciseDateFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    final String keyName = "idFrag";
    TextView tvBack, tvDate, tvCountItem, tvDelete;
    ListView lvExercises;
    FitnessDAO fitnessDAO;
    ExerciseDAO exerciseDAO;
    MyDatabase mydatabase;
    List<DetailExercise> detailExercisesList;

    public ExerciseDateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_fitness, container, false);
        initializeDatabase();
        initializeViews(view);
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        lvExercises.setOnItemClickListener(this);
        Bundle bundle = getArguments();
        if (bundle!=null){
            String date = bundle.getString("date").trim();
            tvDate.setText("Ngày "+date);
            countItem();
            showExercised();
        }


        return view;
    }

    @Override
    public void onClick(View v) {

        if (getArguments().getInt(keyName) == MainFitnessFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
            } else if (tvDelete.equals(v)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn muốn xóa ngày tập này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitnessDAO.deleteDataWithDate(getArguments().getString("date"));
                        Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new MainFitnessFragment()).commit();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                builder.show();

            }
        } else if (getArguments().getInt(keyName) == ExercisesTodayFragment.ID_FRAG) {

            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new ExercisesTodayFragment()).commit();
            } else if (tvDelete.equals(v)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn muốn xóa hay sửa?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new ExercisesTodayFragment()).commit();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                builder.show();
            }
        }


    }

    @Override
    public void initializeViews(View view) {
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvExercises = view.findViewById(R.id.lvExercises);
        tvDate = view.findViewById(R.id.tvDateFitness);
        tvDelete = view.findViewById(R.id.tvDelete);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        fitnessDAO = new FitnessDAO(mydatabase);
        exerciseDAO = new ExerciseDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailExercise detailExercise = detailExercisesList.get(position);
        updateExercise(detailExercise);
    }

    private List<DetailExercise> getDetailExercisesList() {
        String date = getArguments().getString("date");
        return fitnessDAO.getAllExerciseWithDate(date);
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

        List<Exercise> exerciseList = exerciseDAO.getAll();
        List<String> stringList = new ArrayList<>();
        stringList.add("Mời chọn bài tập");
        for (Exercise exercise : exerciseList) {
            stringList.add(exercise.getExerciseName());
        }

        //set spinner
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

}