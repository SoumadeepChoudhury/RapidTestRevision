package com.example.rapidtestrevision;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ListView list;
    DBCon DB;
    EditText date;
    EditText physics;
    EditText chemistry;
    EditText botany;
    EditText zoology;
    EditText mistake;
    EditText mistakeReasonShow;
    ArrayList<String> values=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list = findViewById(R.id.list);
        mistakeReasonShow = findViewById(R.id.MistakeReason);
        DB=new DBCon(this);
        Cursor res=DB.getData();
        if (res.getCount()!=0) {
            while (res.moveToNext()) {
                String value = res.getString(1).substring(0, 5) + "    " + res.getString(2) + "        " + res.getString(3) + "        " + res.getString(4) + "      " + res.getString(5) + "    " + (Integer.parseInt(res.getString(2)) + Integer.parseInt(res.getString(3)) + Integer.parseInt(res.getString(4)) + Integer.parseInt(res.getString(5)));
                try{
                mistakeReasonShow.setText(res.getString(6));}
                catch (Exception e){
                    mistakeReasonShow.setText("No Mistake Encountered..");
                }
                values.add(value);
            }
        }
        adapter=new ArrayAdapter<String>(MainActivity.this, R.layout.listitemtext,values);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert1=new AlertDialog.Builder(MainActivity.this);
                View view1 =getLayoutInflater().inflate(R.layout.custom_dialog,null);
                date=(EditText) view1.findViewById(R.id.editDate);
                physics=(EditText) view1.findViewById(R.id.editPhysics);
                chemistry=(EditText) view1.findViewById(R.id.editChemistry);
                botany=(EditText) view1.findViewById(R.id.editBotany);
                zoology=(EditText) view1.findViewById(R.id.editZoology);
                mistake=(EditText) view1.findViewById(R.id.mistakeReason);
                Button ins=(Button) view1.findViewById(R.id.button);
                String saved_data=(String)list.getItemAtPosition(i);
                String Mod_data="";
                for(int pos=0;pos<saved_data.length()-3;pos++){
                    if(saved_data.charAt(pos) != 32){
                        Mod_data+=saved_data.charAt(pos);
                    }
                }
                final String PV=Mod_data;
                Cursor mod_value=DB.getDatabyPV(PV);
                String UserDate="";
                int UserP=0;
                int UserC=0;
                int UserB=0;
                int UserZ=0;
                String UserM="";
                Log.d(PV,"");
                if(mod_value.getCount()>0){
                    Log.d(mod_value.getCount()+"","");
                    while (mod_value.moveToNext()){
                        Log.d(mod_value.getString(1).toString(),"");
                        UserDate=mod_value.getString(1).toString();
                        UserP=Integer.parseInt(mod_value.getString(2).toString());
                        UserC=Integer.parseInt(mod_value.getString(3).toString());
                        UserB=Integer.parseInt(mod_value.getString(4).toString());
                        UserZ=Integer.parseInt(mod_value.getString(5).toString());
                        UserM=mod_value.getString(6).toString();
                    }
                    Log.d(UserDate+UserP+UserC+UserB+UserZ+UserM,"");
                    date.setText(UserDate);
                    physics.setText(UserP+"");
                    chemistry.setText(UserC+"");
                    botany.setText(UserB+"");
                    zoology.setText(UserZ+"");
                    mistake.setText(UserM);
                }
                alert1.setView(view1);
                final AlertDialog ad1=alert1.create();
                ad1.setCanceledOnTouchOutside(false);

                ins.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update_data(PV,i);
//                        ad1.dismiss();
                        ad1.cancel();
                    }
                });

                alert1.show();
            }
        });


    }

    public void show_input(View view){
        AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
        View view1 =getLayoutInflater().inflate(R.layout.custom_dialog,null);
        date=(EditText) view1.findViewById(R.id.editDate);
        physics=(EditText) view1.findViewById(R.id.editPhysics);
        chemistry=(EditText) view1.findViewById(R.id.editChemistry);
        botany=(EditText) view1.findViewById(R.id.editBotany);
        zoology=(EditText) view1.findViewById(R.id.editZoology);
        mistake=(EditText) view1.findViewById(R.id.mistakeReason);
        Button ins=(Button) view1.findViewById(R.id.button);

        alert.setView(view1);
        final AlertDialog ad=alert.create();
        ad.setCanceledOnTouchOutside(false);

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_data();
                ad.dismiss();
            }
        });

        alert.show();
    }
    
    public void insert_data(){
        try {
            String date = this.date.getText().toString();
            int physics = Integer.parseInt(this.physics.getText().toString());
            int chemistry = Integer.parseInt(this.chemistry.getText().toString());
            int botany = Integer.parseInt(this.botany.getText().toString());
            int zoology = Integer.parseInt(this.zoology.getText().toString());
            String mistake = this.mistake.getText().toString();
            boolean result = DB.insertData(date, physics, chemistry, botany, zoology, mistake);
            if (result == true) {
                if(mistake==null || mistake=="")
                    mistakeReasonShow.setText("No Mistake Encountered..");
                else
                    mistakeReasonShow.setText(mistake);
                Toast.makeText(this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                String value = date.substring(0, 5) + "    " + physics + "        " + chemistry + "        " + botany + "      " + zoology + "    " + (physics + chemistry + botany + zoology);
                values.add(value);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Error Occurred..Try Again Later", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"Error encountered.."+e.toString(),Toast.LENGTH_SHORT).show();

        }
    }

    public void update_data(String PV,int i){
        try {
            String date = this.date.getText().toString();
            int physics = Integer.parseInt(this.physics.getText().toString());
            int chemistry = Integer.parseInt(this.chemistry.getText().toString());
            int botany = Integer.parseInt(this.botany.getText().toString());
            int zoology = Integer.parseInt(this.zoology.getText().toString());
            String mistake = this.mistake.getText().toString();
            String PV1= date.substring(0,5)+physics+chemistry+botany+zoology;
            mistake=mistake.trim();
            if(mistake!=""||mistake!=null){
                mistakeReasonShow.setText(mistake);
            }
            boolean result = DB.updateData(PV,PV1, date, physics, chemistry, botany, zoology, mistake);
            if (result == true) {
                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                String value = date.substring(0, 5) + "    " + physics + "        " + chemistry + "        " + botany + "      " + zoology + "    " + (physics + chemistry + botany + zoology);
//            values.add(value);
                values.set(i, value);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Error Occurred..Try Again Later", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"Error encountered.."+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}