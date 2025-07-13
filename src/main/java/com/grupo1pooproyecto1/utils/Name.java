/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.utils;

/**
 *
 * @author estebanruiz
 */
public class Name {
    private String firstName;
    private String lastName;
    
    public Name(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    } 
    
    @Override
    public String toString(){
        return firstName + "" + lastName;
    }
}
