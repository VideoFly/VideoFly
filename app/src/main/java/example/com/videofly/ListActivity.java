package example.com.videofly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {

    private ArrayList<Friend> friends;
    private FriendAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // init friends
        friends = new ArrayList<Friend>();
        friends.add(new Friend("Alex", 23, R.drawable.g_1));
        friends.add(new Friend("Julie", 2, R.drawable.g_1));
        friends.add(new Friend("Brian", 8, R.drawable.g_1));
        friends.add(new Friend("Irene", 5, R.drawable.g_1));


        ListView listView = (ListView) findViewById(R.id.listView);

        listAdapter = new FriendAdapter(
                this, friends);

        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListActivity.this, FriendActivity.class);
                intent.putExtra("name", friends.get(i).getName());
                startActivity(intent);

            }
        });

    }

}