package com.android.main.carsdb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.android.main.carsdb.Activities.CarActivity;
import com.android.main.carsdb.Activities.MainActivity;
import com.android.main.carsdb.Model.Car;
import com.android.main.carsdb.Utils.Util;
import com.android.uraall.carsdbwithroomstartercode.R;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> mCarArrayList;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView priceTextView;


        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
        }
    }

    public void setCarArrayList(List<Car> carArrayList) {
        if (!mCarArrayList.isEmpty()) mCarArrayList.clear();
        mCarArrayList.addAll(carArrayList);
        notifyDataSetChanged();
    }

    public CarsAdapter(Context context, ArrayList<Car> cars, MainActivity mainActivity) {
        this.context = context;
        this.mCarArrayList = cars;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Car car = mCarArrayList.get(position);

        holder.nameTextView.setText(car.getName());
        holder.priceTextView.setText(car.getPrice() + " $");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, CarActivity.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra(Car.class.getSimpleName(), car);
                mainActivity.startActivityForResult(intent, Util.UPDATE_CAR_REQUEST_CODE);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivity.startDialogDeleteCar(car, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {

        return mCarArrayList.size();
    }
}
