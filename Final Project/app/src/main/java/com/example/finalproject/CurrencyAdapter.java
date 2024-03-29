package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

// on below line we are creating our adapter class
// in this class we are passing our array list
// and our View Holder class which we have created.
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewholder> {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ArrayList<Currency> currencyM;
    private Context context;
    int flag = 0;

    public CurrencyAdapter(ArrayList<Currency> currencyModals, Context context) {
        this.currencyM = currencyModals;
        this.context = context;
    }

    // below is the method to filter our list.
    public void filterList(ArrayList<Currency> filterllist) {
        // adding filtered list to our
        // array list and notifying data set changed
        currencyM = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyAdapter.CurrencyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CurrencyAdapter.CurrencyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.CurrencyViewholder holder, int position) {
        // on below line we are setting data to our item of
        // recycler view and all its views.
        Currency modal = currencyM.get(position);
        holder.nameTV.setText(modal.getName());
        holder.rateTV.setText("$ " + df2.format(modal.getPrice()));
        holder.symbolTV.setText(modal.getSymbol());

    }

    @Override
    public int getItemCount() {
        // on below line we are returning
        // the size of our array list.
        return currencyM.size();
    }

    // on below line we are creating our view holder class
    // which will be used to initialize each view of our layout file.
    public class CurrencyViewholder extends RecyclerView.ViewHolder {
        private TextView symbolTV, rateTV, nameTV;
        private ImageButton imageTVbtn;


        public CurrencyViewholder(@NonNull View itemView) {
            super(itemView);
            // on below line we are initializing all
            // our text views along with its ids.
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            rateTV = itemView.findViewById(R.id.idTVRate);
            nameTV = itemView.findViewById(R.id.idTVName);
            imageTVbtn = itemView.findViewById(R.id.idTVimagebtn);



            imageTVbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag == 0)
                    {
                        imageTVbtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                        flag = 1;
                    }
                    else if(flag == 1)
                    {
                        imageTVbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        flag = 0;
                    }

                }
            });

        }
    }
}
