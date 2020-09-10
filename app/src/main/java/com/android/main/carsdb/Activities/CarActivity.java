package com.android.main.carsdb.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.main.carsdb.App;
import com.android.main.carsdb.Data.CarsAppDatabase;
import com.android.main.carsdb.Model.Car;
import com.android.uraall.carsdbwithroomstartercode.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class CarActivity extends AppCompatActivity {

    private EditText mCarNameEditText;
    private EditText mCarPriceEditText;
    private Button mCarEditButton;
    private FloatingActionButton mFloatingActionButton;
    private boolean mIsUpdate;
    private Car mEditCar;

    private CarsAppDatabase mCarsAppDatabase = App.getAppDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        mCarNameEditText = findViewById(R.id.car_name_edit_text);
        mCarPriceEditText = findViewById(R.id.car_price_edit_text);
        mCarEditButton = findViewById(R.id.car_edit_button);
        mCarEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsUpdate) {
                    mEditCar.setName(mCarNameEditText.getText().toString());
                    mEditCar.setPrice(mCarPriceEditText.getText().toString());
                    updateCar(mEditCar);
                } else {
                    String name = mCarNameEditText.getText().toString();
                    String price = mCarPriceEditText.getText().toString();
                    mEditCar = new Car(name, price);
                    addCar(mEditCar);
                }
            }
        });
        mFloatingActionButton = findViewById(R.id.car_delete_floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogDeleteCar(mEditCar);
            }
        });

        Intent intent = getIntent();
        mIsUpdate = intent.getBooleanExtra("isUpdate", false);
        if (mIsUpdate) {
            // обновление слова в базе данных
            mEditCar = (Car) Objects.requireNonNull(getIntent().getExtras()).getSerializable(Car.class.getSimpleName());
            mCarEditButton.setText("Update");
            mCarNameEditText.setText(mEditCar.getName());
            mCarPriceEditText.setText(mEditCar.getPrice());
        } else {
            mFloatingActionButton.hide();
        }
    }

    public void startDialogDeleteCar(final Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete car");
        builder.setMessage("Do you really want to delete the car?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCar(car);
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

    private void addCar(final Car car) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mCarsAppDatabase.getCarDAO().addCar(car);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute();
    }

    private void updateCar(final Car car) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mCarsAppDatabase.getCarDAO().updateCar(car);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute();
    }

    private void deleteCar(final Car car) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mCarsAppDatabase.getCarDAO().deleteCar(car);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute();
    }
}
