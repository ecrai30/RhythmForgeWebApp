package com.example.d308vacationsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationsapp.R;
import com.example.d308vacationsapp.database.Repository;
import com.example.d308vacationsapp.entities.Excursion;
import com.example.d308vacationsapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    String name;
    double price;
    int vacationID;

    EditText editName;
    EditText editPrice;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        vacationID = getIntent().getIntExtra("id",-1);
        editName=findViewById(R.id.vacationname);
        editPrice=findViewById(R.id.vacationprice);

        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price",0.0);
        editName.setText(name);
        editPrice.setText(Double.toString(price));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacID", vacationID);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationId() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacationdetails,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.vacationsave){
            Vacation vacation;
            if(vacationID==-1){
                if(repository.getmAllVacations().size()==0) vacationID=1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() -1).getVacationId() +1;
                vacation = new Vacation(vacationID,editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.insert(vacation);
                this.finish();
            }
            else{
                vacation = new Vacation(vacationID,editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.update(vacation);
                this.finish();
            }
        }
        return true;
    }
}