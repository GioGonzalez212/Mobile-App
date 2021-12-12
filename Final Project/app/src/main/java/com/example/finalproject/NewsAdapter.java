package com.example.finalproject;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

// on below line we are creating our adapter class
// in this class we are passing our array list
// and our View Holder class which we have created.
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ArrayList<News> newsM;
    private Context context;

    public NewsAdapter(ArrayList<News> newsModals, Context context) {
        this.newsM = newsModals;
        this.context = context;
    }

    // below is the method to filter our list.
    public void filterList(ArrayList<News> filterllist) {
        // adding filtered list to our
        // array list and notifying data set changed
        newsM = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsAdapter.NewsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        // on below line we are setting data to our item of
        // recycler view and all its views.
        News modal = newsM.get(position);
        holder.nameTV.setText(modal.getName());
        holder.LastHourTV.setText("$ " + df2.format(modal.getLastHour()));
        holder.LastWeekTV.setText("$ " + df2.format(modal.getLastWeek()));
        holder.UpdateDateTV.setText(unix_to_human(modal.getUpdateDate()));
        holder.symbolTV.setText(modal.getSymbol());
    }

    @Override
    public int getItemCount() {
        // on below line we are returning
        // the size of our array list.
        return newsM.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView symbolTV, LastHourTV, nameTV, UpdateDateTV, LastWeekTV;
        private ImageButton imageTVbtn;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVName);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            LastHourTV = itemView.findViewById(R.id.idTVLastHour);
            LastWeekTV = itemView.findViewById(R.id.idTVLastWeek);
            UpdateDateTV = itemView.findViewById(R.id.idTVUpdateDate);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String unix_to_human(String value){
        //conver seconds to milliseconds
        Date date = new java.util.Date(Integer.parseInt(value)*1000L);
        //the format of the date

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm aa");
        // give a timezone reference for formatting (see comment at the bottom)
        String formattedDate = sdf.format(date);
        return formattedDate;

    }//end of unix_to_human
}
