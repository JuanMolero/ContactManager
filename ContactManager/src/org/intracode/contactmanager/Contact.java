package org.intracode.contactmanager;

import android.net.Uri;

/**
 * Created by Juan Molero on 23.07.13.
 */
public class Contact {

    private String _nombre, _telefono, _email, _direccion;
    private Uri _imageURI;
    private int _id;

    public Contact (int id, String nombre, String telefono, String email, String direccion, Uri imageURI) {
        _id = id;
        _nombre = nombre;
        _telefono = telefono;
        _email = email;
        _direccion = direccion;
        _imageURI = imageURI;
    }

    public int getId() { return _id; }

    public String getNombre() {
        return _nombre;
    }

    public String getTelefono() {
        return _telefono;
    }

    public String getEmail() {
        return _email;
    }

    public String getDireccion() {
        return _direccion;
    }

    public Uri getImageURI() { return _imageURI; }
}
