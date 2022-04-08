package com.codebee.tradethrust.view.acitivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewImageActivity extends AppCompatActivity {

    @BindView(R.id.image_view)
    public ImageView imageView;

    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ActionBar actionBar = getSupportActionBar();

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();
        picasso = application.getPicasso();

        actionBar.hide();

        String imagePath = getIntent().getStringExtra("path");

        ButterKnife.bind(this);

        File imageFile = new File(imagePath);

        if(imageFile.exists()) {
            picasso.load(imageFile)
                    .into(imageView);
        }else {

            if(!imagePath.startsWith("http")) {
                imagePath = "http://" + imagePath;
            }
            picasso.load(imagePath)
                    .into(imageView);
        }

    }

    @OnClick(R.id.close_btn)
    public void closeActivity(View view) {
        super.onBackPressed();
    }
}
