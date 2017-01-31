package com.definityfirst.jesusgonzalez.agendaapp;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    contactAdapter adapter;
    List<Contacto> allcontacts;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
                lv = (ListView) findViewById(android.R.id.list);
                TextView emptyText = (TextView) findViewById(android.R.id.empty);
                lv.setEmptyView(emptyText);
                allcontacts = db.getAllContacts();
                adapter = new contactAdapter(this, R.layout.contactview, allcontacts);
                lv.setAdapter(adapter);
                registerForContextMenu(lv);



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.popuplist, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()) {
            case R.id.edit:

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialogoagregar, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
                alertDialogBuilderUserInput.setView(mView);
                final EditText addNombre = (EditText) mView.findViewById(R.id.dialogAddNombre);
                final EditText addNumero = (EditText) mView.findViewById(R.id.dialogAddNumero);
                final EditText addEmail = (EditText) mView.findViewById(R.id.dialogAddEmail);
                      addNombre.setText(adapter.getItem(info.position).getName());
                      addNumero.setText(adapter.getItem(info.position).getNumber());
                      addEmail.setText(adapter.getItem(info.position).getEmail());

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                        Contacto updated = adapter.getItem(info.position);
                                        updated.setName(addNombre.getText().toString());
                                        updated.setNumber(addNumero.getText().toString());
                                        updated.setEmail(addEmail.getText().toString());
                                        db.updateContact(updated);
                                        Toast.makeText(MainActivity.this, "Contacto Editado!", Toast.LENGTH_LONG).show();
                                        updateData();
                            }
                        })

                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                return true;
            case R.id.delete:

                        Contacto deleted = adapter.getItem(info.position);
                        db.deleteContact(deleted);
                        Toast.makeText(MainActivity.this, "Contacto Eliminado!", Toast.LENGTH_LONG).show();
                        updateData();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.toolbar_buscar);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setNotifyOnChange(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setNotifyOnChange(true);
                if (TextUtils.isEmpty(newText)) {
                    lv.clearTextFilter();
                    adapter.filter("");
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.toolbar_add) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialogoagregar, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);

            final EditText addNombre = (EditText) mView.findViewById(R.id.dialogAddNombre);
            final EditText addNumero = (EditText) mView.findViewById(R.id.dialogAddNumero);
            final EditText addEmail = (EditText) mView.findViewById(R.id.dialogAddEmail);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                                    db.addContact(new Contacto(addNombre.getText().toString(),addNumero.getText().toString(),addEmail.getText().toString()));
                                    Toast.makeText(MainActivity.this, "Contacto Agregado!", Toast.LENGTH_LONG).show();
                                    updateData();
                        }
                    })

                    .setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData() {
        List<Contacto> allcontacts = db.getAllContacts();
        adapter = new contactAdapter(this,R.layout.contactview,allcontacts);
        SearchView searchView = (SearchView) findViewById(R.id.toolbar_buscar);
        adapter.filter(searchView.getQuery().toString());

        lv.setAdapter(adapter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }



}
