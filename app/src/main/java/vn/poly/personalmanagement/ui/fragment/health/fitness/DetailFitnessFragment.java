package vn.poly.personalmanagement.ui.fragment.health.fitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.health.fitness.DetailexerciseAdapter;
import vn.poly.personalmanagement.database.dao.FitnessDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.DetailExercise;


public class DetailFitnessFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    final String keyName = "idFrag";
    TextView tvBack, tvDate, tvCountItem, tvDelete;
    ListView lvExercises;
    FitnessDAO fitnessDAO;
    MyDatabase mydatabase;
    List<DetailExercise> detailExercisesList;

    public DetailFitnessFragment() {
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

        if (getArguments().getInt(keyName) == FitnessFragment.ID_FRAG) {
            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new FitnessFragment()).commit();
            } else if (tvDelete.equals(v)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn muốn xóa ngày tập này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitnessDAO.deleteDataWithDate(getArguments().getString("date"));
                        Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_health_root, new FitnessFragment()).commit();
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
}