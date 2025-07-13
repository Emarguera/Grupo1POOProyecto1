/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.interfaces.Playable;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author estebanruiz
 */
public class PlaybackQueue {
    private Queue<Playable> items;

    public PlaybackQueue() {
        this.items = new LinkedList<>();
    }

    public void addItem(Playable item) {
        getItems().offer(item);
    }

    public Playable removeItem() {
        return getItems().poll();
    }

    public void clear() {
        getItems().clear();
    }

    public Queue<Playable> getItems() {
        return items;
    }

    public void setItems(Queue<Playable> items) {
        this.items = items;
    }
}
