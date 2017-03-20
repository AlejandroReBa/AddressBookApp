package com.example.alejandoreba.addressbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Contact> listContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listContact = new ArrayList<>();
    }


    private class Contact{
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String address;

        public Contact (String firstNameIn, String lastNameIn, String phoneNumberIn, String addressIn){
            this.firstName = firstNameIn;
            this.lastName = lastNameIn;
            this.phoneNumber = phoneNumberIn;
            this.address = addressIn;
        }

        public String getFirstName(){
           return this.firstName;
        }

        public String getLastName(){
            return this.lastName;
        }

        public String getPhoneNumber(){
            return this.phoneNumber;
        }

        public String getAddress(){
            return this.address;
        }


    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.save)
        {
            String savedText = "";
            for (Contact c : listContact) {
                savedText += c.getFirstName() + "," + c.getLastName() + "," + c.getPhoneNumber() + ",\"" + c.getAddress() + "\" \n";
            }
            try
            {
                PrintWriter pw =
                        new PrintWriter( new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/contacts.csv"));
                pw.println(savedText);
                pw.close(); // close the file to ensure data is flushed to file
            }
            catch(IOException e)
            {
                new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                        setPositiveButton("OK", null).show();
            }

            return true;
        }else if(item.getItemId() == R.id.load){
            try
            {
                FileReader fr = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/contacts.csv");
                BufferedReader reader = new BufferedReader(fr);
                String line = "";
                String loadedText = "";
                while((line = reader.readLine()) != null)
                {
                    String[] components = line.split(",");
                    if(components.length==4)
                    {
                        Contact currentContact = new Contact (components[0], components[1], components[2], components[3]);
                        listContact.add(currentContact);
                    }
                }
                reader.close();
            }
            catch(IOException e)
            {
                new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                        setPositiveButton("OK", null).show();

            }
            return true;
        }else if (item.getItemId() == R.id.add){
            String firstNameText = ((EditText)findViewById(R.id.firstname)).getText().toString();
            String lastNameText = ((EditText)findViewById(R.id.lastname)).getText().toString();
            String phoneNumberText = ((EditText)findViewById(R.id.phonenumber)).getText().toString();
            String addressText = ((EditText)findViewById(R.id.address)).getText().toString();

            this.listContact.add(new Contact(firstNameText, lastNameText, phoneNumberText, addressText));

        }else if (item.getItemId() == R.id.search){
            String lastNameText = ((EditText)findViewById(R.id.lastname)).toString();
            boolean find = false;
            int index = 0;
            while (!find && index<this.listContact.size()) {
                Contact contact = this.listContact.get(index);
                if (contact.getLastName().equals(lastNameText)) {
                    find = true;
                    ((EditText) findViewById(R.id.firstname)).setText(contact.getFirstName());
                    System.out.println("-------------->" + contact.getFirstName());
                    ((EditText) findViewById(R.id.phonenumber)).setText(contact.getPhoneNumber());
                    ((EditText) findViewById(R.id.address)).setText(contact.getAddress());
                }else{
                    index++;
                }
            }

        }
        return false;
    }


}