package com.application.atmosphereApp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Locale;

//add try and catches
public class PostActivity extends AppCompatActivity {
    //constant for permission for requesting gallery or camera
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static  final int STORE_REQUEST_CODE = 200;
        // app permissions array for camera and STORAGE
    String[] cameraPerm;
    String[] storagePerm;

    private static  final int IMAGE_PICK_CODE_CAMERA = 300;
    private static  final int IMAGE_PICK_GALLERY_CODE = 400;


   private Uri imageUri = null;
    private StorageTask upload;
      private  StorageReference reference;

    //class vars
   private ImageView exit, add_img;
   private EditText img_def;
   private Button post_now;
    FirebaseAuth firebaseAuth;
    DatabaseReference venueData;
    String name, email, uid, dp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        progressDialog = new ProgressDialog(this);
        venueData = FirebaseDatabase.getInstance().getReference("Venues");
        Query query = venueData.orderByChild("emailAdd").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snappy : snapshot.getChildren()){
                    name = ""+ snappy.child("venueName").getValue();
                    email = ""+snappy.child("emailAdd").getValue();
                    dp = ""+snappy.child("imageURL").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //connect java vars to xml id
        exit = findViewById(R.id.exit);
        add_img = findViewById(R.id.add_img);
        post_now = findViewById(R.id.post_now);
        img_def = findViewById(R.id.img_def);
     reference = FirebaseStorage.getInstance().getReference("posts");
        cameraPerm = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePerm = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickAnImage();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();

            }
        });
        post_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String def = img_def.getText().toString().trim();
                        if(TextUtils.isEmpty(def)) {
                            Toast.makeText(PostActivity.this, "define your night please", Toast.LENGTH_SHORT).show();
                return;
                        }
                if(imageUri!=null){
                    //post without image
                    uploadPostData(def,String.valueOf(imageUri));
                    startActivity(new Intent(PostActivity.this, MainActivity.class));

                } else {

                }
            }
        });

    }

    private void uploadPostData(String description, String uri){

        progressDialog.setMessage("posting your atmosphere");
        progressDialog.show();
        String time = String.valueOf(System.currentTimeMillis());
        String fileName = "Posts/"+"post_"+time;

        if (!uri.equals("default")){
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(fileName);
            reference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    String downloadUri = uriTask.getResult().toString();

                    if (uriTask.isSuccessful()){
                        //url is receivers
                        HashMap<Object, String> map = new HashMap<>();
                        map.put("postID",uid);
                        map.put("postedBy",name);
                        map.put("postDescription",description);
                        map.put("imageURL",downloadUri);

                        //path to firestore
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Posts");
                        reference1.child(time).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                                Toast.makeText(PostActivity.this, "Posted Atmosphere", Toast.LENGTH_SHORT).show();
                                //reset the values
                                img_def.setText("");
                                add_img.setImageURI(null);
                                imageUri = null;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            HashMap<Object, String> map = new HashMap<>();
            map.put("postID",uid);
            map.put("postedBy",name);
            map.put("postDescription",description);
            map.put("imageURL","default");

            //path to firestore
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Posts");
            reference1.child(time).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Posted Atmosphere", Toast.LENGTH_SHORT).show();
                    img_def.setText("");
                    add_img.setImageURI(null);
                    imageUri = null;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    /**
     * method to pick an image provided permissions are checked
     */
    private void pickAnImage(){
        String[] choices = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose An Image");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    if (!checkCameraPerm()){
                        requestCameraPerm();
                    } else {
                        cameraPic();
                    }

                }
                if (i ==1){
                    if (!checkStoragePerm()) {
                        requestStoragePerm();
                    } else {
                        pickGallery();
                    }

                }

            }
        });
        builder.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void cameraPic() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        cv.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);


        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,IMAGE_PICK_CODE_CAMERA);
    }

    /**
     * method to check storage permissions and if they are enabled
     * @return true if enabled
     * return false if not
     */
    private boolean checkStoragePerm(){

        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return res;
    }

    /**
     * method to request the runtime storage perm
     */
    private void requestStoragePerm(){
        ActivityCompat.requestPermissions(this, storagePerm,STORE_REQUEST_CODE);
    }


    /**
     * method to check camera permissions and if they are enabled
     * @return true if enabled
     * return false if not
     */
    private boolean checkCameraPerm(){

        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return res2 && res;
    }

    /**
     * method to request the runtime CAMERA perm
     */
    private void requestCameraPerm(){
        ActivityCompat.requestPermissions(this, cameraPerm,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @Nullable int[] grant) {
        super.onRequestPermissionsResult(requestCode, permissions, grant);


        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grant.length>0){
                    boolean camAccepted = grant[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grant[1] == PackageManager.PERMISSION_GRANTED;
                    if(camAccepted&& storageAccepted){
                        pickAnImage();
                    } else {
                        Toast.makeText(this, "Permissions are necessary", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;
            case STORE_REQUEST_CODE:{
                if (grant.length>0){
                    boolean storageAccepted = grant[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permissions are necessary", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            } break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                imageUri = data.getData();
                add_img.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CODE_CAMERA){
                add_img.setImageURI(imageUri);
            }
        }





        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        checkUserStatus();
        super.onPostResume();
    }

    private void checkUserStatus(){
        FirebaseUser venue = firebaseAuth.getCurrentUser();
        if(venue!= null){
            email = venue.getEmail();
            uid = venue.getUid();
        }
    }
}