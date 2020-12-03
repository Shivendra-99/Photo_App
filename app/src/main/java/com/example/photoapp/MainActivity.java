package com.example.photoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText input;
    TextView textView;
    Button choose,upload;
    upload uploads;
    ProgressBar progressBar;
    StorageReference storageReference;
    Uri uri;
    StorageTask storageTask;
    DatabaseReference databaseReference;
   final static int RC_PICK_PHOTO=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.image);
        input=(EditText)findViewById(R.id.enter_the_file);
        textView=(TextView)findViewById(R.id.show_all);
        choose=(Button)findViewById(R.id.choose_button);
        upload=(Button)findViewById(R.id.upload);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
      //  firebaseStorage=FirebaseStorage.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference("upload");
        databaseReference= FirebaseDatabase.getInstance().getReference("upload");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RC_PICK_PHOTO);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask!=null && storageTask.isInProgress())
                {
                    Toast.makeText(MainActivity.this,"Upload in progress please wait",Toast.LENGTH_LONG).show();
                    upload.setClickable(false);
                }
                else
                {
                    uploadToDatabse();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadedFile();

            }
        });
    }
    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_PICK_PHOTO && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uri=data.getData();
            Picasso.get().load(data.getData()).into(imageView);
        }
    }
    public void uploadToDatabse()
    {
        StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        UploadTask upload=reference.putFile(uri);
          upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       String url=uri.toString();
                        uploads=new upload(input.getText().toString(),url);
                        String uploadId=databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(uploads);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        },500);
                        Toast.makeText(MainActivity.this,"Upload is Done",Toast.LENGTH_LONG).show();
                        Log.d("Download url",url);
                    }
                });
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG);
              }
          }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                  double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                  progressBar.setProgress((int)progress);
              }
          });
    }
    private void uploadedFile()
    {
        Intent intent=new Intent(MainActivity.this,ShowImages.class);
        startActivity(intent);
    }
}