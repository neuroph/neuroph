package org.neuroph.netbeans.files.nnet;

import java.io.IOException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.cookies.SaveCookie;

/**
 *
 * @author Ivana
 */
public class SaveCookieImpl implements SaveCookie {
private final SaveCookie context;
 public SaveCookieImpl(SaveCookie context) {
        this.context = context;
    }
 public void save() throws IOException {

            Confirmation msg = new NotifyDescriptor.Confirmation("Do you want to save \""
               + "file", NotifyDescriptor.OK_CANCEL_OPTION,
                NotifyDescriptor.QUESTION_MESSAGE);

            Object result = DialogDisplayer.getDefault().notify(msg);

            //When user clicks "Yes", indicating they really want to save,
            //we need to disable the Save button and Save menu item,
            //so that it will only be usable when the next change is made
            //to the text field:
            if (NotifyDescriptor.YES_OPTION.equals(result)) {
               context.save();
                //Implement your save functionality here.
            }

        }

}
