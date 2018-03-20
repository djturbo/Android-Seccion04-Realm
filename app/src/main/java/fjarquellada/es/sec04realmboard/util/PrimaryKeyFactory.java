package fjarquellada.es.sec04realmboard.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmFieldType;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by francisco on 18/3/18.
 */

public class PrimaryKeyFactory {
    private static Map<Class<? extends RealmModel>, AtomicLong> keyMap;

    /**
     * Initialize the factory. Must be called before any primary key is generated.
     * Note
     *
     * @param realm Realm to configure primary keys for.
     */
    public static synchronized void init(Realm realm) {
        if (keyMap != null) {
            throw new IllegalStateException("Factory has already been initialized. Call reset() before initializing again.");
        }

        RealmSchema schema = realm.getSchema();
        HashMap<Class<? extends RealmModel>, AtomicLong> map = new HashMap<>();

        final RealmConfiguration configuration = realm.getConfiguration();
        for (final Class<? extends RealmModel> c : configuration.getRealmObjectClasses()) {
            String className = c.getSimpleName();
            RealmObjectSchema objectSchema = schema.get(className);
            if (objectSchema.hasPrimaryKey()) {
                String fieldName = objectSchema.getPrimaryKey();
                RealmFieldType type = objectSchema.getFieldType(fieldName);

                if (type == RealmFieldType.INTEGER) {
                    Number val = realm.where(c).max(fieldName);
                    AtomicLong keyGenerator = new AtomicLong(val != null ? val.longValue() : -1);
                    map.put(c, keyGenerator);
                }
            }
        }

        keyMap = map;
    }

    /**
     * Reset this factory and all generated values.
     * Call {@link #init(Realm)} before using this class again.
     */
    public static synchronized void reset() {
        keyMap = null;
    }

    /**
     * Generate the next primary key for a class. Starting from {@code 0}.
     *
     * @param clazz class to generate the next key for.
     */
    public static long nextKey(final Class<? extends RealmObject> clazz) {
        AtomicLong generator = keyMap.get(clazz);
        return generator.incrementAndGet();
    }
}
