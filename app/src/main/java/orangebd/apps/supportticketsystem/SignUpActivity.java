package orangebd.apps.supportticketsystem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {

    //for spinner
    String selectedItem;

    private Button mSignUpButton;
    private Context context;

    //EditText section
    private EditText mEdtTxtDomain;
    private String mStrDomain;

    private EditText mEdtTxtUname;
    private String mStrUname;

    private EditText mEdtTxtCnumber;
    private String mStrCnumber;

    private EditText mEdtTxtEmail;
    private String mStrEmail;

    private EditText mEdtTxtPwd;
    private String mStrPwd;

    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context=this;


        //Dropdown List
       // Spinner mSpinner=findViewById(R.id.spnrOne);


        //Adding options
        /*
        final List<String> cc=new ArrayList<String>();
        cc.add("Select");
        cc.add("pmo.gov.bd");
        cc.add("bangladesh.gov.bd");
        cc.add("shyllet.bangladesh.gov.bd");
        cc.add("khulna.bangladesh.gov.bd");
        */

        /*
        //calling the spinner_item_layout
        ArrayAdapter<String> spinnerArrayAdapterCC = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, cc);
        spinnerArrayAdapterCC.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(spinnerArrayAdapterCC);
        */

        //catching and getting the text for EditText

        mEdtTxtDomain=findViewById(R.id.domainFieldId);        mStrDomain=mEdtTxtDomain.getText().toString();
        mEdtTxtUname=findViewById(R.id.nameBoxId);            mStrUname=mEdtTxtUname.getText().toString();
        mEdtTxtCnumber=findViewById(R.id.contactId);          mStrCnumber=mEdtTxtCnumber.getText().toString();
        mEdtTxtEmail=findViewById(R.id.email);              mStrEmail=mEdtTxtEmail.getText().toString();
        mEdtTxtPwd=findViewById(R.id.password);             mStrPwd=mEdtTxtPwd.getText().toString();

        mSignUpButton= findViewById(R.id.regiPageSignUpId);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: for validation
                //SetTextValue();

                map = new HashMap<String, String>();

                map.put("action","complete_submitting");

                map.put("user_domain",mStrDomain);
                map.put("user_name",mStrUname);
                map.put("user_contact",mStrCnumber);
                map.put("user_email",mStrEmail);
                map.put("user_password",mStrPwd);

                new UploadUserInfo().execute("http://app.support.ae/app_submit/user_registration.php");

                Intent i = new Intent(context, LoginActivity.class);
                v.getContext().startActivity(i);
            }
        });

        /*

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int item=position;
                selectedItem  =cc.get(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        */
    }

    public class UploadUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            String data= performPostCall(params[0],map);

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

            if (msg.equalsIgnoreCase("success")){

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


    //for compolsory fields
    /*private void SetTextValue(){

        strName=nameText.getText().toString();
        if (strName.equalsIgnoreCase("")) {
            nameText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strLastName=surnameText.getText().toString();
        if (strLastName.equalsIgnoreCase("")){
            surnameText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strEmail=emailText.getText().toString();
        if (strEmail.equalsIgnoreCase("")){
            emailText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strPassword=passwordText.getText().toString();
        if (strPassword.equalsIgnoreCase("")){
            passwordText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strPhone=phoneText.getText().toString();
        if (strPhone.equalsIgnoreCase("")){
            phoneText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strCity=cityText.getText().toString();
        if (strCity.equalsIgnoreCase("")){
            cityText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strRegion=regionText.getText().toString();
        if (strRegion.equalsIgnoreCase("")){
            regionText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strAddress=addressText.getText().toString();
        if (strAddress.equalsIgnoreCase("")){
            addressText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strZipCode=zipCodeText.getText().toString();
        if (strZipCode.equalsIgnoreCase("")){
            zipCodeText.setError("This field cannot be empty");
            nullError=true;
            return;
        }
        strSpecialRequest=specialRequestText.getText().toString();
        if (strSpecialRequest.equalsIgnoreCase("")){
            specialRequestText.setError("This field cannot be empty");
            nullError=true;
            return;
        }

        strAppointmentZipCode=appointmentZipCodeText.getText().toString();
        if (strAppointmentZipCode.equalsIgnoreCase("")){
            strAppointmentZipCode="";
        }
        strAppointmentCity=appointmentCityText.getText().toString();
        if (strAppointmentCity.equalsIgnoreCase("")){
            strAppointmentCity="";
        }
        strAppointmentState=appointmentStateText.getText().toString();
        if (strAppointmentState.equalsIgnoreCase("")){
            strAppointmentState="";
        }
        strAppointmentAddress=appointmentAddressText.getText().toString();
        if (strAppointmentAddress.equalsIgnoreCase("")){
            strAppointmentAddress="";
        }

        nullError=false;
    }*/
}
