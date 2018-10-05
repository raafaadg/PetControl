package com.pet.petcontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class GalleryActivity extends AppCompatActivity implements ArquivoDialogListener {

    ImageView imageView;
    TextView tvImagem;

    private static final int GALLERY_REQUEST = 1889;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        tvImagem = findViewById(R.id.tvImagem);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

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
            startActivity(new Intent(GalleryActivity.this, CameraActivity.class));
        }
        else if(escolha.equals("main"))
            startActivity(new Intent(GalleryActivity.this,MainActivity.class));
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(GalleryActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(GalleryActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
