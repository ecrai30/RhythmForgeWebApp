package com.example.d308vacationsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationsapp.R;
import com.example.d308vacationsapp.database.Repository;
import com.example.d308vacationsapp.entities.Excursion;
import com.example.d308vacationsapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VacationList extends AppCompatActivity {
private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.sample){
            repository=new Repository(getApplication());
            //Toast.makeText(VacationList.this, "put in sample data", Toast.LENGTH_SHORT).show();

            Vacation vacation = new Vacation(0,"Brazil",2000.0);
            repository.insert(vacation);
            vacation = new Vacation(0,"Colombia",2000.0);
            repository.insert(vacation);
            Excursion excursion=new Excursion(0,"hiking",100.0,1);
            repository.insert(excursion);
            excursion=new Excursion(0,"Cycling",200.0,1);
            repository.insert(excursion);
            return true;

        }
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
}