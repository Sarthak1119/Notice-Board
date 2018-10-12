package starlabs.noticeboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment {
    ListView ls_me, ls_me_img,ls_me_pdf;
    int flag = 1;
    ArrayAdapter<String> adapter;
    List<String> listdata;
    List<Upload> uploadList;
    List<Upload> uploadListpdf;
    FirebaseDatabase FDB;
    DatabaseReference DBR, imageme,pdfme;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ls_me = (ListView) view.findViewById(R.id.ls_me);
        ls_me_img = (ListView) view.findViewById(R.id.ls_me_img);
        ls_me_pdf=(ListView) view.findViewById(R.id.ls_me_pdf);

        listdata = new ArrayList<String>();
        uploadList = new ArrayList<>();
        uploadListpdf=new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listdata);
        ls_me.setAdapter(adapter);
        ls_me_img.setAdapter(adapter);

        FDB = FirebaseDatabase.getInstance();
        imageme = FirebaseDatabase.getInstance().getReference().child("Images").child("ME");
        pdfme=FirebaseDatabase.getInstance().getReference().child("Pdf").child("ME");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://noticeboard-35b9a.appspot.com");
        DBR = FDB.getReference();
        DatabaseReference DBR2 = DBR.child("Notification").child("ME");
        DBR2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String temp = ds.child("Notice").child("textnotice").getValue(String.class);
                    listdata.add(temp);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageme.addValueEventListener(new ValueEventListener() {
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
                    ls_me_img.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pdfme.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadListpdf.add(upload);

                    String[] uploads = new String[uploadListpdf.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = uploadListpdf.get(i).getName();
                    }

                    //displaying it to list
                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, uploads);
                    ls_me_pdf.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ls_me_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialogpdf();
                flag=i;
            }
        });

        ls_me_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialog();
                flag = i;
            }
        });


        return view;
    }

    private void alertDialogpdf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        Upload upload = uploadListpdf.get(flag);

        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        startActivity(intent);
    }

    private void alertDialog() {
        //we van pass both MainActivity.this & getApplicationContext()
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


    void download() {
        Upload upload = uploadList.get(flag);

        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        startActivity(intent);
    }
}