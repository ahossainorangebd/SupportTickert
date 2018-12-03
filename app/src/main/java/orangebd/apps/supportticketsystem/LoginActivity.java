package orangebd.apps.supportticketsystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private ArrayList<DetailDataModel> detailList=new ArrayList<>();


    private RecyclerView.Adapter adapter;

    private RecyclerView recyclerView;

    private Context context;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;




    public static String ID;
    public static String TITLE;
    public static String TICKETsTATE;



    /**
     * this is a temporary code  -->
     * */
    private EditText mEdtTxtEmail;
    private String mStrEmail;

    private EditText mEdtTxtPwd;
    private String mStrPwd;

    private HashMap<String, String> map;
    private HashMap<String, String> map2;

    private SessionManager sm;


    /**
     * <--  this is a temporary code
     * */

    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context=this;

        //recyclerView=findViewById(R.id.my_recycler_view);

       // sm=new SessionManager(this);

        isLogin=GlobalVar.gIsLogin;

        if(isLogin==true){
            HashMap<String,String> hashMap=sm.getUserDetails();
            String strEmail=hashMap.get("email");
            String strPwd=hashMap.get("password");

            map = new HashMap<String, String>();

            map.put("user_email",strEmail);
            map.put("user_password",strPwd);

            new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/login.php");
            finish();
        }

        /**
         * this is a temporary code -->
         * */

        mEdtTxtEmail=findViewById(R.id.email);
        mEdtTxtPwd=findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.regiPageSignUpId);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mStrEmail=mEdtTxtEmail.getText().toString();
                mStrPwd=mEdtTxtPwd.getText().toString();

                map = new HashMap<String, String>();

                //TODO//
              //  map.put("access_token","access");


                map.put("user_email",mStrEmail);
                map.put("user_password",mStrPwd);

                /**
                 * Login URL for hitting
                 * */
                new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/login.php");

//                new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/getUserTickets.php");

                //new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/statistics.php");

                /*if(mStrEmail.equals("admin")) {

                    GlobalVar.gIsAdmin=true;
                }*/

                /*if(mStrPwd.equalsIgnoreCase("pwd")) {

                    GlobalVar.gIsYouCanEnter=true;
                }*/

                /*if(GlobalVar.gIsAdmin==true) {
                    Intent i = new Intent(context, MainActivity.class);
                    v.getContext().startActivity(i);
                }*/

                /*else
                {
                    Intent i = new Intent(context, MainActivity.class);
                    v.getContext().startActivity(i);
                }*/

                /*Intent i = new Intent(context, MainActivity.class);
                v.getContext().startActivity(i);*/
            }
        });

        /**
         *  <--  this is a temporary code
         * */

        // Set up the login form.
        /*mEmailView = findViewById(R.id.email);*/

        /*populateAutoComplete();*/

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });


        Button mSignInButton = findViewById(R.id.signInBtnId);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SignUpActivity.class);
                v.getContext().startActivity(i);
            }
        });



        /*
        Button mSignInButton = findViewById(R.id.RegiPageLogInId);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MainActivity.class);
                v.getContext().startActivity(i);
            }
        });
        */



        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    /*private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

    /*private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UploadUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data= performPostCall(params[0],map);

            if (data != null) try {

                JSONObject jsonObj = new JSONObject(data);
                detailList=new ArrayList<DetailDataModel>();

                DetailDataModel model = new DetailDataModel();

                String login_success = jsonObj.getString("success");
                model.setmSuccessId(login_success);

                if(login_success.equalsIgnoreCase("1")) {

                    try {
                        for (int i=0;i<jsonObj.length()-1;i++)
                        {
                            JSONArray object = (JSONArray) jsonObj.get("data");
                            JSONObject object2 = (JSONObject) object.get(0);

                            int length=object.length();

                            //Iterator<String> temp = object.keys();
                            //while (temp.hasNext()) {

                            //String dynamicKey = (String) temp.next();


                            //for success login


                            //for User data
                            String Eid = object2.getString("id");
                            String Ename = object2.getString("user_name");
                            String Eemail = object2.getString("user_email");
                            String Emobile = object2.getString("user_mobile");
                            String Epassword = object2.getString("user_password");
                            String Edomain = object2.getString("user_domain");
                            String Etype = object2.getString("user_type");
                            String Estatus = object2.getString("status");

                            GlobalVar.gName=Ename;
                            GlobalVar.gMobile=Emobile;
                            GlobalVar.gEmail=Eemail;

                            model.setmUserId(Eid);
                            model.setmUserName(Ename);
                            model.setmUserEmail(Eemail);
                            model.setmUserMobile(Emobile);
                            model.setmUserPasword(Epassword);
                            model.setmUserDomain(Edomain);
                            model.setmUserType(Etype);
                            model.setmStatus(Estatus);

                            //for ticket Information
                        /*String tktId = object.getString("tn");
                        String tktsId = object.getString("id");
                        String ttl = object.getString("title");
                        String tState = object.getString("ticket_state_id");

                        model.setmTicketId(tktId);
                        model.setmTitle(ttl);
                        model.setmTicket_state_id(tState);
                        model.setmTicketids(tktsId);


                        String lSuccess = object.getString("success");
                        String lData = object.getString("data");

                        model.setmSuccessId(lSuccess);
                        model.setmDatas(lData);*/



                            //for Login success & data


                            detailList.add(model);
                            publishProgress();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
            catch (final JSONException e) {
                Log.e("tag", "Couldn't get json from server.");
            }
            else {
                Log.e("tag", "Couldn't get json from server.");
            }


            // String returnData=data.toString();
            if (data.equalsIgnoreCase("ok"))
                return "success";
            else
                return "";

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            GlobalVar.gArrayFromLoginPage=detailList;

            //TODO
            //progressDialog.dismiss();

            if (detailList.size()>0) {

                String mUserType= detailList.get(0).getmUserType();

                if (mUserType.equalsIgnoreCase("1")){

                    if(isLogin==false) {

                        sm = new SessionManager(context);
                        sm.createLoginSession(mStrEmail, mStrPwd);

                    }

                    GlobalVar.gMapFromLoginActivity=map;

                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("EFloginActvt", mStrEmail);
                    i.putExtra("isadmin", true);


                    startActivity(i);

                }
                else{
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("EFloginActvt", mStrEmail);
                    i.putExtra("isadmin", false);

                    startActivity(i);
                }








                //TODO//
                //  map.put("access_token","access");

                return;
            }

            else {
                mEdtTxtEmail.setError("Your email or password is incorrect");
                mEdtTxtEmail.requestFocus();

                return;
            }

            //TODO
            //for a Success attempt activity

           /* SessionManager sm=new SessionManager(getApplicationContext());
            sm.createLoginSession(mStrEmail,mStrPwd);*/

            /*String msg=result;*/

            /*if (msg.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(),"Welcome", Toast.LENGTH_LONG).show();
                *//*
                Intent i=new Intent(getApplicationContext(),thank_youActivity.class);
                startActivity(i);
                *//*
            }

            //TODO
            //for a Failed attempt activity

            else {

                Toast.makeText(getApplicationContext(),"Incorrect Password or Email", Toast.LENGTH_LONG).show();
                *//*
                Intent i=new Intent(getApplicationContext(),paymentActivity.class);
                startActivity(i);
                *//*
            }*/


            /**
             *   //TODO//
             * */

        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }

    }

    public String  performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }

            else {
                response="";
            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()) {

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

