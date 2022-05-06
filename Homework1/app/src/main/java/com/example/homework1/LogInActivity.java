package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homework1.databinding.ActivityLogInBinding;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    ArrayList<Users> usersArrayList;
    int errorCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        usersArrayList = new ArrayList<>();
        getData();
        errorCount = 0;
        binding.editTextLogInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextLogInEmail.getText().toString();
                String password = binding.editTextLogInPassword.getText().toString();
                if ((!email.equals("") && !password.equals(""))) {
                    int i = 0;
                    while (usersArrayList.size() > i && !(usersArrayList.get(i).email.equals(email)) ) {
                        i++;
                    }
                    if(usersArrayList.size() == i){
                        errorCount++;
                        Toast.makeText(LogInActivity.this, "Wrong Information. Try Again.!!("+errorCount+")", Toast.LENGTH_SHORT).show();

                    }
                    else if(usersArrayList.get(i).password.equals(password)){
                        Toast.makeText(LogInActivity.this, "Successfully Completed!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogInActivity.this,MusicListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        errorCount++;
                        Toast.makeText(LogInActivity.this, "The password is not true!("+errorCount+")", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, "All information must be written!!", Toast.LENGTH_SHORT).show();
                }
                if(errorCount >= 3){
                    Toast.makeText(LogInActivity.this,"Wrong Information for 3 times. First Sign Up !!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void getData(){
        try{
            SQLiteDatabase database = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM users",null);
            int nameIx = cursor.getColumnIndex("name");
            int idIx = cursor.getColumnIndex("id");
            int emailIx =cursor.getColumnIndex("email");
            int passwordIx = cursor.getColumnIndex("password");

            while (cursor.moveToNext()){
                String name = cursor.getString(nameIx);
                int id = cursor.getInt(idIx);
                String email=cursor.getString(emailIx);
                String password=cursor.getString(passwordIx);
                Users users = new Users(name,id,email,password);
                usersArrayList.add(users);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}