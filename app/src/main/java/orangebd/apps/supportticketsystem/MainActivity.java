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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
    private HashMap<String, String> map3;

    private String emailFromLoginActvt;
    private String nameFromLoginActvt;
    private String contactFromLoginActvt;

    private boolean isAdminCheck;

    private HashMap<String, String> mapFromLogin;

    private boolean isRedirectFromLoginAct;

    private TextView userNameTxtView;
    private TextView userEmailTxtView;
    private TextView userContactTxtView;



    private TextView mOpenTicketBtnText;
    private TextView mClosedTicketBtnText;
    private TextView mTotalTicketBtnText;


    //For the three button texts
    private String mOpenT;
    private String mClosedT;
    private String mTotalT;

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

//        View navView = LayoutInflater.from(context).inflate(R.layout.nav_header_main, null, false);
        NavigationView nView= findViewById(R.id.nav_view);
        View headerView = nView.getHeaderView(0);

        userNameTxtView=headerView.findViewById(R.id.userNameId);
        userContactTxtView=headerView.findViewById(R.id.textView);
        userEmailTxtView=headerView.findViewById(R.id.contactTextViewId);

        //sm=new SessionManager(this);
       // sm.checkLogin();

        emailFromLoginActvt = getIntent().getExtras().getString("EFloginActvt");
        nameFromLoginActvt = getIntent().getExtras().getString("NFloginActvt");
        contactFromLoginActvt = getIntent().getExtras().getString("CFloginActvt");


        userNameTxtView.setText(GlobalVar.gUserNameFromSplash);
        userContactTxtView.setText(GlobalVar.gContactFromSplash);
        userEmailTxtView.setText(GlobalVar.gEmailFromSplash);
        //userEmailTxtView.setText(emailFromLoginActvt);

        android.support.design.widget.FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddTicketActivity.class);
                i.putExtra("EFmainActvt", emailFromLoginActvt);
                view.getContext().startActivity(i);
            }
        });

        recyclerView=findViewById(R.id.my_recycler_view);

        statisticsBtnLayout=findViewById(R.id.statisticsBtnLayoutId);

        if (isAdminCheck==true) {

            statisticsBtnLayout.setVisibility(View.VISIBLE);

            GlobalVar.gIsAdminForStatistics=true;
        }
        else {
            statisticsBtnLayout.setVisibility(View.GONE);
            GlobalVar.gIsAdminForStatistics=false;
        }

        testButton1=findViewById(R.id.totalDoneTicketBtn1);
        testButton2=findViewById(R.id.totalDoneTicketBtn2);
        testButton3=findViewById(R.id.totalDoneTicketBtn3);

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

        mapFromLogin=GlobalVar.gMapFromLoginActivity;
        SessionManager sm=new SessionManager(context);

        isRedirectFromLoginAct=sm.checkLogin();

        map2 = new HashMap<String, String>();
        map2.put("user_email", emailFromLoginActvt);

        if(GlobalVar.gIsAdminForStatistics==true){
            new LoadStatisticsInfo().execute("http://114.130.54.74/otrs_monitoring/api/statistics.php");
        }
        else
            new LoadUsersInfo().execute("http://114.130.54.74/otrs_monitoring/api/loadUserTickets.php");

        mTotalTicketBtnText=findViewById(R.id.totalTicketId);
        mOpenTicketBtnText=findViewById(R.id.openTicketId);
        mClosedTicketBtnText=findViewById(R.id.closedTicketId);


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
            i.putExtra("EFmainActvt", emailFromLoginActvt);
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
            i.putExtra("EFmainActvt", emailFromLoginActvt);
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

    public class LoadStatisticsInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            map3= new HashMap<>();

            String data= performPostCall(params[0],map3);

            if (data != null) try {

                JSONObject jsonObj = new JSONObject(data);
                detailList=new ArrayList<DetailDataModel>();

                DetailDataModel model = new DetailDataModel();

                String openTicketNumber = jsonObj.getString("open");
                String closedTicketNumber = jsonObj.getString("closed");
                String totalTicketNumber = jsonObj.getString("total");
                model.setmOpenTicketNumber(openTicketNumber);
                model.setmClosedTicketNumber(closedTicketNumber);
                model.setmTotalTicketNumber(totalTicketNumber);

                detailList.add(model);

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

            mOpenT = convetEngToBn(detailList.get(0).getmOpenTicketNumber());
            mClosedT = convetEngToBn(detailList.get(0).getmClosedTicketNumber());
            mTotalT = convetEngToBn(detailList.get(0).getmTotalTicketNumber());


            testButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this,"আপনার মোট টিকেট সংখ্যা " + mTotalT + " টি",Toast.LENGTH_LONG).show();
                }
            });

            testButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this,"আপনার চলমান টিকেট সংখ্যা " + mOpenT + " টি",Toast.LENGTH_LONG).show();
                }
            });

            testButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this, "আপনার নিস্পন্ন টিকেট সংখ্যা " + mClosedT + " টি" ,Toast.LENGTH_LONG).show();
                }
            });


            mTotalTicketBtnText.setText(mTotalT);
            mOpenTicketBtnText.setText(mOpenT);
            mClosedTicketBtnText.setText(mClosedT);

            new LoadUsersInfo().execute("http://114.130.54.74/otrs_monitoring/api/loadUserTickets.php");
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
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


    public String convetEngToBn(String num){

        num = num.replace("0","০");
        num = num.replace("1","১");
        num = num.replace("2","২");
        num = num.replace("3","৩");
        num = num.replace("4","৪");
        num = num.replace("5","৫");
        num = num.replace("6","৬");
        num = num.replace("7","৭");
        num = num.replace("8","৮");
        num = num.replace("9","৯");

        return num;
    }
}
