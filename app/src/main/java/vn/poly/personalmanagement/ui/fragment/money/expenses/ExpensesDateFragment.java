package vn.poly.personalmanagement.ui.fragment.money.expenses;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.dao.ExpensesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.Initialize;


public class ExpensesDateFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String NAME_FRAG = ExpensesDateFragment.class.getName();
    final String keyName = "fragName";

    TextView tvBack, tvEdit, tvDate, tvDelete, tvCountItem;
    ImageView icAdd;
    int isEditing = 0;
    MyDatabase mydatabase;
    ExpensesDAO expensesDAO;
    ListView lvExpenses;
    Bundle bundle;
    public ExpensesDateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_expenses_date, container, false);
      initializeDatabase();
      initializeViews(view);
        tvEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        lvExpenses.setOnItemClickListener(this);
        bundle = getArguments();
        if (bundle != null) {
            tvDate.setText(bundle.getString("date"));
        }

        return view;
    }



    @Override
    public void initializeViews(View view) {

        tvDelete = view.findViewById(R.id.tvDelete);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvDate = view.findViewById(R.id.tvChooseDate);
        lvExpenses = view.findViewById(R.id.lvExpenses);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        icAdd = view.findViewById(R.id.icAdd);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getContext());
        expensesDAO = new ExpensesDAO(mydatabase);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void chooseDate() {
        String date = "";
        /* format date: 01/01/2020 */
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void edit() {
        if (isEditing == 0) {
            tvEdit.setText("Xong");
            tvDate.setEnabled(true);
            isEditing = 1;
        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            tvDate.setEnabled(false);
            isEditing = 0;
       //     update();

        }

    }
}