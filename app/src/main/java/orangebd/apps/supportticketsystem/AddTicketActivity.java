package orangebd.apps.supportticketsystem;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AddTicketActivity extends AppCompatActivity {


    public Context context;

    /**
     * this is a temporary code  -->
     * */
    private EditText mEdtTxtSubject;
    private String mStrSubject;

    private EditText mEdtTxtDetails;
    private String mStrDetails;

    private HashMap<String, String> map;


    /**
     * <--  this is a temporary code
     * */


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //for camera
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    public static final int PICK_IMAGE = 2;
    public  static final int RequestPermissionCode  = 1;
    String contentPath;
    Bitmap uploadImage;

    String imageName;

    private Button loginForPwdMatched;


    //Splash Activities
    SessionManager sm;
    private HashMap<String,String> splashMap;
    private ArrayList<DetailDataModel> detailList;
    private String mStrEmail;
    private String mStrpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addticket);

        EnableRuntimePermissionToAccessCamera();

        context=this;

        View view = LayoutInflater.from(context).inflate(R.layout.custom_logodetails, null, false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#800080")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.imageView= this.findViewById(R.id.imagePictureId);


        /**
         *   this is a temporary code -->
         * */

        mEdtTxtSubject=findViewById(R.id.label3);
        mEdtTxtDetails=findViewById(R.id.textDetails);

        /**
         *  <--  this is a temporary code
         * */

        Button photoButton = this.findViewById(R.id.cameraModeId);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // imageView.setVisibility(View.VISIBLE);
                /*
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                */

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        /*
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        */

        Button mLogInButton = findViewById(R.id.loginForPwdMatch);
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Ticket added successfully", Toast.LENGTH_LONG).show();

                mStrSubject=mEdtTxtSubject.getText().toString();
                mStrDetails=mEdtTxtDetails.getText().toString();

                map = new HashMap<String, String>();

                map.put("action","complete_adding");

                map.put("user_subject",mStrSubject);
                map.put("user_details",mStrDetails);


                /*new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/userAddTicket.php");*/

                Intent i = new Intent(context, MainActivity.class);

                if(GlobalVar.gIsAdminForStatistics==true){
                    i.putExtra("isadmin", true);
                }
                else
                    i.putExtra("isadmin", false);

                v.getContext().startActivity(i);
            }
        });


        Button browseButton=this.findViewById(R.id.attachFileId);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_IMAGE);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        /*sm=new SessionManager(this);

        GlobalVar.gIsLogin= sm.checkLogin();

        if(GlobalVar.gIsLogin==true){
            HashMap<String,String> hashMap=sm.getUserDetails();
            String strEmail=hashMap.get("email");
            String strPwd=hashMap.get("password");

            splashMap = new HashMap<String, String>();

            splashMap.put("user_email",strEmail);
            splashMap.put("user_password",strPwd);

            mStrEmail=strEmail;
            mStrpwd=strPwd;

            new SplashActivities().execute("http://114.130.54.74/otrs_monitoring/api/login.php");
        }*/


    }

    private boolean mayRequestContacts() {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        //else if (id==R.id.nav_share)
        //    shareIt();
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);
        }

        else if (requestCode == PICK_IMAGE && resultCode== RESULT_OK){
            if( data != null) {
                Uri selectedUri = data.getData();
                String[] columns = { MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.MIME_TYPE };

                Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
                cursor.moveToFirst();

                int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
                int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );
                try {
                    uploadImage=MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imageUri=getPath(selectedUri);
                contentPath = cursor.getString(pathColumnIndex);
                String[] imagePath=contentPath.split("/");
                int arrayLength=imagePath.length;
                imageName=imagePath[arrayLength-1];
                String mimeType    = cursor.getString(mimeTypeColumnIndex);
                cursor.close();

                if(mimeType.startsWith("image")) {
                    Bitmap bm=null;
                    if (data != null) {
                        try {
                            bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bm);
                }
            }

        }
    }


    public void EnableRuntimePermissionToAccessCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)){
            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }




    public class UploadUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data= performPostCall(params[0],map);

            // String returnData=data.toString();
            if (data.equalsIgnoreCase("ok"))
                return "success";
            else
                return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //TODO
            //progressDialog.dismiss();

            //TODO
            //for a Success attempt activity

            String msg=result;

            if (msg.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(),"User Information Uploaded Successfully", Toast.LENGTH_LONG).show();
                /*
                Intent i=new Intent(getApplicationContext(),thank_youActivity.class);
                startActivity(i);
                */
            }

            //TODO
            //for a Failed attempt activity

            else {

                Toast.makeText(getApplicationContext(),"Failed to Upload User Information", Toast.LENGTH_LONG).show();
                /*
                Intent i=new Intent(getApplicationContext(),paymentActivity.class);
                startActivity(i);
                */
            }
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



    /*public class SplashActivities extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data= performPostCall(params[0],splashMap);

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

                            model.setmUserId(Eid);
                            model.setmUserName(Ename);
                            model.setmUserEmail(Eemail);
                            model.setmUserMobile(Emobile);
                            model.setmUserPasword(Epassword);
                            model.setmUserDomain(Edomain);
                            model.setmUserType(Etype);
                            model.setmStatus(Estatus);

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

                    if(GlobalVar.gIsLogin==false) {

                        sm = new SessionManager(context);
                        sm.createLoginSession(mStrEmail, mStrpwd);

                    }

                    GlobalVar.gMapFromLoginActivity=splashMap;

                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("EFloginActvt", mStrEmail);
                    i.putExtra("isadmin", true);

                    startActivity(i);
                    finish();

                }
                else{
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("EFloginActvt", mStrEmail);
                    i.putExtra("isadmin", false);

                    startActivity(i);
                    finish();
                }

                return;
            }

        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }

    }*/




}

