package com.example.firestoretesting;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FifthFragment extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView showTextView;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fifth, container, false);
        showTextView = view.findViewById(R.id.log);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        EditText dname = (EditText) view.findViewById(R.id.device_name);
        view.findViewById(R.id.find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    find_and_show(dname.getText().toString());
                    dname.setText("");

            }
        });
    }

    public void find_and_show(String device_name)
    {

        db.collection(device_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String displaying = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Map<String,Object> map = document.getData();
                                if(map.get("location") != null)
                                {
                                    setMap((String) map.get("location"));

                                }
                                else {
                                    Timestamp timestamp = (Timestamp) map.get("time");
                                    displaying += "\n \n"+"Temperature: " + map.get("temperature") + "\n";
                                    displaying += "Pressure: " + map.get("pressure").toString() + "\n";
                                    displaying += "Humidity: " + map.get("humidity") + "\n";
                                    displaying += "Date/Time: " + timestamp.toDate();
                                }
                            }
                            display(displaying);
                        } else {
                            ToastMess("No device found!");
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

    public void setMap(String location)
    {
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" +
                location + "&zoom=17&markers=color:blue%7C"+
                location +"&size=600x600&region=AU&key=AIzaSyDJ8xJEuceNVuFVNuOpZImdcvyakOYbJYk";


        ImageView viewImg = (ImageView) view.findViewById(R.id.imageView);
        Picasso.get().load(url).into(viewImg);
    }

    public void display(String s)
    {
        showTextView.setText(s);
    }

}