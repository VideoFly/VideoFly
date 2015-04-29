package example.com.videofly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class login extends AppCompatActivity{

    private ImageButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mLoginButton = (ImageButton) findViewById(R.id.fb_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLoginButton.setEnabled(false);
                userLogin();

            }
        });

    }

    private void userLogin() {
        List<String> permissions = Arrays.asList("user_friends", "email");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                mLoginButton.setEnabled(true);
                if (user == null) {
                    Log.d("login-activity", "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(getApplicationContext(), "Unable to Login! Try Again", Toast.LENGTH_SHORT).show();
                } else if (user.isNew()) {
                    Log.d("login-activity", "User signed up and logged in through Facebook!");
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.d("login-activity", "User logged in through Facebook!");
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.login:
                mLoginButton.setEnabled(false);
                userLogin();
            break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
