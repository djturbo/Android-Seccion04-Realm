package fjarquellada.es.sec04realmboard.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import fjarquellada.es.sec04realmboard.R;
import fjarquellada.es.sec04realmboard.adapter.NoteAdapter;
import fjarquellada.es.sec04realmboard.model.Board;
import fjarquellada.es.sec04realmboard.model.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, RealmChangeListener<Board>{

    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private ListView listView;

    private long idBoard;

    private Realm realm;
    private Board board;
    private EditText editTextDescription;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        this.idBoard = (long) getIntent().getExtras().get("boardId");

        board = realm.where(Board.class).equalTo("id", this.idBoard).findFirst();
        notes = board.getNotes();
        board.addChangeListener(this);
        this.setTitle(board.getTitle());

        this.fab = findViewById(R.id.fab_add_note);
        this.fab.setOnClickListener(this);

        this.listView = findViewById(R.id.list_view_notes);
        this.adapter = new NoteAdapter(this.notes, R.layout.list_view_note_item, this);

        this.listView.setAdapter(this.adapter);


        this.fab.setOnClickListener(this);
        registerForContextMenu(listView);
    }

    private void createNote(String description){
        this.realm.beginTransaction();
        Note note = new Note(description);
        realm.copyToRealm(note);
        this.board.getNotes().add(note);
        this.realm.commitTransaction();
    }
    private void deleteNote(Note note){
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editNote(Note note, String description){
        realm.beginTransaction();
        note.setDescription(description);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        board.getNotes().deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void showAlertForCreatingNote(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!"".equals(title)){
            builder.setTitle(title);
        }
        if(!"".equals(message)){
            builder.setMessage(message);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(view);

        editTextDescription = view.findViewById(R.id.edit_text_note_description);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = editTextDescription.getText().toString().trim();
                if(!"".equals(description)){
                    createNote(description);
                }else{
                    Toast.makeText(NoteActivity.this, "No ha escrito usted ningúna descripción.", Toast.LENGTH_LONG).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }private void showAlertForEditNote(String title, String message, final Note note){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!"".equals(title)){
            builder.setTitle(title);
        }
        if(!"".equals(message)){
            builder.setMessage(message);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(view);

        editTextDescription = view.findViewById(R.id.edit_text_note_description);
        editTextDescription.setText(note.getDescription());
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = editTextDescription.getText().toString().trim();
                if(!"".equals(description)){
                    editNote(note, description);
                }else{
                    Toast.makeText(NoteActivity.this, "No ha escrito usted ningúna descripción.", Toast.LENGTH_LONG).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.board.getNotes().get(info.position).getDescription());
        getMenuInflater().inflate(R.menu.context_menu_notes, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        boolean res = true;
        Note note = this.board.getNotes().get(info.position);

        switch(item.getItemId()){
            case R.id.item_ctx_note_delete:
                this.deleteNote(note);
                break;
            case R.id.item_ctx_note_edit:
                this.showAlertForEditNote("Edit Note "+ note.getDescription(), "Editar nota", note);
                break;
            default:
                res = super.onContextItemSelected(item);
                break;
        }

        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = true;

        switch(item.getItemId()){
            case R.id.item_notes_delete:
                this.deleteAll();
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }

        return res;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_add_note){
            showAlertForCreatingNote("Create Note", "Rellene el formulario para crear una nota.");
        }
    }

    @Override
    public void onChange(Board board) {
        this.adapter.notifyDataSetChanged();
    }
}
