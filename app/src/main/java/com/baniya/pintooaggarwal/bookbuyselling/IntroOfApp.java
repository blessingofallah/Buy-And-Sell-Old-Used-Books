package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroOfApp extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_intro_of_app);

        addSlide(AppIntroFragment.newInstance("Book-ऐ-Istan: Buy and Sell Used Books",
                "Buy And Sell Your Book",
                R.drawable.books,
                Color.parseColor("#51e2b7")));


        addSlide(AppIntroFragment.newInstance("Book-ऐ-Istan: Buy and Sell Used Books",
                "Donate Your Books",
                R.drawable.donate,
                Color.parseColor("#02B9C1")));



        addSlide(AppIntroFragment.newInstance("Book-ऐ-Istan: Buy and Sell Used Books",
                "Add your Book Details Easily",
                R.drawable.notebook,
                Color.parseColor("#8e8bf9")));

        showStatusBar(false);
        setBarColor(Color.parseColor("#ff390c"));
        setSeparatorColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this,StartingActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {

        startActivity(new Intent(this,StartingActivity.class));
        finish();

    }

    @Override
    public void onSlideChanged() {

    }
}

