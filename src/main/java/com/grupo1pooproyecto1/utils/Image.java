/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.utils;

/**
 *
 * @author estebanruiz
 */
public class Image {
    private String filePath;
    
    public Image(String filePath){
        this.filePath = filePath;
    }
    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }    
    
    @Override
    
    public String toString(){
        return "Image path:" + filePath;
    }
}
