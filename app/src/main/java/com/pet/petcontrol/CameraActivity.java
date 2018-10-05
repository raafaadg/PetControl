package com.pet.petcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements ArquivoDialogListener {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    TextView tvImagem;
    File dir;
    FileOutputStream fileOutputStream;
    static String fileName;
    public static Bitmap photo;

    public static Bitmap getPhoto() {
        return photo;
    }

    public static String getFileName() {
        return fileName;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        tvImagem = findViewById(R.id.tvImagem);

        File filepath = Environment.getExternalStorageDirectory();
        dir = new File(filepath.getAbsolutePath(),"PetControl");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        tvImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askShare();
            }
        });
    }

    private void askShare() {
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.show(getSupportFragmentManager(), "Deseja Compartilhar a Adoção?");
    }

    @Override
    public void applyTexts(String escolha) {
        if(escolha.equals("share")) {
            startActivity(Intent.createChooser(new Share().Share(), "Share Image"));
            startActivity(new Intent(CameraActivity.this, CameraActivity.class));
        }
        else if(escolha.equals("main"))
            startActivity(new Intent(CameraActivity.this,MainActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo = (Bitmap) (data != null ? data.getExtras().get("data") : null);
        imageView.setImageBitmap(photo);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = simpleDateFormat.format(new Date());
        fileName = "Pet" + date + ".jpg";
        //String filefileName = dir.getAbsolutePath() + "/" + fileName;
        File newFile = new File(dir,fileName);

        try {
            fileOutputStream = new FileOutputStream(newFile);
            photo.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            addImageToGallery(newFile.getAbsolutePath(),this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(newFile)));
    }


    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


}
