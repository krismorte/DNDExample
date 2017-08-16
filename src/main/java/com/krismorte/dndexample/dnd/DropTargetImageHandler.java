/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krismorte.dndexample.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DropTargetImageHandler implements DropTargetListener {

    public enum DragState {

        Waiting,
        Accept,
        Reject
    }

    private DNDImagePanel dropPane;
    private DragState state = DragState.Waiting;

    public DropTargetImageHandler(DNDImagePanel dropPane) {
        this.dropPane = dropPane;

    }

    protected void processDrag(DropTargetDragEvent dtde) {
        /*if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }*/
        if (state == DragState.Accept) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        state = DragState.Reject;
        Transferable t = dtde.getTransferable();
        try {
            List<File> data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            if (data.size() > 1) {
                state = DragState.Reject;
            } else {
                for (File file : data) {
                    //System.out.println("File: " + file.getName().toUpperCase());
                    if (file.getName().toUpperCase().endsWith(".JPG") || file.getName().toUpperCase().endsWith(".PNG") || file.getName().toUpperCase().endsWith(".GIF")) {
                        state = DragState.Accept;
                    } else {
                        state = DragState.Reject;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            state = DragState.Reject;
        }

        processDrag(dtde);
        SwingUtilities.invokeLater(DragUpdate.getInstance(dropPane, true, dtde.getLocation()));
        dropPane.repaint();
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        processDrag(dtde);
        SwingUtilities.invokeLater(DragUpdate.getInstance(dropPane, true, dtde.getLocation()));
        dropPane.repaint();
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        state = DragState.Waiting;
        SwingUtilities.invokeLater(DragUpdate.getInstance(dropPane, false, null));
        dropPane.repaint();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        state = DragState.Waiting;
        SwingUtilities.invokeLater(DragUpdate.getInstance(dropPane, false, null));

        Transferable transferable = dtde.getTransferable();
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrop(dtde.getDropAction());
            try {

                List transferData = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                if (!dropPane.isMultiFile()) {
                    if (transferData.size() > 1) {
                        JOptionPane.showMessageDialog(null, "Just one file at the time!");
                        return;
                    }
                }

                if (transferData != null && transferData.size() > 0) {
                    dropPane.importFiles((File) transferData.get(0));
                    dtde.dropComplete(true);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            dtde.rejectDrop();
        }
    }
}
