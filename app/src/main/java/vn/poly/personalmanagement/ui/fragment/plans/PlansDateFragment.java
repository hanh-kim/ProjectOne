package vn.poly.personalmanagement.ui.fragment.plans;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.plans.PlansAdapter;
import vn.poly.personalmanagement.database.dao.PlansDAO;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.methodclass.AlarmNotificationReceiver;
import vn.poly.personalmanagement.methodclass.AlarmProvide;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.DateTimeFormat;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Plan;


public class PlansDateFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String FRAG_NAME = PlansDateFragment.class.getName();
    final String keyName = "fragName";
    TextView tvDateToday, tvBack, tvDelete;
    ListView lvPlans;
    ImageView icAdd;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountItem;
    FrameLayout layoutSearch;
    EditText edtSearch;
    MyDatabase mydatabase;
    PlansDAO plansDAO;
    List<Plan> planList;
    Bundle bundle;
    Intent intent;
    PendingIntent pendingIntent;

    FirebaseUser currentUser;
    DatabaseReference databaseReference;

    public PlansDateFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plans_date, container, false);
        initializeViews(view);
        initializeDatabase();
        tvBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        lvPlans.setOnItemClickListener(this);
        intent = new Intent(getActivity(), AlarmNotificationReceiver.class);

        bundle = getArguments();
        if (getArguments() != null) {
            String date = bundle.getString("date");
            if (getArguments().getString("date").equals(CurrentDateTime.getCurrentDate())) {
                tvDateToday.setText("Hôm nay, " + date);
            } else tvDateToday.setText("Ngày " + date);

            countItem();
            showPlans();
            remindPlan();
        }

        return view;
    }

    @Override
    public void initializeViews(View view) {
        tvDateToday = view.findViewById(R.id.tvDateToday);
        tvBack = view.findViewById(R.id.tvBack);
        tvDelete = view.findViewById(R.id.tvDelete);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        lvPlans = view.findViewById(R.id.lvPlans);

        tvCountItem = view.findViewById(R.id.tvCountItem);


    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getActivity());
        plansDAO = new PlansDAO(mydatabase);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        if (tvBack.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
        } else if (tvDelete.equals(view)) {
            delete();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailPlansFragment detailPlansFragment = new DetailPlansFragment();

        Bundle bundle = new Bundle();
        bundle.putString(keyName, FRAG_NAME);
        bundle.putSerializable("plan", getPlansList().get(position));
        detailPlansFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_plans_root, detailPlansFragment).commit();
    }

    private List<Plan> getPlansList() {
        return plansDAO.getAllPlansWithDate(bundle.getString("date"));
    }

    private void countItem() {

        if (getPlansList().size() == 0) {
            tvCountItem.setText("Danh sách trống");
        } else tvCountItem.setText("Tất cả: " + getPlansList().size());

    }

    private void showPlans() {
        planList = getPlansList();
        final PlansAdapter adapter = new PlansAdapter(plansDAO);
        adapter.setDataAdapter(planList, new PlansAdapter.OnNotificationListener() {
            @Override
            public void onClick(final Plan plan, final int position) {

                if (plan.getDate().compareTo(CurrentDateTime.getCurrentDate()) < 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn muốn tiếp tục xóa kế hoạch này?");
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            plansDAO.deleteData(plan);
                            planList.remove(position);
                            adapter.notifyDataSetChanged();
                            countItem();
                        }
                    });
                    builder.create().show();

                }

            }
        });
        lvPlans.setAdapter(adapter);
    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa những kế hoạch trong ngày này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                plansDAO.deleteDataWithDate(bundle.getString("date"));
                Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_plans_root, new MainPlansFragment()).commit();
            }

        });
        builder.create().show();


    }

    private List<Plan> getPlansFuture() {
        return plansDAO.getAllPlansFuture();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void remindPlan() {
        List<Plan> remindList = getPlansFuture();
        if (remindList != null) {
            for (Plan mPlan : remindList) {
                Calendar calendar = DateTimeFormat.parseCalendar(mPlan.getTime(), mPlan.getDate());
           //     Calendar calendar = DateTimeFormat.parseTimeToCalendar(mPlan.getTime());
                Bundle bundle = new Bundle();
                bundle.putSerializable("plan_key", mPlan);
                intent.putExtra("bundle_key", bundle);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), mPlan.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmProvide.getAlarmManager(getActivity()).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }


    }

    private List<Plan> getAllPlans() {
        return plansDAO.getAllData();
    }


}