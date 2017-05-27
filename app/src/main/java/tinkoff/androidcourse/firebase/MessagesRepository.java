package tinkoff.androidcourse.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

import tinkoff.androidcourse.model.db.MessageItem;

/**
 * Created on 27.05.2017
 *
 * @author Puzino Yury
 */

public class MessagesRepository {

    private final static MessagesRepository instance = new MessagesRepository();
    private final DatabaseReference db;
    private final static String FB_MESSAGE = "messages";   //branch for messages in firebase DB

    public MessagesRepository() {
        this.db = FirebaseDatabase.getInstance().getReference(FB_MESSAGE);
    }

    public static synchronized MessagesRepository getInstance() {
        return instance;
    }

    public void addMessage(final MessageItem messageItem, final OnTransactionComplete<Void> onTransactionComplete) {

        //UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();

        db.push().runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(messageItem);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot dataSnapshot) {

                if (commited) {
                    onTransactionComplete.onCommit(null);
                } else {
                    onTransactionComplete.onAbort(databaseError.toException());
                }
            }

        });

    }

    /**
     * todo: retrieve messages with this method in the future
     * */
    public void getMessages(final MessageItemValueListener eventListener) {

        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        db.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageItem value = dataSnapshot.getValue(MessageItem.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TODO implementation
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO implementation
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO implementation
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO implementation
            }

        });

        db.addListenerForSingleValueEvent(new DefaultValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<MessageItem> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        items.add(snapshot.getValue(MessageItem.class));
                    }

                eventListener.onValue(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
            }

        });
    }
}
