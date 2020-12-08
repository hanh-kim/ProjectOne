package vn.poly.personalmanagement.adapter.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.model.Note;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
   private   Context context;
   private List<Note> noteList;
    public interface OnItemRemoveListener {
        void onRemove(Note note, int position);
    }
    public interface OnItemShowMenuClickListener {
        void onClick(Note note, int position,View view);
    }
    private OnItemRemoveListener onItemRemoveListener;
    private OnItemShowMenuClickListener onItemShowMenuClickListener;

    public NotesAdapter(Context context) {
        this.context = context;
    }
    public NotesAdapter(Context context,List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    public void setDataAdapter(List<Note> noteList, OnItemRemoveListener onItemRemoveListener) {
        this.noteList = noteList;
        this.onItemRemoveListener = onItemRemoveListener;
        notifyDataSetChanged();
    }
    public void setDataAdapter(List<Note> noteList,  OnItemShowMenuClickListener onItemShowMenuClickListener) {
        this.noteList = noteList;
        this.onItemShowMenuClickListener = onItemShowMenuClickListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_note, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvNoteTitle);
            viewHolder.tvDateTime = convertView.findViewById(R.id.tvDateTime);
            viewHolder.tvFolderName = convertView.findViewById(R.id.tvFolderName);
            viewHolder.icInfo = convertView.findViewById(R.id.icInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Note note = noteList.get(position);
        String folderName = "";
        if (note.getFolderID() == 1) {
            folderName = "Ghi chú";
        } else if (note.getFolderID() == 2) {
            folderName = "Ghi chú quan trọng";
        }
        viewHolder.tvTitle.setText(note.getTitle());
        viewHolder.tvDateTime.setText(note.getTime() + ", " + note.getDate());
        viewHolder.tvFolderName.setText(folderName);
        if(note.getIsDeleted()==1){
            viewHolder.icInfo.setImageResource(R.drawable.ic_info);
            viewHolder.icInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemShowMenuClickListener.onClick(note,position,viewHolder.icInfo);
                }
            });
        }else {
            viewHolder.icInfo.setImageResource(R.drawable.ic_outline_remove);
            viewHolder.icInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemRemoveListener.onRemove(note, position);
                }
            });
        }

        notifyDataSetChanged();
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle, tvDateTime, tvFolderName;
        ImageView icInfo;

    }


}
