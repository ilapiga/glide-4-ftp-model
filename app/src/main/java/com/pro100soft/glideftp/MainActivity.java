package com.pro100soft.glideftp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pro100soft.glideftp.GlideFTP.FTPModel;

public class MainActivity extends AppCompatActivity {

    static String FTP_ADDRESS = "example.com";
    static String FTP_USER = "user";
    static String FTP_PASS = "password";
    static String FTP_IMAGE_FILE_NAME = "sample.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLoadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(MainActivity.this, (ImageView)findViewById(R.id.imageView), FTP_IMAGE_FILE_NAME);
            }
        });
    }

    public static void loadImage(Activity activity, ImageView imageView, String fileName) {
        try {
            Glide.with(activity)
                    .load(new FTPModel(FTP_ADDRESS, null, FTP_USER, FTP_PASS, fileName))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_no_photo_light)
                    .error(R.drawable.ic_no_photo)
                    .fallback(R.drawable.ic_no_photo)
                    .into(imageView);
        }catch (Exception ex){
            imageView.setImageBitmap(drawableToBitmap(activity.getDrawable(R.drawable.ic_no_photo)));
        }
    }

    public static void loadImage(Fragment fragment, ImageView imageView, String fileName) {
        try {
            Glide.with(fragment)
                    .load(new FTPModel(FTP_ADDRESS, null, FTP_USER, FTP_PASS, fileName))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_no_photo_light)
                    .error(R.drawable.ic_no_photo)
                    .fallback(R.drawable.ic_no_photo)
                    .into(imageView);
        }catch (Exception ex){
            imageView.setImageBitmap(drawableToBitmap(fragment.getContext().getDrawable(R.drawable.ic_no_photo)));
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}