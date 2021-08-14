package com.example.cars_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.provider.Car_entity;

import java.util.ArrayList;
import java.util.List;

public class CarRecyclerAdapter extends RecyclerView.Adapter<CarRecyclerAdapter.ViewHolder> {
    String position ;

    private List<Car_entity> mCars ;
    public void setmCars(List<Car_entity> mCars){
        this.mCars = mCars;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMaker.setText(mCars.get(position).getMaker());
        holder.tvModel.setText(mCars.get(position).getModel());
        holder.tvColor.setText(mCars.get(position).getColor());
        holder.tvPrice.setText(String.valueOf(mCars.get(position).getPrice()));
        holder.tvSeats.setText(String.valueOf(mCars.get(position).getSeats()));
        holder.tvYear.setText(String.valueOf(mCars.get(position).getYear()));
    }

    @Override
    public int getItemCount() {
        if (mCars == null)
            return 0;
        else
            return mCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvMaker ;
        private TextView tvModel ;
        private TextView tvYear ;
        private TextView tvColor ;
        private TextView tvSeats ;
        private TextView tvPrice ;
        public CardView mCardView;
        public View mView;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            mView = itemView;

            tvMaker = itemView.findViewById(R.id.carMaker);
            tvYear = itemView.findViewById(R.id.carYear);
            tvModel = itemView.findViewById(R.id.carModel);
            tvSeats = itemView.findViewById(R.id.carSeat);
            tvPrice = itemView.findViewById(R.id.carPrice);
            tvColor = itemView.findViewById(R.id.carColor);
            mCardView = itemView.findViewById(R.id.card_view);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = "Position " + getAdapterPosition();
                    Toast.makeText(v.getContext(),(position),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }





}
