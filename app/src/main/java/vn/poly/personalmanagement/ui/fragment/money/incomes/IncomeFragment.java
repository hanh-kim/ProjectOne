package vn.poly.personalmanagement.ui.fragment.money.incomes;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import vn.poly.personalmanagement.adapter.money.incomes.IncomesAdapter;
import vn.poly.personalmanagement.database.dao.IncomesDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.ObjectDate;
import vn.poly.personalmanagement.ui.fragment.money.MoneyFragment;


public class IncomeFragment extends Fragment
        implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    TextView tvBack, tvCurrentDate, tvCountItem, tv;
    ListView lvIncomes;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;

    MyDatabase mydatabase;
    IncomesDAO incomesDAO;
    List<Income> incomeList;

    public IncomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        initializeViews(view);
        initializeDatabase();
        tvCurrentDate.setText("Hôm nay," + CurrentDateTime.getCurrentDate());
        tvBack.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        tv.setOnClickListener(this);

        lvIncomes.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);

        countItem();
        showIncomeDate();
        return view;
    }


    @Override
    public void initializeViews(View view) {
        tv = view.findViewById(R.id.tv);
        icAdd = view.findViewById(R.id.icAdd);
        tvBack = view.findViewById(R.id.tvBack);
        tvCountItem = view.findViewById(R.id.tvCountItem);
        lvIncomes = view.findViewById(R.id.lvIncomes);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        incomesDAO = new IncomesDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Income income = incomeList.get(position);
        DetailIncomeFragment detailIncomeFragment = new DetailIncomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("income", income);
        detailIncomeFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_money_root, detailIncomeFragment).commit();

    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new MoneyFragment()).commit();
        } else if (icAdd.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new AddIncomesFragment()).commit();
        } else if (tv.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_money_root, new DetailIncomeFragment()).commit();
        } else if (tvToSearch.equals(view)) {
            startSearch();
        } else if (tvCancelSearch.equals(view)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        layoutSearch.setVisibility(View.VISIBLE);
        edtSearch.setText("");
    }

    private List<ObjectDate> getMoneytDateList() {
        return incomesDAO.getIncomeDateList();
    }

    private List<Income> getIncomeList() {
        return incomesDAO.getAllIncomes();
    }

    private void countItem() {
        if (getIncomeList().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getIncomeList().size());

    }

    private void showIncomeDate() {
        incomeList = getIncomeList();
        final IncomesAdapter adapter = new IncomesAdapter();
        adapter.setDataAdapter(incomeList, new IncomesAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(final Income income, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn muốn xóa khoản thu này?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        incomesDAO.deleteData(income);
                        incomeList.remove(position);
                        adapter.notifyDataSetChanged();
                        countItem();
                        Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
                    }

                });
                builder.create().show();
            }
        });
        lvIncomes.setAdapter(adapter);
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}