package com.socialone.android.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.inject.Inject;
import com.parse.signpost.commonshttp.CommonsHttpOAuthConsumer;
import com.parse.signpost.commonshttp.CommonsHttpOAuthProvider;
import com.socialone.android.R;
import com.socialone.android.activity.AppNetAuthActivity;
import com.socialone.android.appnet.adnlib.AppDotNetClient;
import com.socialone.android.appnet.adnlib.data.Token;
import com.socialone.android.appnet.adnlib.data.User;
import com.socialone.android.appnet.adnlib.response.LoginResponseHandler;
import com.socialone.android.appnet.adnlib.response.UserResponseHandler;
import com.socialone.android.socialauth.FourSquareAuth;
import com.socialone.android.socialauth.GooglePlusAuth;
import com.socialone.android.socialauth.InstagramAuth;
import com.socialone.android.socialauth.LinkedinAuth;
import com.socialone.android.socialauth.LoginAdapter;
import com.socialone.android.socialauth.LoginListener;
import com.socialone.android.socialauth.MyspaceAuth;
import com.socialone.android.socialauth.TwitterAuth;
import com.socialone.android.utils.Constants;
import com.socialone.android.utils.Datastore;

import java.util.Arrays;



/**
 * Created by david.hodge on 11/4/13.
 */
public class SocialConnectFragment extends RoboSherlockFragmentActivity {

    LoginButton facebookBtn;
    Button twitterBtn;
    Button plusBtn;
    Button fourSquareBtn;
    Button myspaceBtn;
    Button appNetBtn;
    Button instagramBtn;
    Button tumblrBtn;
    Button linkedinBtn;
    Context mContext;

    private UiLifecycleHelper uiHelper;
    Session session;
    @Inject Datastore mDatastore;

    LoginAdapter facebookLoginAdapter;
    LoginAdapter twitterLoginAdapter;
    LoginAdapter plusLoginAdapter;
    LoginAdapter fourLoginAdapter;
    LoginAdapter myspaceLoginAdapter;
    LoginAdapter instagramAdapter;
    LoginAdapter linkedinAdapter;
//    JumblrClient jumblrClient;
    AppDotNetClient client;
    public static final String CONSUMER_KEY = "Get this from your Tumblr application settings page";
    public static final String CONSUMER_SECRET = "Get this from your Tumblr application settings page";
    private static CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    private static CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL, Constants.ACCESS_URL, Constants.AUTHORIZE_URL);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_connect_accounts);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mContext = this;
        setUpFacebook();
        setUpTwitter();
        setUpPlus();
        setUpFourSquare();
        setUpMyspace();
        setUpAppNet();
        setUpInstagram();
        setUpTumblr();
        setUpLinkedin();

        uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        });
        uiHelper.onCreate(savedInstanceState);

    }

    private void setUpFacebook(){
        facebookBtn = (LoginButton) findViewById(R.id.social_connect_facebook_btn);
        facebookBtn.setReadPermissions(
                Arrays.asList("user_photos", "read_stream", "user_status", "friends_photos",
                        "friends_status", "friends_birthday", "basic_info", "user_location"));

//        Session session = Utils.ensureFacebookSessionFromCache(getBaseContext());
        session = Session.getActiveSession();
        Request meRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    mDatastore.setUserLoggedInToFacebook(true);
                    Toast.makeText(SocialConnectFragment.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        meRequest.executeAndWait();

    }

    private void setUpTwitter(){
        twitterBtn = (Button) findViewById(R.id.social_connect_twitter_btn);
        twitterLoginAdapter = new TwitterAuth(mContext);
        twitterLoginAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Toast.makeText(SocialConnectFragment.this, "twitter connected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginAdapter.authorize();
            }
        });
    }

    private void setUpPlus(){
        plusBtn = (Button) findViewById(R.id.social_connect_plus_btn);
        plusLoginAdapter = new GooglePlusAuth(mContext);
        plusLoginAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusLoginAdapter.authorize();
            }
        });
    }

    private void setUpFourSquare(){
        fourSquareBtn = (Button) findViewById(R.id.social_connect_foursquare_btn);
        fourLoginAdapter = new FourSquareAuth(mContext);
        fourLoginAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
        fourSquareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourLoginAdapter.authorize();
            }
        });
    }

    private void setUpMyspace(){
        myspaceBtn = (Button) findViewById(R.id.social_connect_myspace_btn);
        myspaceLoginAdapter = new MyspaceAuth(mContext);
        myspaceLoginAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
        myspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myspaceLoginAdapter.authorize();
            }
        });
    }

    private void setUpAppNet(){
        appNetBtn = (Button) findViewById(R.id.social_connect_appnet_btn);
//        client = new AppDotNetClient("zza9bRtZ63UGNAxG965a3Kr2uUkmXAqr", "LV6NbFgtJpCmhh6VADnkcWXGHF4tj5eq");
        appNetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO switch to activity for result to help confirm to the user that auth worked
                startActivity(new Intent(mContext, AppNetAuthActivity.class));
//                client.authenticateWithPassword("david_hodge", "d121091d", "write_post", new LoginResponseHandler() {
//                    @Override
//                    public void onSuccess(String accessToken, Token token) {
////                        client.setToken(accessToken);
//                        welcomeAppNetUser();
//                    }
//                    @Override
//                    public void onError(Exception error) {
//                        super.onError(error);
//                        Log.e("app.net", error.toString());
//                    }
//                });
            }
        });
    }

    private void welcomeAppNetUser(){
        client.retrieveCurrentUser(new UserResponseHandler() {
            @Override
            public void onSuccess(User responseData) {
                Toast.makeText(mContext, "Welcome " + responseData.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpInstagram(){
        instagramBtn = (Button) findViewById(R.id.social_connect_instagram_btn);
        instagramAdapter = new InstagramAuth(mContext);
        instagramAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramAdapter.authorize();
            }
        });
    }

//    private void appNetComingSoon(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.app_net_coming_soon_message)
//                .setPositiveButton(R.string.dialog_vote, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(R.string.dialog_alright, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.setInverseBackgroundForced(true);
//        builder.create();
//        builder.show();
//    }

    private void setUpTumblr(){
        tumblrBtn = (Button) findViewById(R.id.social_connect_tumblr_btn);
//        jumblrClient = new JumblrClient(Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);

        tumblrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                jumblrClient.setToken(" ", " ");
//                User user = jumblrClient.user();
//                Toast.makeText(mContext, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tumblrAuth(){

    }

    private void setUpLinkedin(){
        linkedinBtn = (Button) findViewById(R.id.social_connect_linkdin_btn);
        linkedinAdapter = new LinkedinAuth(mContext);
        linkedinAdapter.setListener(new LoginListener() {
            @Override
            public void onComplete(Bundle bundle) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });

        linkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkedinAdapter.authorize();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.social_connect_menu, (com.actionbarsherlock.view.Menu) menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.connect_done:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        return true;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
//            getUserInfo();
        } else if (state.isClosed()) {
            Toast.makeText(this, "error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

}
