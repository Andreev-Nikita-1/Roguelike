package objects;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Objects, which state have to be saved directly when taking map snapshot.
 * Has to have method "getSnapshot", which saves the classname,
 * and static method "restoreFromSnapshot", containing proper implementation
 */
public interface SnapshotableFromMap {

    JSONObject getSnapshot();

    /**
     * Invokes method "restoreFromSnapshot" on class, taken from snapshot by "class" key
     */
    static SnapshotableFromMap restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (SnapshotableFromMap) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class)
                .invoke(null, jsonObject);
    }

}
