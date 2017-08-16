/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krismorte.dndexample.dnd;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author c007329
 */
public class DNDSupport extends MouseAdapter implements ActionListener {
    
    private JPopupMenu menu = new JPopupMenu();
    private JMenuItem remove = new JMenuItem("remove");
    private JLabel dragLabel = null;
    private int dragLabelWidthDiv2;
    private int dragLabelHeightDiv2;
    private JPanel clickedPanel = null;
    private Point location;
    private DNDImagePanel choosedPanel;
    private Container parent;
    private JPanel panelImages;
    private List<JPanel> panels = new ArrayList<>();
    
    public DNDSupport(Container parent, JPanel panelImages) {
        
        for (Component c : panelImages.getComponents()) {
            if (c instanceof JPanel) {
                panels.add((JPanel) c);
            }
        }
        
        this.parent = parent;
        this.panelImages = panelImages;
        remove.addActionListener(this);
        menu.add(remove);
    }
    
    public void changeJMenuItemText(String itemText) {
        remove.setText(itemText);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (choosedPanel != null) {
            choosedPanel.clear();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        
        choosedPanel = (DNDImagePanel) panelImages.getComponentAt(me.getPoint());
        if (me.getButton() == MouseEvent.BUTTON3) {
            menu.show(me.getComponent(), me.getX(), me.getY());            
        } else {
            clickedPanel = (JPanel) panelImages.getComponentAt(me.getPoint());
            Component[] components = clickedPanel.getComponents();
            if (components.length == 0) {
                return;
            }
            // if we click on jpanel that holds a jlabel
            if (components[0] instanceof JLabel) {

                // remove label from panel
                dragLabel = (JLabel) components[0];
                location = dragLabel.getLocation();
                clickedPanel.remove(dragLabel);
                clickedPanel.repaint();
                
                dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
                dragLabelHeightDiv2 = 0;
                
                int x = me.getPoint().x - dragLabelWidthDiv2;
                int y = me.getPoint().y - dragLabelHeightDiv2;
                dragLabel.setLocation(x, y);
                parent.add(dragLabel);
                parent.repaint();
            }
        }
        
    }
    
    @Override
    public void mouseDragged(MouseEvent me) {
        if (dragLabel == null) {
            return;
        }
        int x = me.getPoint().x - dragLabelWidthDiv2;
        int y = me.getPoint().y - dragLabelHeightDiv2;
        dragLabel.setLocation(x, y);
        parent.repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
        if (dragLabel == null) {
            return;
        }
        panelImages.remove(dragLabel); // remove dragLabel for drag layer of JLayeredPane
        JPanel droppedPanel = (JPanel) panelImages.getComponentAt(me.getPoint());
        if (droppedPanel == null) {
            // if off the grid, return label to home            
            dragLabel.setLocation(location);
            clickedPanel.add(dragLabel);
            clickedPanel.repaint();
        } else {
            boolean founded = false;
            
            for (JPanel panelFroList : panels) {
                if (panelFroList == droppedPanel) {
                    founded = true;
                    break;
                }
            }
            
            if (founded == false) {
                // if off the grid, return label to home
                dragLabel.setLocation(location);
                clickedPanel.add(dragLabel);
                clickedPanel.repaint();
            } else {
                ((DNDImagePanel) droppedPanel).clear();
                dragLabel.setLocation(location);
                droppedPanel.add(dragLabel);
                droppedPanel.repaint();
            }
        }
        parent.repaint();
        dragLabel = null;
    }
    
}
