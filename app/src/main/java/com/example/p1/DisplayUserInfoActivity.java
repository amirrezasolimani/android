package com.example.p1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class DisplayUserInfoActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    UserInfoAdapter adapter;
    List<UserItem> userItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        recyclerView = findViewById(R.id.recyclerViewUserInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String age = intent.getStringExtra("age");

        userItemList = new ArrayList<>();
        userItemList.add(new UserItem("Name", name));
        userItemList.add(new UserItem("Email", email));
        userItemList.add(new UserItem("Age", age));

        adapter = new UserInfoAdapter(this, userItemList);
        recyclerView.setAdapter(adapter);
    }
}
