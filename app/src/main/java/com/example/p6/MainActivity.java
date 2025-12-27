package com.example.p6;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnSave;

    AppDb db;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        db = AppDb.getInstance(this);

        btnSave.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "ایمیل معتبر نیست", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                db.userEmailDao().insert(new UserEmailEntity(email));

                runOnUiThread(() ->
                        Toast.makeText(this, "ایمیل ذخیره شد ✅", Toast.LENGTH_SHORT).show()
                );
            });
        });
    }
}
