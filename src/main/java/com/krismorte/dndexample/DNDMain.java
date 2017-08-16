/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krismorte.dndexample;

import com.krismorte.dndexample.view.DNDTester;
import javax.swing.UIManager;

/**
 *
 * @author c007329
 */
public class DNDMain {

   

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                DNDTester dNDTester = new DNDTester();
                dNDTester.setVisible(true);
            }
        });
    }

}
