package com.example.firestoretesting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ForthFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView showTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forth, container, false);
        showTextView = view.findViewById(R.id.content);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = ForthFragmentArgs.fromBundle(getArguments()).getMyArg();
        EditText mess = (EditText)  view.findViewById(R.id.post_text);
        displayAll();

        view.findViewById(R.id.sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mess.length() < 4)
                {
                    ToastMess("Your message is too short");
                }
                else
                {
                    sign(username, mess.getText().toString());
                    mess.setText("");
                }

            }
        });
    }
    public void sign(String username, String s){
        DocumentReference docRef = db.collection("guestbook").document();
        Map<String,Object> map = new HashMap<>();
        map.put("username", username);
        map.put("date", LocalDateTime.now().toString());
        map.put("message", s);
        docRef.set(map);
        displayAll();
    }

    public void displayAll()
    {

        db.collection("guestbook")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String displaying = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> map = document.getData();
                                displaying += "Signed by: "+map.get("username")+"\n";
                                displaying += "Date: "+map.get("date").toString()+"\n";
                                displaying += "Message: "+map.get("message")+"\n \n";
                            }
                            display(displaying);
                        } else {

                        }
                    }
                });

    }

    public void ToastMess(String s)
    {
        Toast t = Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP,0,200);
        t.show();
    }

    public void display(String s)
    {
        showTextView.setText(s);
    }
}