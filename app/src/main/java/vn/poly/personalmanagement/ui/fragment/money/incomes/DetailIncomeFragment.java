package vn.poly.personalmanagement.ui.fragment.money.incomes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.methodclass.Initialize;


public class DetailIncomeFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    TextView tvBack, tvEdit, tvDateTime, tvChooseDate,tvDelete;
    EditText edtTitle, edtAmount, edtDescribe;
    int isEditing = 0;

    public DetailIncomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_income, container, false);
        initializeDatabase();
        initializeViews(view);

        tvEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new IncomeFragment()).commit();
        } else if (tvEdit.equals(v)) {
            edit();

        } else if (tvChooseDate.equals(v)) {
            chooseDate();
        }else if (tvDelete.equals(v)) {
            Toast.makeText(getActivity(),"Xóa thành công!",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new IncomeFragment()).commit();
        }
    }

    private void chooseDate() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void initializeViews(View view) {
        tvChooseDate = view.findViewById(R.id.tvChooseDate);
        tvBack = view.findViewById(R.id.tvBack);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtDescribe = view.findViewById(R.id.edtDescription);
        tvDelete = view.findViewById(R.id.tvDelete);
    }

    @Override
    public void initializeDatabase() {

    }

    private void edit() {
        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtTitle.setEnabled(true);
            edtAmount.setEnabled(true);
            tvChooseDate.setEnabled(true);
            edtDescribe.setEnabled(true);
            isEditing=1;
        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            edtTitle.setEnabled(false);
            tvChooseDate.setEnabled(false);
            edtAmount.setEnabled(false);
            edtDescribe.setEnabled(false);
            isEditing=0;
            Toast.makeText(getActivity(),"Cập nhật thành công!",Toast.LENGTH_LONG).show();
        }

    }


}