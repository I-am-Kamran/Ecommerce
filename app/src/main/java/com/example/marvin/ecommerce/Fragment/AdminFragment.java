package com.example.marvin.ecommerce.Fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marvin.ecommerce.Activity.Constants;
import com.example.marvin.ecommerce.Model.Upload;
import com.example.marvin.ecommerce.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment implements View.OnClickListener
{
    private static final int PICK_IMAGE_REQUEST=234;
    private ImageView imageChoose;
    private EditText edtName,edtPrice,edtQuantity,edtDescription;
    private Button btnUpload,btnChoose;
    private Uri filepath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private Toolbar toolbarAdmin;


    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin, container, false);

        imageChoose=view.findViewById(R.id.imageChooser);
        edtName=view.findViewById(R.id.nameChooser);
        edtPrice=view.findViewById(R.id.priceChooser);
        edtQuantity=view.findViewById(R.id.quantity);
        edtDescription=view.findViewById(R.id.description);

        btnChoose=view.findViewById(R.id.Button_choose);
        btnUpload=view.findViewById(R.id.Button_upload);

        /**Toolbar**/
        toolbarAdmin=view.findViewById(R.id.toolbar_adminFragment);
        toolbarAdmin.setTitle("Admin");
        toolbarAdmin.setTitleTextColor(Color.WHITE);
        toolbarAdmin.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbarAdmin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.frame,new ShowDatabaseFragment());
                ft.commit();
            }
        });



        storageReference= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        btnUpload.setOnClickListener(this);
        btnChoose.setOnClickListener(this);






        return view;
    }

    @Override
    public void onClick(View view)
    {
        int id=view.getId();

        switch (id)
        {
            case R.id.Button_choose:
                showFileChooser();
                break;

            case R.id.Button_upload:
                uploadFile();
                break;
        }
    }

    private void uploadFile()
    {
        if (filepath!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //Getting the storage reference
            StorageReference sRef =storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filepath));

            //adding the file to reference
            sRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "File Upload Successfully!!", Toast.LENGTH_SHORT).show();

                    String uploadId=mDatabase.push().getKey();
                    //creating the upload object to store uploaded image details
                    Upload upload=new Upload();

                    upload.setProductId(uploadId);
                    upload.setName(edtName.getText().toString().trim());
                    upload.setPrice(edtPrice.getText().toString().trim());
                    upload.setPhotoUrl(taskSnapshot.getDownloadUrl().toString().trim());
                    upload.setQuantity(edtQuantity.getText().toString().trim());
                    upload.setDescription(edtDescription.getText().toString().trim());


                    mDatabase.child(uploadId).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded"+ ((int)progress)+ " %...");
                }
            });

        }
    }
    public String getFileExtension(Uri filepath)
    {
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(filepath));
    }

    private void showFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //start our activity and give the result & start onActivityResult()
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filepath=data.getData();
            try
            {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filepath);
                imageChoose.setImageBitmap(bitmap);
                Toast.makeText(getActivity(), "set image successfully", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){e.printStackTrace();}
        }
        else
        {
            Toast.makeText(getActivity(), "Select Picture", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}
