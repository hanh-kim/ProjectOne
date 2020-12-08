package vn.poly.personalmanagement.ui.fragment.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Note;

public class AddNoteFragment extends Fragment implements Initialize, View.OnClickListener {

    final String keyName = "idFrag";
    TextView tvBack, tvDone, tvDate;
    Bundle bundle;
    MyDatabase mydatabase;
    NotesDAO notesDAO;
    EditText edtTitle, edtContent;

    public AddNoteFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        bundle = getArguments();
        initializeViews(view);
        initializeDatabase();
        tvDate.setText(CurrentDateTime.getCurrentDate());
        tvDone.setOnClickListener(this);
        tvBack.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (bundle.getInt(keyName) == NotesFragment.FRAG_ID) {
            // id =1 : notes fragment

            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new NotesFragment()).commit();
            } else if (tvDone.equals(v)) {
                addNote(NotesFragment.FRAG_ID);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new NotesFragment()).commit();
            }


        } else if (bundle.getInt(keyName) == ImportantNotesFragment.FRAG_ID) {
            //id=2:  important notes fragment

            if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new ImportantNotesFragment()).commit();
            } else if (tvDone.equals(v)) {
                addNote(ImportantNotesFragment.FRAG_ID);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new ImportantNotesFragment()).commit();

            }


        }


    }

    @Override
    public void initializeViews(View view) {
        tvBack = view.findViewById(R.id.tvBack);
        tvDone = view.findViewById(R.id.tvBack);
        tvDate = view.findViewById(R.id.tvCurrentDate);
        edtContent = view.findViewById(R.id.edtNoteContent);
        edtTitle = view.findViewById(R.id.edtNoteTitle);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new MyDatabase(getContext());
        notesDAO = new NotesDAO(mydatabase);
    }

    private void addNote(int fragId) {
        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString();
        Note note = new Note();
        if (title.isEmpty()) {
            note.setTitle("Không có tiêu đề");
        } else {
            note.setTitle(title);
        }
        note.setFolderID(fragId);
        note.setDate(CurrentDateTime.getCurrentDate());
        note.setTime(CurrentDateTime.getCurrentTime());
        if (content.isEmpty()) {
            note.setContent(" ");
        } else note.setContent(content);

        note.setIsDeleted(0);
        notesDAO.addData(note);
        Toast.makeText(getActivity(), "Lưu thông tin thành công", Toast.LENGTH_SHORT).show();
    }
}