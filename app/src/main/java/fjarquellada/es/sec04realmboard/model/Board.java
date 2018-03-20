package fjarquellada.es.sec04realmboard.model;

import java.util.Date;

import fjarquellada.es.sec04realmboard.application.BoardApplication;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by francisco on 18/3/18.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private long id;
    @Required
    private String title;
    @Required
    private Date createdAt;
    private RealmList<Note>notes;

    public Board() {
    }

    public Board(String title) {
        this.id = BoardApplication.boardID.incrementAndGet();
        this.title = title;
        this.createdAt = new Date();
        this.notes = new RealmList<Note>();
    }

    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    public RealmList<Note> getNotes() {
        return notes;
    }

}
