package tinkoff.androidcourse.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created on 25.05.2017
 *
 * @author Puzino Yury
 */

public abstract class DefaultValueEventListener implements ValueEventListener {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot){

    }

    @Override
    public void onCancelled(DatabaseError databaseError){

    }
}
