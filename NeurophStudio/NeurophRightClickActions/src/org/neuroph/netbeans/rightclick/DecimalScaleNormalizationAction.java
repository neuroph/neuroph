package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.neuroph.util.data.norm.DecimalScaleNormalizer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.rightclick.DecimalScaleNormalizationAction"
)
@ActionRegistration(
        displayName = "#CTL_DecimalScaleNormalizationAction"
)
@Messages("CTL_DecimalScaleNormalizationAction=Decimal Scale")
public final class DecimalScaleNormalizationAction extends AbstractAction implements ActionListener {

    private final DataSetDataObject context;

    public DecimalScaleNormalizationAction(DataSetDataObject context) {
        super(Bundle.CTL_DecimalScaleNormalizationAction());
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
       DataSet dataSet = context.getDataSet();
       DecimalScaleNormalizer norm = new DecimalScaleNormalizer();
       norm.normalize(dataSet);
       
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Normalized data set " + dataSet.getLabel() + " using Decimal Scale normalization method");        
    }
}
