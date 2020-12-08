package vn.poly.personalmanagement.ui.fragment.notes;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;


public class NotesDeletedFragment extends Fragment implements Initialize, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int FRAG_ID = 3;
    final String keyName = "idFrag";
    final String keyNote = "note";
    final String keyNoteTitle = "title";
    final String keyDate = "date";
    final String keyTime = "time";
    final String keyContent = "content";

    ListView lvResultSearch;
    TextView tvToSearch, tvCancelSearch, tvClearAll;
    FrameLayout layoutSearch;
    EditText edtSearch;
    ImageView icAddNote;
    TextView tvBack, tvCount;
    int id = FRAG_ID;
    List<Note> noteList;
    ListView lvNotes;
    NotesAdapter adapter;
    Mydatabase mydatabase;
    NotesDAO notesDAO;

    public NotesDeletedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notes_deleted, container, false);
        initializeViews(view);
        initializeDatabase();
        tvBack.setOnClickListener(this);

        lvResultSearch.setOnItemClickListener(this);
        tvToSearch.setOnClickListener(this);
        tvClearAll.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        lvNotes.setOnItemClickListener(this);
        adapter = new NotesAdapter(getContext());
        noteList = notesDAO.getAllDataDeleted();
        search();
        countItem();
        adapter.setDataAdapter(noteList, new NotesAdapter.OnItemShowMenuClickListener() {
            @Override
            public void onClick(final Note note, final int position, View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.menu_item_note);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Note nt = note;
                        int itemId = item.getItemId();
                        if (itemId == R.id.item_restore) {
                            restoreItem(nt, position);
                        } else if (itemId == R.id.item_delete) {
                            removeItem(nt, position);
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
        lvNotes.setAdapter(adapter);


        return view;
    }



    @Override
    public void onClick(View v) {
        if (tvBack.equals(v)) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_notes_root, new NoteFoldersFragment()).commit();

        } else if (tvToSearch.equals(v)) {
            startSearch();
        } else if (tvCancelSearch.equals(v)) {
            hideSoftKeyboard();
            cancelSearch();
        } else if (tvClearAll.equals(v)) {

            if (notesDAO.getAllDataDeleted().size() == 0) {
                Toast.makeText(getActivity(), "Không có ghi chú!", Toast.LENGTH_LONG).show();
            } else {
                deleteAllForever();
            }
        }
    }

    @Override
    public void initializeViews(View view) {
        tvCount = view.findViewById(R.id.tvNoteCount);
        lvNotes = view.findViewById(R.id.lvNotesDeleted);
        tvBack = view.findViewById(R.id.tvBack);
        tvToSearch = view.findViewById(R.id.tvToSearch);
        tvCancelSearch = view.findViewById(R.id.tvCancelSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        lvResultSearch = view.findViewById(R.id.lvResultSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        tvClearAll = view.findViewById(R.id.tvClearAll);
    }

    @Override
    public void initializeDatabase() {
        mydatabase = new Mydatabase(getContext());
        notesDAO = new NotesDAO(mydatabase);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailNoteFragment detailNoteFragment = new DetailNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyName, FRAG_ID);

//        bundle.putString(keyNoteTitle, note.getTitle());
//        bundle.putString(keyContent, note.getContent());
//        bundle.putString(keyDate, note.getDate());
//        bundle.putString(keyTime, note.getTime());
        Note note = noteList.get(position);
        bundle.putSerializable(keyNote, note);
        detailNoteFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_notes_root, detailNoteFragment).commit();
    }

    private void search() {
        final NotesAdapter adapter = new NotesAdapter(getActivity());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edtSearch.getText().toString().trim();
                List<Note> noteList;

                if (!text.isEmpty()){
                    noteList=notesDAO.getResultSearched(text);
                    adapter.setDataAdapter(noteList, new NotesAdapter.OnItemRemoveListener() {
                        @Override
                        public void onRemove(Note note, int position) {
                            removeItem(note,position);
                        }
                    });
                    lvResultSearch.setAdapter(adapter);
                    NotesDeletedFragment.this.adapter.notifyDataSetChanged();
                }else {
                    noteList= new ArrayList<>();
                    adapter.setDataAdapter(noteList, new NotesAdapter.OnItemRemoveListener() {
                        @Override
                        public void onRemove(Note note, int position) {
                            removeItem(note,position);
                        }
                    });
                    lvResultSearch.setAdapter(adapter);
                    NotesDeletedFragment.this.adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void restoreItem(Note note, final int position) {
        note.setIsDeleted(0);
        notesDAO.updateData(note);
        noteList.remove(position);
        countItem();
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Khôi phục thành công!", Toast.LENGTH_LONG).show();
    }

    private void removeItem(final Note note, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn muốn xóa vĩnh viễn ghi chú này?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesDAO.deleteData(note);
                noteList.remove(position);
                adapter.notifyDataSetChanged();
                countItem();
                Toast.makeText(getActivity(), "Đã xóa thành công!", Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }

    private void countItem() {
        noteList = notesDAO.getAllDataDeleted();
        if (noteList.size() == 0) {
            tvCount.setText("Không có ghi chú nào");
        } else tvCount.setText("Có " + noteList.size() + " ghi chú");
    }

    private void cancelSearch() {
        edtSearch.setText("");
        layoutSearch.setVisibility(View.GONE);
        hideSoftKeyboard();
    }

    private void startSearch() {
        edtSearch.setText("");
        layoutSearch.setVisibility(View.VISIBLE);
    }

    private void deleteAllForever() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Tất cả ghi chú này sẽ bị xóa vĩnh viễn. Không thể hoàn tác sau thao tác này.");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesDAO.clearData();
                noteList.clear();
                countItem();
                adapter.notifyDataSetChanged();
                //  Toast.makeText(getActivity(), "Đã xóa tất cả!", Toast.LENGTH_LONG).show();

            }
        });
        builder.create().show();
    }

    public void hideSoftKeyboard() {
     //   InputMethodManager inputMethodManager =
      try {
          InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
          inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(),0);

      }catch (NullPointerException e){
          e.fillInStackTrace();
      }
    }

}