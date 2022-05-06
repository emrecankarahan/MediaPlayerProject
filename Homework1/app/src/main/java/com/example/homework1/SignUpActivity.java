package com.example.homework1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.homework1.databinding.ActivitySignUpBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private ActivitySignUpBinding binding;
    Bitmap selectedImage;
    SQLiteDatabase database;
    SQLiteDatabase db;
    ArrayList<Users> usersArrayList;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        usersArrayList = new ArrayList<>();
        registerLauncher();
        getData();
    }
    public void signUp(View view){
        String name = binding.editTextSignUpName.getText().toString();
        String surname = binding.editTextSignUpSurname.getText().toString();
        String email = binding.editTextSignUpEmail.getText().toString();
        String password = binding.editTextSignUpPassword.getText().toString();
        String password2 = binding.editTextSignUpPassword2.getText().toString();
        String phoneNumber = binding.editTextSignUpPhoneNumber.getText().toString();
        /*
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();
        */
        System.out.println("Size(1) ---> "+usersArrayList.size());
        if( name.equals("") || surname.equals("") || email.equals("") || password.equals("") || password2.equals("") || phoneNumber.equals("") ){
            Toast.makeText(SignUpActivity.this,"All information must be written!!",Toast.LENGTH_LONG).show();
        }else if(password.equals(password2)) {
            i = 0;
            while (usersArrayList.size() > i && !(usersArrayList.get(i).email.equals(email))){
                i++;
            }
            if(usersArrayList.size() == i){
                try{
                    database = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY,name VARCHAR,surname VARCHAR,email VARCHAR,phoneNumber VARCHAR,password VARCHAR)");
                    String sqlString = "INSERT INTO users(name,surname,email,phoneNumber,password) VALUES(?,?,?,?,?)";
                    SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                    sqLiteStatement.bindString(1, name);
                    sqLiteStatement.bindString(2, surname);
                    sqLiteStatement.bindString(3, email);
                    sqLiteStatement.bindString(4, phoneNumber);
                    sqLiteStatement.bindString(5, password);
                    //sqLiteStatement.bindBlob(6,byteArray);
                    sqLiteStatement.execute();
                    System.out.println("Information Loaded!");
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(SignUpActivity.this,"Registering is successful!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignUpActivity.this,"The email is already used!!",Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(SignUpActivity.this,"The password does not match!!",Toast.LENGTH_LONG).show();
        }
    }

    public void sendMessage(View view){
        String name = binding.editTextSignUpName.getText().toString();
        String surname = binding.editTextSignUpSurname.getText().toString();
        String email = binding.editTextSignUpEmail.getText().toString();
        String password = binding.editTextSignUpPassword.getText().toString();
        String password2 = binding.editTextSignUpPassword2.getText().toString();
        String phoneNumber = binding.editTextSignUpPhoneNumber.getText().toString();
        Intent sendingMail = new Intent(Intent.ACTION_SEND);
        if(name.equals("") || surname.equals("") || email.equals("") || password.equals("") || password2.equals("") || phoneNumber.equals("") ){
            Toast.makeText(SignUpActivity.this,"All information must be written!!",Toast.LENGTH_LONG).show();
        }else if(password.equals(password2)) {
            while (usersArrayList.size() > i && !(usersArrayList.get(i).email.equals(email))){
                i++;
            }
            if(usersArrayList.size() == i){
                sendingMail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                sendingMail.putExtra(Intent.EXTRA_SUBJECT,"Information about Registration");
                sendingMail.putExtra(Intent.EXTRA_TEXT,name+" "+surname+"\nPhone Number: "+phoneNumber+"\nPassword: "+password);
                sendingMail.setType("message/rfc822");
                startActivity(Intent.createChooser(sendingMail,"Choose an Email Client"));
            }else{
                Toast.makeText(SignUpActivity.this,"The email is already used!!",Toast.LENGTH_LONG).show();
            }


        }else
        {
            Toast.makeText(SignUpActivity.this,"The password does not match!!",Toast.LENGTH_LONG).show();
        }


    }

    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // request permission !!
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Need a photo",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();

            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            // go gallery !!
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }
    }

    private void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        Uri imageData = intentFromResult.getData();
                        binding.imageViewSignUpSelectImage.setImageURI(imageData);
                        try{
                            if(Build.VERSION.SDK_INT >=28 ) {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageData);
                                Bitmap selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageViewSignUpSelectImage.setImageBitmap(selectedImage);
                            }else{
                                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                                binding.imageViewSignUpSelectImage.setImageBitmap(selectedImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{

                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if ( result == true ){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(SignUpActivity.this,"Need a permission for your photo",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getData(){
        try{
            db = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM users",null);
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

