/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krismorte.dndexample.dnd;

import java.awt.Point;

public class DragUpdate implements Runnable {

    private boolean dragOver;
    private Point dragPoint;
    private DNDImagePanel dropPane;

    private DragUpdate(DNDImagePanel dropPane, boolean dragOver, Point dragPoint) {
        this.dropPane = dropPane;
        this.dragOver = dragOver;
        this.dragPoint = dragPoint;
    }

    public static DragUpdate getInstance(DNDImagePanel dropPane, boolean dragOver, Point dragPoint) {
        DragUpdate dragUpdate = new DragUpdate(dropPane, dragOver, dragPoint);
        return dragUpdate;
    }

    @Override
    public void run() {
        dropPane.setDragOver(dragOver);
        dropPane.repaint();
    }
}
