package com.example.todoapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.util.UserManager;

import java.util.List;

public class FragmentAdmin extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        ListView listView = view.findViewById(R.id.listUsers);
        UserManager userManager = new UserManager(requireContext());
        List<String> users = userManager.getAllUsers();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);

        return view;
    }
}
