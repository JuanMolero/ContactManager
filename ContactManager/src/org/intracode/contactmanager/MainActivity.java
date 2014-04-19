package org.intracode.contactmanager;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText nombreTxt, telefonoTxt, emailTxt, direccionTxt;
    ImageView contactImageImgView;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageUri = Uri.parse("android.resource://org.intracode.contactmanager/drawable/no_user_logo.png");
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreTxt = (EditText) findViewById(R.id.txtNombre);
        telefonoTxt = (EditText) findViewById(R.id.txtTelefono);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        direccionTxt = (EditText) findViewById(R.id.txtDireccion);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        dbHandler = new DatabaseHandler(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creador");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creador");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("lista");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("Lista");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nombreTxt.getText()), String.valueOf(telefonoTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(direccionTxt.getText()), imageUri);
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    Toast.makeText(getApplicationContext(), String.valueOf(nombreTxt.getText()) + " se ha agregado a tus contactos!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(nombreTxt.getText()) + " ya existe. Por favor, use un nombre distinto.", Toast.LENGTH_SHORT).show();
            }
        });

        nombreTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(String.valueOf(nombreTxt.getText()).trim().length() > 0);
        }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }

            });

        if (dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts());

        populateList();
    }

    private boolean contactExists(Contact contact) {
        String nombre = contact.getNombre();
        int contactCount = Contacts.size();

        for (int i = 0; i < contactCount; i++) {
            if (nombre.compareToIgnoreCase(Contacts.get(i).getNombre()) == 0)
                return true;
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                contactImageImgView.setImageURI(data.getData());
            }
        }
    }

    private void populateList() {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super (MainActivity.this, R.layout.listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView nombre = (TextView) view.findViewById(R.id.contactNombre);
            nombre.setText(currentContact.getNombre());
            TextView telefono = (TextView) view.findViewById(R.id.phoneNumero);
            telefono.setText(currentContact.getTelefono());
            TextView email = (TextView) view.findViewById(R.id.emailDireccion);
            email.setText(currentContact.getEmail());
            TextView direccion = (TextView) view.findViewById(R.id.cDireccion);
            direccion.setText(currentContact.getDireccion());
            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.getImageURI());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
