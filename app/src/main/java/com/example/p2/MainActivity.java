package com.example.p2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUserClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvVertical = findViewById(R.id.rvVertical);
        rvVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<CategoryItem> categories = createSampleData();
        VerticalAdapter adapter = new VerticalAdapter(categories, this);
        rvVertical.setAdapter(adapter);
    }

    @Override
    public void onUserClicked(karbar user) {
        Toast.makeText(this, "Clicked: " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    private List<CategoryItem> createSampleData() {
        List<CategoryItem> list = new ArrayList<>();

        list.add(new CategoryItem("Friends", generateUsers("Friend", 8)));
        list.add(new CategoryItem("Colleagues", generateUsers("Colleague", 6)));
        list.add(new CategoryItem("Family", generateUsers("Family", 5)));
        list.add(new CategoryItem("Classmates", generateUsers("Class", 7)));

        return list;
    }

    private List<karbar> generateUsers(String prefix, int count) {
        List<karbar> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            users.add(new User(
                    System.currentTimeMillis() + i,
                    prefix + " User " + i,
                    prefix.toLowerCase() + i + "@example.com"
            ));
        }
        return users;
    }
}

