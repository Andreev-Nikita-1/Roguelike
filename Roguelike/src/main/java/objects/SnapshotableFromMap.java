package objects;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public interface SnapshotableFromMap {

    JSONObject getSnapshot();

    static SnapshotableFromMap restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (SnapshotableFromMap) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class)
                .invoke(null, jsonObject);
    }

}
