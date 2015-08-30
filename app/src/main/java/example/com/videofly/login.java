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

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class login extends AppCompatActivity{

    private ImageButton mLoginButton;
    private final String LOGTAG = "LoginActivity";

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

        List<String> permissions = Arrays.asList("public_profile","user_friends", "email");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            Set<String> declinedPermission = null;

            @Override
            public void done(ParseUser user, ParseException err) {
                mLoginButton.setEnabled(true);
                declinedPermission = AccessToken.getCurrentAccessToken().getDeclinedPermissions();
                if (user == null) {
                    Log.d(LOGTAG, "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(getApplicationContext(), "Unable to Login! Try Again", Toast.LENGTH_SHORT).show();
                }
                else if (user.isNew()) {
                    if (declinedPermission != null) {
                        if (declinedPermission.size() > 0)
                            showPermissionsInvalidDialog();
                    }
                    else {
                        Log.d(LOGTAG, "User signed up and logged in through Facebook!");
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    if (declinedPermission.size() > 0) {
                        Log.d(LOGTAG, "declined permission is not null");
                        Log.d(LOGTAG, "declined permission size: " + declinedPermission.size());
                        showPermissionsInvalidDialog();
                    }
                    else{
                        Log.d(LOGTAG, "declined permission size was not greater then 0");
                        Log.d(LOGTAG, "declined permission size: " + declinedPermission.size());
                        Log.d(LOGTAG, "User logged in through Facebook!");
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                }
            }
        });

    }

    private void showPermissionsInvalidDialog(){
        new MaterialDialog.Builder(this)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }

                })
                .title("Uh Oh! We need Permissions")
                .content("Please make sure all permission are granted!")
                .positiveText("Ok")
                .autoDismiss(true)
                .show();
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
