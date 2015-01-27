package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.neuroph.util.data.norm.RangeNormalizer;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.rightclick.RangeNormalizationAction"
)
@ActionRegistration(
        displayName = "#CTL_RangeNormalizationAction"
)
@Messages("CTL_RangeNormalizationAction=Range")
public final class RangeNormalizationAction  extends AbstractAction implements ActionListener {

    private final DataSetDataObject context;

    public RangeNormalizationAction(DataSetDataObject context) {
        super(Bundle.CTL_RangeNormalizationAction());
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RangeNormalizationPanel panel = new RangeNormalizationPanel();
        
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                panel, // instance of your panel
                "Range Normalization Parameters", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                      // otherwise specify options as:
                      //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
        );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            
          double lowLimit = Double.parseDouble(panel.getLowField().getText().trim());
          double highLimit = Double.parseDouble(panel.getHighField().getText().trim());
          
          DataSet dataSet = context.getDataSet();
          RangeNormalizer norm = new RangeNormalizer(lowLimit, highLimit);
          norm.normalize(dataSet);
            
          IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Normalized data set dataSet.getLabel()  using Range normalization method on range ["+lowLimit + " "+highLimit+"]");        
        } 
    }
}
