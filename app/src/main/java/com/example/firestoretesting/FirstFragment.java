package com.example.firestoretesting;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private TextView showTextView;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username_input = (EditText) view.findViewById(R.id.name_in);
                EditText pass_input = (EditText) view.findViewById(R.id.pass_in);
                if(username_input.length() < 4 || pass_input.length() < 4)
                {
                    ToastMess("Invalid username or password");
                }
                else
                {
                    get(username_input.getText().toString(), pass_input.getText().toString());
                }
            }
        });

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText username_input = (EditText) view.findViewById(R.id.name_in);
                EditText pass_input = (EditText) view.findViewById(R.id.pass_in);

                if(username_input.getText().toString().contains(" "))
                {
                    ToastMess("Invalid username!");
                }
                else if(pass_input.getText().toString().contains(" "))
                {
                    ToastMess("Invalid password!");
                }
                else if(username_input.getText().toString().length() < 4)
                {
                    ToastMess("Your username is too short!");
                }
                else if(pass_input.getText().toString().length() < 4)
                {
                    ToastMess("Your password is too short!");
                }
                else
                {
                    add(username_input.getText().toString(), pass_input.getText().toString());
                }


            }
        });
    }

    public void add(String username, String password)
    {
        User user = new User(username,password);
        DocumentReference docRef = db.collection("users").document(user.getName());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        CollectionReference users = db.collection("users");
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("password", user.getPass());
                        users.document(user.getName()).set(temp);
                        ToastMess("User Created! Please login");
                        return;
                    } else {
                       ToastMess("Username is taken");
                    }
                } else {
                    ToastMess("Error! Please check your internet connection");
                }
            }
        });
    }

    public void get(String username, String password)
    {
        User user = new User(username,password);
        try{
            DocumentReference docRef = db.collection("users").document(user.getName());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String,Object> list = document.getData();
                            if(user.getPass().equals(list.get("password")))
                            {
                                ToastMess("Welcome!");
                                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(username);
                                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
                            }
                            else
                            {
                                ToastMess("Wrong Password!");
                            }
                        }
                        else
                        {
                            ToastMess("Wrong Username!");
                        }
                    }
                    else
                    {
                        ToastMess("Error! Please check your internet connection");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void ToastMess(String s)
    {

       Toast t = Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP,0,200);
        t.show();

    }

//    public void display(String s)
//    {
//        showTextView.setText(s);
//    }

}



