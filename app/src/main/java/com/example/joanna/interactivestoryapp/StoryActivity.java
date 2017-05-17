package com.example.joanna.interactivestoryapp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joanna.interactivestoryapp.R;
import com.example.joanna.interactivestoryapp.model.Page;
import com.example.joanna.interactivestoryapp.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {
    private static final String TAG=StoryActivity.class.getSimpleName();
    private String name;
    private Story story;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;
    private Stack<Integer> pageStack = new Stack<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImageView=(ImageView)findViewById(R.id.storyImageView);
        storyTextView=(TextView) findViewById(R.id.storyTextView);
        choice1Button=(Button)findViewById(R.id.choice1Button);
        choice2Button=(Button)findViewById(R.id.choice2Button);

        //get data
        Intent intent=getIntent();
        name=intent.getStringExtra(getString(R.string.Key_name));
        if(name==null||name.isEmpty()){
            name="name is empty";
        }
        Log.d(TAG,name);
        story=new Story();
        loadPage(0);
    }

    private void loadPage(int pageNo) {
        pageStack.push(pageNo);
        final Page page=story.getPage(pageNo);
        Drawable image= ContextCompat.getDrawable(this,page.getImageId());
        storyImageView.setImageDrawable(image);
        String pageText=getString(page.getTextId());
        //add name if placeholder included,won't add if not
        pageText=String.format(pageText,name);
        storyTextView.setText(pageText);

        if(page.isFinalPage()){
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText(R.string.play_again_button);
            //for the final page return the beginning
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  finish();
                    loadPage(0);
                }
            });
        }else {
            loadButtons(page);
        }
    }

    private void loadButtons(final Page page) {
        choice1Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice1().getNextPage();
                loadPage(nextPage);
            }
        });

        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice2().getNextPage();
                loadPage(nextPage);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //remove the top page;
        pageStack.pop();
        if(pageStack.isEmpty()) {
            super.onBackPressed();
        }else {
            loadPage(pageStack.pop());
        }
    }
}
