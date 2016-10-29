package com.aexyn.facebooklogindemo;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * First of all we need to create a project at facebook developer site
 * use below link for create app at facebook developer site
 * https://developers.facebook.com/docs/facebook-login/android
 * <p>
 * App level Gradle file :- compile 'com.facebook.android:facebook-android-sdk:4.15.0'
 * <p>
 * After creating app at fb developer site, we can get our app key. Copy and store it in strings files
 * <string name="facebook_app_id">942122885853240</string>
 * And then go to manifest and add meta data block
 * <meta-data
 * android:name="com.facebook.sdk.ApplicationId"
 * android:value="@string/facebook_app_id" />
 * <p>
 * Fb provide their own facebook login button
 * copy it and paste in activity xml file
 * <p>
 * put all the code written here as it is to the desired class when needed
 */

public class MainActivity extends AppCompatActivity {

    /* For Facebook */
    private CallbackManager mCallbackManager;
    private LoginButton login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions("user_friends");
        login_button.registerCallback(mCallbackManager, mCallback);
        login_button.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            Log.e("LoginActivity Response ", response.toString());

                            try {

                                String Name = object.getString("name");
                                String Email = object.getString("email");
                                String Id = object.getString("id");
                                Log.e("Full Info : ", Name + " / " + Email + " / " + Id);

                                // * here we can call our web service for login using facebook credentials like email, name, etc * //

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
