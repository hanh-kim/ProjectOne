package vn.poly.personalmanagement.ui.fragment.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.database.dao.NotesDAO;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.methodclass.Initialize;
import vn.poly.personalmanagement.model.Note;

public class DetailNoteFragment extends Fragment implements Initialize, View.OnClickListener {
    final String keyName = "idFrag";
    final String keyNote = "note";
    public static final String TAG_NAME = DetailNoteFragment.class.getName();

    int isEditing = 0;
    TextView tvEdit, tvBack, tvDateTime, tvRestore;
    EditText edtTitle, edtContent;
    Mydatabase mydatabase;
    NotesDAO notesDAO;

    public DetailNoteFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);
        initializeViews(view);
        initializeDatabase();
        tvEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);

        if (getArguments().getInt(keyName) == NotesDeletedFragment.FRAG_ID) {
            tvEdit.setText("Xóa");
            tvRestore.setOnClickListener(this);
        } else {
            tvEdit.setText("Sửa");
            tvRestore.setText("Xóa ghi chú");
            tvRestore.setOnClickListener(this);
        }
        Note note = (Note) getArguments().get(keyNote);
        tvDateTime.setText(note.getTime() + ", ngày " + note.getDate());
        edtTitle.setText(note.getTitle());
        edtContent.setText(" " + note.getContent());
        return view;
    }

    @Override
    public void onClick(View v) {

        if (getArguments().getInt(keyName) == NotesFragment.FRAG_ID) {
            // id =1 : notes fragment
            if (tvEdit.equals(v)) {
                edit();
            } else if (tvBack.equals(v)) {
//                if (getFragmentManager()!=null){
//                    getFragmentManager().popBackStack();
//                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_notes_root, new NotesFragment()).commit();

            } else if (tvRestore.equals(v)) {
                removeNote(new NotesFragment());

            }


        } else if (getArguments().getInt(keyName) == ImportantNotesFragment.FRAG_ID) {
            //id=2:  important notes fragment

            if (tvEdit.equals(v)) {
                edit();
            } else if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_notes_root, new ImportantNotesFragment()).commit();
            } else if (tvRestore.equals(v)) {
                removeNote(new ImportantNotesFragment());

            }


        } else if (getArguments().getInt(keyName) == NotesDeletedFragment.FRAG_ID) {
            //id=3:  deleted notes fragment
            if (tvEdit.equals(v)) {
                deleteForever();
            } else if (tvBack.equals(v)) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_notes_root, new NotesDeletedFragment()).commit();

            } else if (tvRestore.equals(v)) {
                restoreNote();
            }
        }
    }

    private void updateNote() {

    }


    private void deleteForever() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Xóa vĩnh viễn ghi chú này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Note note = (Note) getArguments().get(keyNote);
                notesDAO.deleteData(note);
                Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_notes_root, new NotesDeletedFragment()).commit();
            }
        });
        builder.create().show();
    }

    private void removeNote(final Fragment fragment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Xóa ghi chú này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Note note = (Note) getArguments().get(keyNote);
                note.setIsDeleted(1);
                notesDAO.updateData(note);
                Toast.makeText(getActivity(), "Ghi chú được chuyển đến thùng rác!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_notes_root, fragment).commit();
            }
        });
        builder.create().show();

    }

    private void restoreNote() {
        Note note = (Note) getArguments().get(keyNote);
        note.setIsDeleted(0);
        notesDAO.updateData(note);
        Toast.makeText(getActivity(), "Khôi phục thành công!", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_notes_root, new NotesDeletedFragment()).commit();
    }

    @Override
    public void initializeViews(View view) {
        tvEdit = view.findViewById(R.id.tvEdit);
        tvBack = view.findViewById(R.id.tvBack);
        edtTitle = view.findViewById(R.id.edtNoteTitle);
        edtContent = view.findViewById(R.id.edtNoteContent);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvRestore = view.findViewById(R.id.tvRestore);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getActivity());
        notesDAO = new NotesDAO(mydatabase);
    }

    private void edit() {

        if (isEditing == 0) {
            tvEdit.setText("Xong");
            edtTitle.setEnabled(true);
            edtContent.setEnabled(true);
            isEditing = 1;

        } else if (isEditing == 1) {
            tvEdit.setText("Sửa");
            edtTitle.setEnabled(false);
            edtContent.setEnabled(false);
            isEditing = 0;
            Note note = (Note) getArguments().get(keyNote);
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            if (title.isEmpty()) {
                note.setTitle("Không có tiêu đề");
            } else {
                note.setTitle(title);
            }
            //  note.setFolderID(fragId);
            note.setDate(CurrentDateTime.getCurrentDate());
            note.setTime(CurrentDateTime.getCurrentTime());
            if (content.isEmpty()) {
                note.setContent(" ");
            } else note.setContent(content);

            note.setIsDeleted(0);
            notesDAO.updateData(note);
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_LONG).show();
        }

    }
}