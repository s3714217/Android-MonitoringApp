package com.example.firestoretesting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ThirdFragment extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = ThirdFragmentArgs.fromBundle(getArguments()).getMyArg();


        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newpass = (EditText) view.findViewById(R.id.new_pass_in);
                EditText oldpass = (EditText) view.findViewById(R.id.old_pass_in);

                if(newpass.length() < 4 || newpass.getText().toString().contains(" "))
                {
                    ToastMess("Invalid new password");
                }
                else
                {
                    change(username,oldpass.getText().toString(), newpass.getText().toString());
                }


            }
        });
    }
    public void change(String username, String old_pass, String new_pass)
    {
        DocumentReference docRef = db.collection("users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> list = document.getData();
                        if(old_pass.equals(list.get("password")))
                        {
                            CollectionReference users = db.collection("users");
                            Map<String, Object> temp = new HashMap<>();
                            temp.put("password", new_pass);
                            users.document(username).set(temp);
                            ToastMess("Password changed");

                        }
                        else
                        {
                            ToastMess("Wrong old password");
                        }
                        return;
                    }
                } else {
                    ToastMess("Error! Please check your internet connection");
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



}