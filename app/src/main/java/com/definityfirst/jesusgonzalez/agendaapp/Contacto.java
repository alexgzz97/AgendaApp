package com.definityfirst.jesusgonzalez.agendaapp;

/**
 * Created by jesus.gonzalez on 27/01/2017.
 */
public class Contacto {

    //private variables
    private int id;
    private String nombre;
    private String numero;
    private String email;

    // Empty constructor
    public Contacto(){

    }
    // constructor
    public Contacto(int id, String nombre, String numero, String email){
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.email = email;
    }

    // constructor
    public Contacto(String nombre, String numero, String email){
        this.nombre = nombre;
        this.numero = numero;
        this.email = email;    }
    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.nombre;
    }

    // setting name
    public void setName(String nombre){
        this.nombre = nombre;
    }

    // getting phone number
    public String getNumber(){
        return this.numero;
    }

    // setting phone number
    public void setNumber(String numero){
        this.numero = numero;
    }
    // getting email
    public String getEmail(){
        return this.email;
    }

    // setting email
    public void setEmail(String email){
        this.email = email;
    }
}
