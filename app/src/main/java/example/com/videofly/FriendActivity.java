package example.com.videofly;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.util.ArrayList;


public class FriendActivity extends ActionBarActivity {

    private ArrayList<Friend> friends;
    private FriendAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String name = this.getIntent().getStringExtra("name");
        TextView text = (TextView) findViewById(R.id.friendNameText);
        text.setText(name);

    }




}