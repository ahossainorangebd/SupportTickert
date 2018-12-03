package orangebd.apps.supportticketsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ArrayList<DetailDataModel> detailList=new ArrayList<>();

    private static RecyclerView.Adapter adapter;
    public static Context context;

    private static RecyclerView recyclerView;

    private LinearLayout testButton3;
    private LinearLayout testButton1;
    private LinearLayout testButton2;

    private RelativeLayout statisticsBtnLayout;


    public static String ID;
    public static String TITLE;
    public static String TICKETsTATE;

    SessionManager sm;

    private HashMap<String, String> map2;

    private String emailFromLoginActvt;

    private boolean isAdminCheck;

    private HashMap<String, String> mapFromLogin;

    private boolean isRedirectFromLoginAct;

    private TextView userNameTxtView;
    private TextView userEmailTxtView;
    private TextView userContactTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_logodetails, null, false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#800080")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isAdminCheck = getIntent().getExtras().getBoolean("isadmin");

        View navView = LayoutInflater.from(context).inflate(R.layout.nav_header_main, null, false);

        userNameTxtView=navView.findViewById(R.id.userNameId);
        userContactTxtView=navView.findViewById(R.id.textView);
        userEmailTxtView=navView.findViewById(R.id.contactTextViewId);

        userNameTxtView.setText(GlobalVar.gName);
        userContactTxtView.setText(GlobalVar.gMobile);
        userEmailTxtView.setText(GlobalVar.gEmail);

        //sm=new SessionManager(this);
       // sm.checkLogin();

        android.support.design.widget.FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddTicketActivity.class);
                view.getContext().startActivity(i);
            }
        });

        recyclerView=findViewById(R.id.my_recycler_view);

        statisticsBtnLayout=findViewById(R.id.statisticsBtnLayoutId);

        if (isAdminCheck==true) {

            statisticsBtnLayout.setVisibility(View.VISIBLE);

        }
        else
            statisticsBtnLayout.setVisibility(View.GONE);

        testButton3=findViewById(R.id.totalDoneTicketBtn3);
        testButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"আপনার নিস্পন্ন টিকেট সংখ্যা ৭৬ টি",Toast.LENGTH_LONG).show();
            }
        });

        testButton2=findViewById(R.id.totalDoneTicketBtn2);
        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"আপনার চলমান টিকেট সংখ্যা ৮৯ টি",Toast.LENGTH_LONG).show();
            }
        });

        testButton1=findViewById(R.id.totalDoneTicketBtn1);
        testButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"আপনার মোট টিকেট সংখ্যা ২৩৪ টি",Toast.LENGTH_LONG).show();
            }
        });

        //FloatingActionButton actionC = new FloatingActionButton(getBaseContext());

        /*

        FloatingActionButton actionA = findViewById(R.id.action_a);
        actionA.setIcon(R.drawable.plusicon);
        actionA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddTicketActivity.class);
                view.getContext().startActivity(i);

            }
        });

        FloatingActionButton actionB = findViewById(R.id.action_b);
        actionB.setIcon(R.drawable.plusicon);

        */

        //final FloatingActionsMenu menuMultipleActions = findViewById(R.id.multiple_actions);

    //  menuMultipleActions.addButton(actionA);
      //  menuMultipleActions.addButton(actionB);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        emailFromLoginActvt = getIntent().getExtras().getString("EFloginActvt");

        mapFromLogin=GlobalVar.gMapFromLoginActivity;
        SessionManager sm=new SessionManager(context);

        isRedirectFromLoginAct=sm.checkLogin();

        map2 = new HashMap<String, String>();
        map2.put("user_email", emailFromLoginActvt);

        new LoadUsersInfo().execute("http://114.130.54.74/otrs_monitoring/api/loadUserTickets.php");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.logOutId) {

            isAdminCheck=false;

            sm=new SessionManager(context);

            sm.logoutUser();

            finish();


        }

        if (id == R.id.addTicketId) {

            Intent i = new Intent(context, AddTicketActivity.class);
            context.startActivity(i);
        }
        //else if (id==R.id.nav_share)
        //    shareIt();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contactId) {

        }
        else if (id == R.id.addTicketId) {

            Intent i = new Intent(context, AddTicketActivity.class);
            context.startActivity(i);

        }
        else if (id == R.id.ViewTicketId) {

        }
        /*else if (id == R.id.feedbackid) {

        }*/
        else if (id == R.id.logOutId) {

            isAdminCheck=false;

            sm=new SessionManager(context);

            sm.logoutUser();

            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LoadUsersInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            if(isRedirectFromLoginAct==true){
                map2=mapFromLogin;
            }

            String data= performPostCall(params[0],map2);

            if (data != null) try {

                JSONArray jsonObj = new JSONArray(data);
                detailList=new ArrayList<DetailDataModel>();

                try {
                    for (int i=0;i<jsonObj.length()-1;i++)
                    {
                        JSONObject object = (JSONObject) jsonObj.getJSONObject(i);

                        DetailDataModel model = new DetailDataModel();

                        //for ticket information
                        String tktId = object.getString("tn");
                        String tktsId = object.getString("id");
                        String ttl = object.getString("title");
                        String tState = object.getString("ticket_state_id");

                        model.setmTicketId(tktId);
                        model.setmTitle(ttl);
                        model.setmTicket_state_id(tState);
                        model.setmTicketids(tktsId);

                        detailList.add(model);
                        publishProgress();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter=new RecyclerViewAdapterForTickets(detailList,context);
            recyclerView.setAdapter(adapter);
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
