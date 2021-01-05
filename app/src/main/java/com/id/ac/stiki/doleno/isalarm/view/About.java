package com.id.ac.stiki.doleno.isalarm.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.id.ac.stiki.doleno.isalarm.R;

public class About extends AppCompatActivity {
    ImageButton btn_stiki, btn_gmail, btn_instagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btn_stiki = (ImageButton) findViewById(R.id.btn_stiki);
        btn_gmail = (ImageButton) findViewById(R.id.btn_gmail);
        btn_instagram = (ImageButton) findViewById(R.id.btn_instagram);

        btn_stiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://stiki.ac.id"));
                i.setPackage("org.mozilla.fenix");
                startActivity(i);
            }
        });

        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://instagram.com/ilhamsagitap"));
                i.setPackage("com.instagram.android");
                startActivity(i);
            }
        });

        btn_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ilham.sagitaputra@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });
    }
}