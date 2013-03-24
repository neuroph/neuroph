package org.neuroph.netbeans.files.nnet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.neuroph.netbeans.main.easyneurons.file.FileIO;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public final class SaveCookieAction implements ActionListener {

    private final SaveCookie context;

    public SaveCookieAction(SaveCookie context) {
        this.context = context;
    }

    public void save(NeuralNetworkDataObject nd) {

        FileObject fo = nd.getPrimaryFile();
        String putanja = fo.getPath();
        System.out.println(putanja);
        FileIO fio = new FileIO();
        fio.saveNeuralNetwork(nd.getNeuralNetwork(), nd.getName(), putanja);
//            JOptionPane.showMessageDialog(null, nd.getName()+" has been saved");
    }

    public void actionPerformed(ActionEvent e) {
        //     JOptionPane.showMessageDialog(null, "save");
        try {
            context.save();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
