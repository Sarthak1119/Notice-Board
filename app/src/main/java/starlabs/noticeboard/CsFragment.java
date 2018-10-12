package starlabs.noticeboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CsFragment extends Fragment {
    ListView ls_cs,ls_cs_img,ls_cs_pdf;
    int flag =1;
    ArrayAdapter<String> adapter;
    List<String> listdata;
    List<Upload> uploadlistpdf;
    List<Upload> uploadList;
    FirebaseDatabase FDB;
    DatabaseReference DBR,imagecs,pdfcs;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_cs, container, false);
        ls_cs = (ListView) view.findViewById(R.id.ls_cs);
        ls_cs_img=(ListView) view.findViewById(R.id.ls_cs_img);
        ls_cs_pdf=(ListView)view.findViewById(R.id.ls_cs_pdf);

        listdata = new ArrayList<String>();
        uploadList=new ArrayList<>();
        uploadlistpdf=new ArrayList<>();
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listdata);
        ls_cs.setAdapter(adapter);
        ls_cs_img.setAdapter(adapter);

        FDB = FirebaseDatabase.getInstance();
        imagecs=FirebaseDatabase.getInstance().getReference().child("Images").child("CS");
        pdfcs=FirebaseDatabase.getInstance().getReference().child("Pdf").child("CS");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://noticeboard-35b9a.appspot.com");
        DBR = FDB.getReference();
        DatabaseReference DBR2 = DBR.child("Notification").child("CS");
        DBR2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                  String temp=ds.child("Notice").child("textnotice").getValue(String.class);
                  listdata.add(temp);
                  adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imagecs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);

                    String[] uploads = new String[uploadList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = uploadList.get(i).getName();
                    }

                    //displaying it to list
                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, uploads);
                    ls_cs_img.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pdfcs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadlistpdf.add(upload);

                    String[] uploads = new String[uploadlistpdf.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = uploadlistpdf.get(i).getName();
                    }

                    //displaying it to list
                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, uploads);
                    ls_cs_pdf.setAdapter(adapter);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ls_cs_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialogpdf();
                flag=i;
            }
        });

        ls_cs_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialog();
                flag=i;
            }
        });


        return view;
    }

    private void alertDialogpdf() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Download");
        builder.setCancelable(false);
        builder.setMessage("Do you want to download ?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the app
                //or you can perform anything
                dialogInterface.dismiss();

                downloadpdf();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void downloadpdf() {
        Upload uploadpdf=uploadlistpdf.get(flag);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uploadpdf.getUrl()));
        startActivity(intent);
    }

    private void alertDialog() {
        //we van pass both MainActivity.this & getApplicationContext()
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Download");
        builder.setCancelable(false);
        builder.setMessage("Do you want to download ?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the app
                //or you can perform anything
                dialogInterface.dismiss();

                download();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }


    void download(){
        Upload upload = uploadList.get(flag);
        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        startActivity(intent);
    }


}

