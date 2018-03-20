package fjarquellada.es.sec04realmboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import fjarquellada.es.sec04realmboard.R;
import fjarquellada.es.sec04realmboard.model.Note;

/**
 * Created by francisco on 19/3/18.
 */

public class NoteAdapter extends BaseAdapter {

    private List<Note> notes;
    private int layout;
    private Context context;

    public NoteAdapter(List<Note> notes, int layout, Context context) {
        this.notes = notes;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(this.layout, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        Note note = this.notes.get(position);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        vh.getTextViewDescription().setText(note.getDescription());
        vh.getTextViewCreatedAt().setText(df.format(note.getCreatedAt()));

        return convertView;
    }

    class ViewHolder{
        private TextView textViewDescription;
        private TextView textViewCreatedAt;

        public ViewHolder(View view) {
            this.textViewDescription = view.findViewById(R.id.text_view_note_description);
            this.textViewCreatedAt = view.findViewById(R.id.text_view_note_created_at);
        }

        public TextView getTextViewDescription() {
            return textViewDescription;
        }

        public void setTextViewDescription(TextView textViewDescription) {
            this.textViewDescription = textViewDescription;
        }

        public TextView getTextViewCreatedAt() {
            return textViewCreatedAt;
        }

        public void setTextViewCreatedAt(TextView textViewCreatedAt) {
            this.textViewCreatedAt = textViewCreatedAt;
        }
    }
}
