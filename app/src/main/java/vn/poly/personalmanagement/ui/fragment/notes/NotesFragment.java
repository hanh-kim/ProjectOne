package vn.poly.personalmanagement.ui.fragment.notes;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class NotesFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int FRAG_ID = 1;
    public static final String TAG_NAME = DetailNoteFragment.class.getName();
    public static final String FRAG_NAME = "Ghi chú";
    final String keyNote = "note";
    final String keyName = "idFrag";
    final String keyNoteTitle = "title";
    final String keyDate = "date";
    final String keyTime = "time";
    final String keyContent = "content";
    Mydatabase mydatabase;
    NotesDAO notesDAO;
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch;
    FrameLayout layoutSearch;
    EditText edtSearch;
    ImageView icAddNote;
    TextView tvBack, tvCount;
    int id = FRAG_ID;
    List<Note> noteList;
    ListView lvNotes;
    NotesAdapter adapter;

    public NotesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initializeViews(view);
        initializeDatabase();
        adapter = new NotesAdapter(getContext());
        icAddNote.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        lvNotes.setOnItemClickListener(this);
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        noteList = getList();
//        for (int i = 0; i < 10; i++) {
//            Note note = new Note();
//            note.setTitle("Ghi chú " + (i + 1));
//            note.setFolderID(FRAG_ID);
//            note.setTime(CurrentDateTime.getCurrentTime());
//            note.setDate(CurrentDateTime.getCurrentDate());
//            note.setContent(" tvCount =  view.findViewById(R.id.tvNoteCount);\n" +
//                    "        lvNotes = view.findViewById(R.id.lvNotes);\n" +
//                    "        noteList = new ArrayList<>();\n" +
//                    "        tvBack = view.findViewById(R.id.tvBack);\n" +
//                    "        icAddNote = view.findViewById(R.id.icAddNote);");
//            note.setIsDeleted(0);
//            notesDAO.addData(note);
//        }

        //   noteList = getList();
        countItem();

        adapter.setDataAdapter(noteList, new NotesAdapter.OnItemRemoveListener() {
            @Override
            public void onRemove(Note note, int position) {
                removeItem(note, position);
            }
        });

        lvNotes.setAdapter(adapter);
        return view;
    }

    private List<Note> getList() {
        List<Note> noteList = notesDAO.getAllData(FRAG_ID);
        return noteList;
    }

    private void countItem() {
        if (noteList.size() == 0) {
            tvCount.setText("Không có ghi chú nào");
        } else tvCount.setText("Có " + noteList.size() + " ghi chú");
    }


    @Override
    public void initializeViews(View view) {
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);

        tvCount = view.findViewById(R.id.tvNoteCount);
        lvNotes = view.findViewById(R.id.lvNotes);
        tvBack = view.findViewById(R.id.tvBack);
        icAddNote = view.findViewById(R.id.icAddNote);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        notesDAO = new NotesDAO(mydatabase);
    }

    @Override
    public void onClick(View v) {
        if (icAddNote.equals(v)) {
            AddNoteFragment addNoteFragment = new AddNoteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(keyName, FRAG_ID);
            addNoteFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, addNoteFragment).commit();
        } else if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new NoteFoldersFragment()).commit();

        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DetailNoteFragment detailNoteFragment = new DetailNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName, FRAG_ID);
        Note note = getList().get(position);
        bundle.putSerializable(keyNote, note);
        detailNoteFragment.setArguments(bundle);

        fragmentTransaction.addToBackStack(NotesFragment.TAG_NAME);
        fragmentTransaction.replace(R.id.fragment_notes_root, detailNoteFragment).commit();
    }

    private void removeItem(final Note note, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa ghi chú này");
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
                adapter.notifyDataSetChanged();
                countItem();
                Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
            }
        });

        builder.create().show();


    }
    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

}