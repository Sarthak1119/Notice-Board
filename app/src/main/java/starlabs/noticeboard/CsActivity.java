package starlabs.noticeboard;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class CsActivity extends AppCompatActivity {
    EditText et_cs, et_pdf, et_img;
    Button btn_cs,btn_speech;
    TextView choose_pdf, upload_pdf, btnchoose_img, btnupload_img;
    ImageView imageView;
    FirebaseDatabase database;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_PDF_CODE=11;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    FirebaseStorage storage;
    StorageReference storageReference,imageref,pdfref;
    DatabaseReference refcs,imagerefcs,pdfrefcs;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);
        btn_cs = (Button) findViewById(R.id.btn_cs);
        btn_speech=(Button)findViewById(R.id.btn_speech);
        et_cs = (EditText) findViewById(R.id.et_cs);
        choose_pdf = (TextView) findViewById(R.id.choose_pdf);
        upload_pdf = (TextView) findViewById(R.id.upload_pdf);
        et_pdf = (EditText) findViewById(R.id.et_pdf);
        et_img = (EditText) findViewById(R.id.et_img);
        btnchoose_img = (TextView) findViewById(R.id.choose_jpg);
        btnupload_img = (TextView) findViewById(R.id.upload_img);
        imageView = (ImageView) findViewById(R.id.img);
        progressBar=(ProgressBar)findViewById(R.id.pb);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refcs = database.getReference("Notification");
        imagerefcs=database.getReference("Images");
        pdfrefcs=database.getReference("Pdf");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://noticeboard-35b9a.appspot.com");


        btn_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notice = et_cs.getText().toString();
                Upload up=new Upload(notice);
                if (!TextUtils.isEmpty(notice)) {
                    refcs.child("CS").push().child("Notice").setValue(up);
                    Toast.makeText(CsActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    et_cs.setText("");
                }

            }
        });


        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                speech();

            }
        });




        btnchoose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnupload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(filePath);
            }
        });

        choose_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosepdf();
            }
        });

        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadpdffiles(filePath);
            }
        });

    }

    private void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void choosepdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_CODE);
    }


    private void uploadImage(Uri filePath) {
        if(TextUtils.isEmpty(et_img.getText()))
        {
            Toast.makeText(this, "Enter File Name !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            imageref = storageReference.child("Images").child(et_img.getText().toString());

            imageref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CsActivity.this, "File Uploaded successfully", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(et_img.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            imagerefcs.child("CS").push().setValue(upload);
                            imageView.setImageDrawable(null);
                            et_img.setText("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CsActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.incrementProgressBy(2);


                        }
                    });
        }


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        else if(requestCode == PICK_PDF_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath=data.getData();
            et_pdf.setText(filePath.getLastPathSegment());
        }

        else if(requestCode==REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && null != data)
        {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            et_cs.setText(result.get(0));
        }
        else{
            Toast.makeText(CsActivity.this, "No file chosen", Toast.LENGTH_SHORT).show();
        }

        }




    private void uploadpdffiles(Uri filePath) {
        if(TextUtils.isEmpty(et_pdf.getText()))
        {
            Toast.makeText(this, "Enter File Name !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            pdfref = storageReference.child("Pdf").child(et_pdf.getText().toString());

            pdfref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CsActivity.this, "File Uploaded successfully", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(et_pdf.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            pdfrefcs.child("CS").push().setValue(upload);
                            et_pdf.setText("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CsActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            progressBar.setVisibility(View.VISIBLE);



                        }
                    });
        }

    }
}


