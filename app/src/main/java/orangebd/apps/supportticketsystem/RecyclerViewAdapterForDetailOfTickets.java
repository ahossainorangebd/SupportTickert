package orangebd.apps.supportticketsystem;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapterForDetailOfTickets extends RecyclerView.Adapter<RecyclerViewAdapterForDetailOfTickets.MyViewHolder>{

    private ArrayList<DetailDataModel> dataSet;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewName2;
        TextView textViewName3;
        TextView textViewName4;
        TextView textViewName5;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.ticketNoId);
            this.textViewName2 = itemView.findViewById(R.id.errorDetailsId);
            this.textViewName3 = itemView.findViewById(R.id.txtView3);
            this.textViewName4 = itemView.findViewById(R.id.txtView4);
            this.textViewName5 = itemView.findViewById(R.id.txtView5);
        }
    }

    public RecyclerViewAdapterForDetailOfTickets(ArrayList<DetailDataModel> data, Context context) {

        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_detail_activity, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        TextView textViewVersion = holder.textViewName;
        TextView textViewVersion2 = holder.textViewName2;
        TextView textViewVersion3 = holder.textViewName3;
        TextView textViewVersion4 = holder.textViewName4;
        TextView textViewVersion5 = holder.textViewName5;

        final String Subject=dataSet.get(listPosition).getmSubject();
        final String mFrom=dataSet.get(listPosition).getmFrom();
        final String mTo=dataSet.get(listPosition).getmTo();
        final String mBody=dataSet.get(listPosition).getmBody();
        final String mTime=dataSet.get(listPosition).getmIncomingTime();

        textViewVersion.setText(Subject);
        textViewVersion2.setText(mFrom);
        textViewVersion3.setText(mTo);
        textViewVersion4.setText(mBody);
        textViewVersion5.setText(mTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*Intent i = new Intent(mContext, TicketDetailActivity.class);
               // i.putExtra("TicketNumber", Subject);
               // i.putExtra("SURL", detailUrl);


                try {
                    v.getContext().startActivity(i);
                }
                catch (Exception ex){
                    String msg=ex.getMessage();
                    Log.d("msg",msg);
                }*/
            }
        });

    }

    private String convertEnglishDateToBengali(String englishDate) throws ParseException {
        // Initial date time in String forma†
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
