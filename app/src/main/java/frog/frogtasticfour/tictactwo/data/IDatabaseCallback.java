package frog.frogtasticfour.tictactwo.data;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IDatabaseCallback<T> {
    void OnSuccess(T document);

    void OnFailure(Exception e);
}
