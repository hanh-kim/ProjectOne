package vn.poly.personalmanagement.ui.fragment.notes;

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

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.adapter.notes.NotesAdapter;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Note;

import java.util.List;


public class ImportantNotesFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int FRAG_ID = 2;
    public  static final  String FRAG_NAME ="Ghi chú quan trọng";
    final String keyName = "idFrag";
    final String keyNote = "note";
    final String keyNoteTitle = "title";
    final String keyDate = "date";
    final String keyTime = "time";
    final String keyContent = "content";
    int id = FRAG_ID;
    TextView tvBack, tvCount;
    ImageView icAdd;
    Bundle bundle;
    List<Note> noteList;
    ListView lvNotes;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    NotesAdapter adapter;
    Mydatabase mydatabase;
    NotesDAO notesDAO;
    public ImportantNotesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_important_notes, container, false);
        initializeViews(view);
        initializeDatabase();
        tvBack.setOnClickListener(this);
        icAdd.setOnClickListener(this);
        lvNotes.setOnItemClickListener(this);

        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        noteList = getList();
        adapter = new NotesAdapter(getContext());


        countItem();
        adapter.setDataAdapter(noteList, new NotesAdapter.OnItemRemoveListener() {
           @Override
           public void onRemove(Note note, int position) {
               removeItem(note,position);
               adapter.notifyDataSetChanged();
           }
       });
        lvNotes.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_notes_root, new NoteFoldersFragment()).commit();

        } else if (icAdd.equals(v)) {
            bundle = new Bundle();
            bundle.putInt(keyName, id);
            AddNoteFragment addNoteFragment = new AddNoteFragment();
            addNoteFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_notes_root, addNoteFragment).commit();
        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
            hideSoftKeyboard();
            cancelSearch();
        }
    }

    @Override
    public void initializeViews(View view) {
        lvNotes = view.findViewById(R.id.lvImportantNotes);
        tvCount = view.findViewById(R.id.tvNoteCount);
        tvBack = view.findViewById(R.id.tvBack);
        icAdd = view.findViewById(R.id.icAddNote);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
    }

    @Override
    public void initializeDatabase() {
       mydatabase = new Mydatabase(getContext());
       notesDAO = new NotesDAO(mydatabase);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        DetailNoteFragment detailNoteFragment = new DetailNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName, FRAG_ID);
        Note note = noteList.get(position);
        bundle.putSerializable(keyNote,note);
        detailNoteFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_notes_root, detailNoteFragment).commit();

    }

    private void cancelSearch() {
        layoutSearch.setVisibility(View.GONE);
        edtSearch.setText("");
    }

    private void startSearch() {
        edtSearch.setText("");
        layoutSearch.setVisibility(View.VISIBLE);
    }
    private void removeItem(final Note note,final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa ghi chú này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Note nt = note;
                nt.setIsDeleted(1);
                notesDAO.updateData(nt);
                noteList.remove(position);
               // noteList= getList();
                adapter.notifyDataSetChanged();
                countItem();
                Toast.makeText(getActivity(), "Ghi chú được chuyển đến thùng rác!", Toast.LENGTH_LONG).show();

            }
        });
        builder.create().show();
    }

    private void countItem() {
        if (noteList.size() == 0) {
            tvCount.setText("Không có ghi chú nào");
        } else tvCount.setText("Có " + noteList.size() + " ghi chú");
    }
    private List<Note> getList(){
        List<Note> list =  notesDAO.getAllData(FRAG_ID);
        return list;
    }
    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }
}