package fjarquellada.es.sec04realmboard.application;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import fjarquellada.es.sec04realmboard.model.Board;
import fjarquellada.es.sec04realmboard.model.Note;
import fjarquellada.es.sec04realmboard.util.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by francisco on 18/3/18.
 */

public class BoardApplication extends Application {

    public static AtomicLong boardID = new AtomicLong();
    public static AtomicLong noteID = new AtomicLong();

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        this.setupRealm();

        //RealmConfiguration realmConfig = configFactory.createConfigurationBuilder().build()
        Realm realm = Realm.getDefaultInstance();
        //PrimaryKeyFactory.init(realm);

        boardID = getIdByTable(realm, Board.class);
        noteID = getIdByTable(realm, Note.class);

        realm.close();
    }

    private void setupRealm(){
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicLong getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results =  realm.where(anyClass).findAll();
        return (results.size() > 0 ) ? new AtomicLong(results.max("id").longValue()) : new AtomicLong();

    }
}
