package fjarquellada.es.sec04realmboard.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fjarquellada.es.sec04realmboard.R;
import fjarquellada.es.sec04realmboard.adapter.BoardAdapter;
import fjarquellada.es.sec04realmboard.model.Board;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener, RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    FloatingActionButton fab;
    EditText editTextTitle;

    private Realm realm;
    private RealmResults<Board> boards;
    private BoardAdapter adapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // DB Realm
        realm = Realm.getDefaultInstance();

        fab = findViewById(R.id.fab_add_board);
        fab.setOnClickListener(this);

        this.listView = findViewById(R.id.listViewBoard);

        this.boards = this.getAllBoards();
        this.boards.addChangeListener(this);

        this.adapter = new BoardAdapter(this, boards, R.layout.listview_board_item);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);

        registerForContextMenu(listView);
    }

    /**
     *
     * @param boardName
     */
    private void createBoard(String boardName){
        realm.beginTransaction();

        Board board = new Board(boardName);
        realm.copyToRealm(board);

        realm.commitTransaction();

        /*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Board board = new Board(boardName);
                realm.copyToRealm(board);
            }
        });*/
    }

    private RealmResults<Board> getAllBoards(){
        return this.realm.where(Board.class).findAll();
    }

    private void deleteBoard(Board board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editBoard(Board board, String boardTitle){
        realm.beginTransaction();
        board.setTitle(boardTitle);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    private void showAlertForCreatingBoard(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!"".equals(title)){
            builder.setTitle(title);
        }
        if(!"".equals(message)){
            builder.setMessage(message);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(view);

        editTextTitle = view.findViewById(R.id.edit_text_title);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = editTextTitle.getText().toString().trim();
                if(!"".equals(boardName)){
                    createBoard(boardName);
                }else{
                    Toast.makeText(BoardActivity.this, "No ha escrito usted ningún nombre.", Toast.LENGTH_LONG).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showAlertForEditBoard(String title, String message, final Board board){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!"".equals(title)){
            builder.setTitle(title);
        }
        if(!"".equals(message)){
            builder.setMessage(message);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(view);

        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextTitle.setText(board.getTitle());

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = editTextTitle.getText().toString().trim();

                if(!"".equals(boardName)){
                    editBoard(board, boardName);
                }else{
                    Toast.makeText(BoardActivity.this, "No ha escrito usted ningún nombre.", Toast.LENGTH_LONG).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = true;

        switch(item.getItemId()){
            case R.id.item_board_delete:
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                break;
            default:
                res = super.onOptionsItemSelected(item);
        }

        return res;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.setHeaderTitle(this.boards.get(info.position).getTitle());

        getMenuInflater().inflate(R.menu.context_menu_board, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        boolean res = true;
        Board board = this.boards.get(info.position);
        switch(item.getItemId()){
            case R.id.item_ctx_board_delete:
                this.deleteBoard(board);
                break;
            case R.id.item_ctx_board_edit:
                this.showAlertForEditBoard("Edit Board "+board.getTitle(), "Change Board Title", board);
                break;
            default:
                res = super.onContextItemSelected(item);
        }


        return res;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == fab.getId()){
            showAlertForCreatingBoard("Create Board", "Create new Board");
        }
    }


    @Override
    public void onChange(RealmResults<Board> boards) {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("boardId", boards.get(position).getId());
        startActivity(intent);
    }
}
