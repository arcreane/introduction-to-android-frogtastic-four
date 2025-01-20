package frog.frogtasticfour.tictactwo.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Database {
    private final FirebaseFirestore _db;
    public Database() {
        _db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollection(String name) {
        return _db.collection(name);
    }

    public <T> void add(String collection, HashMap<String, Object> data, IDatabaseCallback<T> callback) {
        add(getCollection(collection), data, callback);
    }

    public <T> void add(CollectionReference collection, HashMap<String, Object> data, IDatabaseCallback<T> callback) {
        collection.add(data);
    }

    public void set(CollectionReference collection, String document, HashMap<String, Object> data, IDatabaseCallback<Object> callback) {
        collection.document(document)
                .set(data)
                .addOnSuccessListener(aVoid -> callback.OnSuccess(null))
                .addOnFailureListener(callback::OnFailure);
    }

    public void update(CollectionReference collection, String document, String field, Object value, IDatabaseCallback<Object> callback) {
        collection.document(document).update(field, value).addOnCompleteListener(updateTask -> {
            if (updateTask.isSuccessful()) {
                callback.OnSuccess(null);
            } else {
                callback.OnFailure(updateTask.getException());
            }
        });
    }

    public void get(String collection, String document, IDatabaseCallback<DocumentSnapshot> callback) {
        get(getCollection(collection), document, callback);
    }

    /** @noinspection unchecked*/
    public void get(CollectionReference collection, String documentName, IDatabaseCallback<DocumentSnapshot> callback) {
        DocumentReference docRef = collection.document(documentName);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    callback.OnSuccess(document);
                } else {
                    callback.OnFailure(new Exception("Document does not exist"));
                }
            } else {
                callback.OnFailure(task.getException());
            }
        });
    }

    public void Build(Task<QuerySnapshot> snapshotTask, IDatabaseCallback<QuerySnapshot> callback) {
        snapshotTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    callback.OnSuccess(querySnapshot);
                } else {
                    callback.OnFailure(new Exception("Document does not exist"));
                }
            } else {
                callback.OnFailure(task.getException());
            }
        });
    }
}
