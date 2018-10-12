package starlabs.noticeboard;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class EeActivity extends AppCompatActivity {
    EditText et_ee,et_jpg,et_pdf;
    Button btn_ee,btn_speech;
    TextView choose_pdf,upload_pdf,choose_jpg,upload_jpg;
    ImageView imageView;
    FirebaseDatabase database;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 11;
    private final int PICK_PDF_CODE=72;
    private final int REQ_CODE_SPEECH_INPUT=103;
    FirebaseStorage storage;
    StorageReference storageReference,imageref,pdfref;
    DatabaseReference refee,imagerefee,pdfrefee;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ee);
        btn_ee = (Button) findViewById(R.id.btn_ee);
        btn_speech=(Button) findViewById(R.id.btn_speech);
        et_ee = (EditText) findViewById(R.id.et_ee);
        choose_pdf = (TextView) findViewById(R.id.choose_pdf);
        upload_pdf = (TextView) findViewById(R.id.upload_pdf);
        et_jpg = (EditText) findViewById(R.id.et_img);
        et_pdf=(EditText) findViewById(R.id.et_pdf);
        choose_jpg = (TextView) findViewById(R.id.choose_jpg);
        upload_jpg = (TextView) findViewById(R.id.upload_img);
        imageView = (ImageView) findViewById(R.id.img);
        progressBar=(ProgressBar) findViewById(R.id.pb);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refee = database.getReference("Notification");
        imagerefee=database.getReference("Images");
        pdfrefee=database.getReference("Pdf");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://noticeboard-35b9a.appspot.com");
        btn_ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notice = et_ee.getText().toString();
                Upload up=new Upload(notice);
                if (!TextUtils.isEmpty(notice)) {
                    refee.child("EE").push().child("Notice").setValue(up);
                    Toast.makeText(EeActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    et_ee.setText("");
                }

            }
        });

        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speech();
            }
        });

        choose_jpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        upload_jpg.setOnClickListener(new View.OnClickListener() {
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


    private void uploadpdffiles(Uri filePath) {
        if(TextUtils.isEmpty(et_pdf.getText()))
        {
            Toast.makeText(this, "Enter File Name !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            pdfref = storageReference.child("EE Pdf").child(et_pdf.getText().toString());

            pdfref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EeActivity.this, "File Uploaded successfully", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(et_pdf.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            pdfrefee.child("EE").push().setValue(upload);
                            et_pdf.setText("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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


    private void uploadImage(Uri filePath) {
        if(TextUtils.isEmpty(et_jpg.getText()))
        {
            Toast.makeText(this, "Enter File Name !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            imageref = storageReference.child("Ee Images").child(et_jpg.getText().toString());

            imageref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EeActivity.this, "File Uploaded successfully", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(et_jpg.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            imagerefee.child("EE").push().setValue(upload);
                            imageView.setImageDrawable(null);
                            et_jpg.setText("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressBar.setVisibility(View.VISIBLE);



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
                && data != null && data.getData() != null)
        {
            filePath=data.getData();
            et_pdf.setText(filePath.getLastPathSegment());
        }

        else if(requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK
                && null !=data)
        {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            et_ee.setText(result.get(0));
        }
        else{
            Toast.makeText(EeActivity.this, "No file chosen", Toast.LENGTH_SHORT).show();
        }

    }
}

