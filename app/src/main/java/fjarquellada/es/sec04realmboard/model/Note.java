package fjarquellada.es.sec04realmboard.model;

import java.util.Date;

import fjarquellada.es.sec04realmboard.application.BoardApplication;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by francisco on 18/3/18.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private long id;
    @Required
    private String description;
    @Required
    private Date createdAt;

    public Note() {
    }

    public Note(String description) {
        this.id = BoardApplication.noteID.incrementAndGet();
        this.description = description;
        this.createdAt = new Date();
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
