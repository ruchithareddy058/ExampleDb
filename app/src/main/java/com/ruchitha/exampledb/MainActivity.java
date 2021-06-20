package com.ruchitha.exampledb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruchitha.exampledb.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
DatabaseReference reference;
ArrayList<MyModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        reference= FirebaseDatabase.getInstance().getReference("Data");
        list=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MyModel model=dataSnapshot.getValue(MyModel.class);
                    list.add(model);
                    MyAdapter myAdapter=new MyAdapter(MainActivity.this,list);
                    binding.rv.setAdapter(myAdapter);
                    binding.rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    binding.rv.addItemDecoration(new DividerItemDecoration
                            (MainActivity.this,DividerItemDecoration.VERTICAL));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void save(View view) {
        String sname=binding.name.getText().toString();
        String sroll=binding.roll.getText().toString();
        String snumber=binding.number.getText().toString();
        MyModel model=new MyModel(sname,sroll,snumber);
        //String id=reference.push().getKey();
        reference.child(sroll).setValue(model);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();

    }
}