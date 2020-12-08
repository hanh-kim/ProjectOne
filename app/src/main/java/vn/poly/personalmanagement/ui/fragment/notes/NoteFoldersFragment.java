package vn.poly.personalmanagement.ui.fragment.notes;

import android.app.Activity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.methodclass.Initialize;

import java.util.ArrayList;
import java.util.List;


public class NoteFoldersFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int idFrag = 0;
    final String keyName = "idFrag";
    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvCountNotes1, tvCountNotes2, tvCountNotes3;
    FrameLayout layoutSearch;
    EditText edtSearch;

    List<String> listString = new ArrayList<>();
    CardView cardNotes, cardNotesImportant, cardDeleted;
    Mydatabase mydatabase;
    NotesDAO notesDAO;


    public NoteFoldersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_note_folders, container, false);
        initializeViews(view);
        initializeDatabase();
        countNotes();
        cardNotesImportant.setOnClickListener(this);
        cardNotes.setOnClickListener(this);
        cardDeleted.setOnClickListener(this);
        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);

        return view;
    }


    private void countNotes() {
        tvCountNotes1.setText(""+notesDAO.countNotes(NotesFragment.FRAG_ID));
        tvCountNotes2.setText(""+notesDAO.countNotes(ImportantNotesFragment.FRAG_ID));
        tvCountNotes3.setText(""+notesDAO.countNoteDeleted());

    }

    @Override
    public void initializeViews(View view) {
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvCountNotes3 = view.findViewById(R.id.tvCountNotesDelete);
        tvCountNotes2 = view.findViewById(R.id.tvCountNotesImportant);
        tvCountNotes1 = view.findViewById(R.id.tvCountNotes);
        //   lvFolder = view.findViewById(R.id.lvFolder);

        cardNotes = view.findViewById(R.id.card_note);
        cardNotesImportant = view.findViewById(R.id.card_note_important);
        cardDeleted = view.findViewById(R.id.card_deleted);

    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        notesDAO = new NotesDAO(mydatabase);
    }

    @Override
    public void onClick(View view) {


        if (cardNotes.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new NotesFragment()).commit();
        } else if (cardNotesImportant.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new ImportantNotesFragment()).commit();
        } else if (cardDeleted.equals(view)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_notes_root, new NotesDeletedFragment()).commit();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DetailNoteFragment detailNoteFragment = new DetailNoteFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(keyName,idFrag);
//        Note note = noteList.get(position);
//        bundle.putString(keyNoteTitle,note.getTitle());
//        bundle.putString(keyContent,note.getContent());
//        bundle.putString(keyDate,note.getDate());
//        bundle.putString(keyTime,note.getTime());
//        detailNoteFragment.setArguments(bundle);
//        getActivity().getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_notes_root, detailNoteFragment).commit();
    }
    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }
}