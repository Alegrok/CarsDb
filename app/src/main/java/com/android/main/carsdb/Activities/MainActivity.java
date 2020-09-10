package com.android.main.carsdb.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.android.main.carsdb.Adapter.CarsAdapter;
import com.android.main.carsdb.App;
import com.android.main.carsdb.Data.CarsAppDatabase;
import com.android.main.carsdb.Model.Car;
import com.android.main.carsdb.Utils.Util;
import com.android.uraall.carsdbwithroomstartercode.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CarsAdapter mCarsAdapter;
    private ArrayList<Car> mCarArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton mFloatingActionButton;

    private CarsAppDatabase mCarsAppDatabase = App.getAppDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        new GetAllCarsAsyncTask().execute();

        mCarsAdapter = new CarsAdapter(this, mCarArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCarsAdapter);

        mFloatingActionButton = findViewById(R.id.create_car_floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CarActivity.class);
                intent.putExtra("isUpdate", false);
                startActivityForResult(intent, Util.ADD_CAR_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Util.ADD_CAR_REQUEST_CODE || requestCode == Util.UPDATE_CAR_REQUEST_CODE)
                && resultCode == RESULT_OK) {
            loadCars();
        }
    }

    private void loadCars() {
        new AsyncTask<Void, Void, List<Car>>() {
            @Override
            protected List<Car> doInBackground(Void... voids) {
                return mCarsAppDatabase.getCarDAO().getAllCars();
            }

            @Override
            protected void onPostExecute(List<Car> cars) {
                mCarsAdapter.setCarArrayList(cars);
            }
        }.execute();
    }

    public void startDialogDeleteCar(final Car car, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete car");
        builder.setMessage("Do you really want to delete the car?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCar(car, position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addCar(String name, String price) {
        new CreateCarAsyncTask().execute(new Car(0, name , price));
    }

    private void deleteCar(Car car, int position) {
        mCarArrayList.remove(position);

        new DeleteCarAsyncTask().execute(car);

        mCarsAdapter.notifyDataSetChanged();
    }

    private void updateCar(String name, String price, int position) {
        Car car = mCarArrayList.get(position);
        car.setName(name);
        car.setPrice(price);

        new UpdateCarAsyncTask().execute(car);

        mCarArrayList.set(position, car);
        mCarsAdapter.notifyDataSetChanged();
    }

    private class GetAllCarsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mCarArrayList.addAll(mCarsAppDatabase.getCarDAO().getAllCars());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCarsAdapter.notifyDataSetChanged();
        }
    }

    private class CreateCarAsyncTask extends AsyncTask<Car, Void, Void> {

        @Override
        protected Void doInBackground(Car... cars) {
            long id = mCarsAppDatabase.getCarDAO().addCar(cars[0]);
            Car car = mCarsAppDatabase.getCarDAO().getCar(id);
            if (car != null) {
                mCarArrayList.add(0, car);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCarsAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateCarAsyncTask extends AsyncTask<Car, Void, Void> {

        @Override
        protected Void doInBackground(Car... cars) {
            mCarsAppDatabase.getCarDAO().updateCar(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCarsAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteCarAsyncTask extends AsyncTask<Car, Void, Void> {

        @Override
        protected Void doInBackground(Car... cars) {
            mCarsAppDatabase.getCarDAO().deleteCar(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCarsAdapter.notifyDataSetChanged();
        }
    }
}
