package fjarquellada.es.sec04realmboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import fjarquellada.es.sec04realmboard.R;
import fjarquellada.es.sec04realmboard.model.Board;
import io.realm.RealmList;

/**
 * Created by francisco on 18/3/18.
 */

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board>boards;
    private int layout;

    public BoardAdapter(Context context, List<Board> boards, int layout){
        this.context = context;
        this.boards = boards;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return boards.size();
    }

    @Override
    public Object getItem(int position) {
        return boards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return boards.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(R.layout.listview_board_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.getTextViewBoardTitle().setText(boards.get(position).getTitle());
        String notas = boards.get(position).getNotes().size() == 1 ? 1 +" nota" : boards.get(position).getNotes().size() + " notas";
        vh.getTextViewNotes().setText( notas );
        vh.getTextViewBoardDate().setText(new SimpleDateFormat("dd/mm/yyyy").format(boards.get(position).getCreatedAt()));
        return convertView;
    }

    class ViewHolder{
        private TextView textViewBoardTitle;
        private TextView textViewNotes;
        private TextView textViewBoardDate;

        public ViewHolder(View view){
            this.textViewBoardTitle  = view.findViewById(R.id.text_view_board_title);
            this.textViewNotes      = view.findViewById(R.id.text_view_board_notes);
            this.textViewBoardDate  = view.findViewById(R.id.text_view_board_date);
        }

        public TextView getTextViewBoardTitle() {
            return textViewBoardTitle;
        }

        public void setTextViewBoardTitle(TextView textViewBoardTitle) {
            this.textViewBoardTitle = textViewBoardTitle;
        }

        public TextView getTextViewNotes() {
            return textViewNotes;
        }

        public void setTextViewNotes(TextView textViewNotes) {
            this.textViewNotes = textViewNotes;
        }

        public TextView getTextViewBoardDate() {
            return textViewBoardDate;
        }

        public void setTextViewBoardDate(TextView textViewBoardDate) {
            this.textViewBoardDate = textViewBoardDate;
        }
    }
}
