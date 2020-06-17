package com.example.firestoretesting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private TextView showTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        showTextView = view.findViewById(R.id.hello);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = SecondFragmentArgs.fromBundle(getArguments()).getMyArg();

        display("Hello " + username);

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.cpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondFragmentDirections.ActionSecondFragmentToThirdFragment action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(username);
                NavHostFragment.findNavController(SecondFragment.this).navigate(action);
            }
        });

        view.findViewById(R.id.guestbook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecondFragmentDirections.ActionSecondFragmentToForthFragment action = SecondFragmentDirections.actionSecondFragmentToForthFragment(username);
                NavHostFragment.findNavController(SecondFragment.this).navigate(action);
            }
        });

        view.findViewById(R.id.monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FifthFragment);
            }
        });




    }
    public void display(String s)
    {
        showTextView.setText(s);
    }
}