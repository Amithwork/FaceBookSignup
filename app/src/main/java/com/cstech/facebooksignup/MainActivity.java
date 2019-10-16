package com.cstech.facebooksignup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenManager;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView name,email;
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenManager = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null){
                Toast.makeText(MainActivity.this, "Do Nothing", Toast.LENGTH_SHORT).show();
            }else{
                loadUserProfile(currentAccessToken);
            }

        }
    };

    public  void  loadUserProfile(AccessToken accessToken){
        GraphRequest graphRequest =GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String firstname = object.getString("first_name");
                    String lastname = object.getString("last_name");
                    String email1 = object.getString("email");
                    String id = object.getString("id");

                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    name.setText(firstname + " " + lastname);
                    email.setText(email1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle paramters = new Bundle();

        paramters.putString("fields","first_name,last_name,email,id");
        graphRequest.setParameters(paramters);
        graphRequest.executeAsync();

    }

    public  void  checkLoginStatus(){
        if (AccessToken.getCurrentAccessToken()!= null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    } 
}
