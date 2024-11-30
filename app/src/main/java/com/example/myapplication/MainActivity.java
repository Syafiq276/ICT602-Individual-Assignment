package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button CalcBill;
    EditText etAmount, etRebate;
    TextView tvOutput,tvOutput2;
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize views
        etAmount = findViewById(R.id.etAmount);
        etRebate = findViewById(R.id.etRebate);
        CalcBill = findViewById(R.id.CalcBill);
        tvOutput = findViewById(R.id.tvOutput);
        tvOutput2 = findViewById(R.id.tvOutput2);

        // Set up toolbar
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        // Enable Edge to Edge layout
        EdgeToEdge.enable(this);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listener for button
        CalcBill.setOnClickListener(v -> calculateBill());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if(item.getItemId() == R.id.item_Home){
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (item.getItemId() == R.id.item_About) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);

        }
        return false;
    }

    private void calculateBill() {
        try {
            // Get inputs
            double units = Double.parseDouble(etAmount.getText().toString());
            double rebatePercentage = Double.parseDouble(etRebate.getText().toString());

            // Validate rebate percentage
            if (rebatePercentage < 0 || rebatePercentage > 5) {
                tvOutput2.setText("Rebate percentage must be between 0% and 5%.");
                return;
            }

            // Calculate the bill
            double bill = 0;
            if (units <= 200) {
                bill = units * 0.218;
            } else if (units <= 300) {
                bill = (200 * 0.218) + ((units - 200) * 0.334);
            } else if (units <= 600) {
                bill = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
            } else {
                bill = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
            }

            // Apply rebate
            double rebate = (rebatePercentage / 100) * bill;
            double finalBill = bill - rebate;

            // Display the result
            tvOutput2.setText(String.format("Total Bill: RM %.2f \nRebate: RM %.2f\nFinal Bill: RM %.2f",
                    bill, rebate, finalBill));
        } catch (NumberFormatException e) {
            tvOutput2.setText("Please enter valid numbers for units and rebate.");
        }
    }
}
