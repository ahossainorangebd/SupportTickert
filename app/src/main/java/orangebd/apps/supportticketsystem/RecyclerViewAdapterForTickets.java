package orangebd.apps.supportticketsystem;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapterForTickets extends RecyclerView.Adapter<RecyclerViewAdapterForTickets.MyViewHolder>{

    private ArrayList<DetailDataModel> dataSet;
    private Context mContext;
    private String stringPath;

    private Typeface tf;

    private ArrayList<DetailDataModel> mFilteredList;

    //private String copyRightText;
    // private ImageView mainImage;

    private String mStatus;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewName2;
        TextView textViewName3;
        TextView textViewName4;

        ImageView imageViewIcon;
        Typeface typeface;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.txtViewSerialNo);
            this.textViewName2 = itemView.findViewById(R.id.txtViewSubject);
            this.textViewName3 = itemView.findViewById(R.id.txtViewDetailsid);
            //this.typeface=Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/SolaimanLipi.ttf");
            //textViewVersion.setTypeface(typeface);
        }
    }

    public RecyclerViewAdapterForTickets(ArrayList<DetailDataModel> data, Context context) {
        this.dataSet = data;
        this.mContext=context;
        stringPath = "file:///android_res/drawable/company_credit_logo.png";

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        TextView textViewVersion = holder.textViewName;
        TextView textViewVersion2 = holder.textViewName2;
        TextView textViewVersion3 = holder.textViewName3;

        final String Subject=dataSet.get(listPosition).getmTicketId();
        final String Subjects=dataSet.get(listPosition).getmTicketids();
        final String Details=dataSet.get(listPosition).getmTitle();
        final String Status=dataSet.get(listPosition).getmTicket_state_id();

        textViewVersion.setText(Subjects);
        textViewVersion2.setText(Details);


        switch (Status)
        {
            case "1":
                mStatus="New";

                break;

            case "2":
                mStatus="Closed successful";

                break;

            case "3":
                mStatus="closed unsuccessful";

                break;

            case "4":
                mStatus="Open";

                break;

            case "5":
                mStatus="Removed";

                break;

            case "6":
                mStatus="Pending reminder";

                break;

            case "7":

                mStatus="Pending auto close+";
                break;

            case "8":
                mStatus="Pending auto close-";

                break;

            case "9":
                mStatus="Merged";
                break;

        }

        textViewVersion3.setText(mStatus);

        //textViewVersion3.setText(Status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, TicketDetailActivity.class);
                i.putExtra("TicketNumber", Subjects);
               // i.putExtra("SURL", detailUrl);

                try {
                    v.getContext().startActivity(i);
                }
                catch (Exception ex){
                    String msg=ex.getMessage();
                    Log.d("msg",msg);
                }
            }
        });

    }
    private String convertEnglishDateToBengali(String englishDate) throws ParseException {
        // Initial date time in String forma†
        //String timeOrg = "Mon Apr 18 22:56:10 GMT+05:30 2016";
        //String timeOrg = "Mon Apr 18 22:56:10 GMT+05:30 2016";
        String timeOrg = englishDate;
        // Corresponding date format
        //2018-05-13 14:47:38
        String dateTimeFormatOrg ="yyyy-MM-dd HH:mm:ss"; //"EEE MMM dd hh:mm:ss z yyyy";
        // SimpleDateFormat using US locale to be able to parse "Mon Apr"
        SimpleDateFormat sdfgmtOrg = new SimpleDateFormat(dateTimeFormatOrg, Locale.US);
        // Parse the date
        Date time = sdfgmtOrg.parse(timeOrg);

        // Target date format
        String dateTimeFormat = "dd MMM yyyy hh:mm";
        // SimpleDateFormat using the target locale
        SimpleDateFormat sdfgmt = new SimpleDateFormat(dateTimeFormat, new Locale("bn","BD"));
        // Set the Time Zone to UTC
        sdfgmt.setTimeZone(TimeZone.getDefault());
        // Print the formatted date
        String returnDate=sdfgmt.format(time);
        //System.out.println(sdfgmt.format(time));
        return returnDate;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }
}
