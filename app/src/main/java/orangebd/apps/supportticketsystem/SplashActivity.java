package orangebd.apps.supportticketsystem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    SessionManager sm;

    private Context context;

    private HashMap<String,String> map;

    private ArrayList<DetailDataModel> detailList;

    private String mStrEmail;
    private String mStrpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context=this;

        getSupportActionBar().hide();

        sm=new SessionManager(this);

        GlobalVar.gIsLogin= sm.checkLogin();

        if(GlobalVar.gIsLogin==true){
                HashMap<String,String> hashMap=sm.getUserDetails();
                String strEmail=hashMap.get("email");
                String strPwd=hashMap.get("password");

                map = new HashMap<String, String>();

                map.put("user_email",strEmail);
                map.put("user_password",strPwd);

                mStrEmail=strEmail;
                mStrpwd=strPwd;

                new UploadUserInfo().execute("http://114.130.54.74/otrs_monitoring/api/login.php");
        }
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

                    GlobalVar.gMapFromLoginActivity=map;

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
