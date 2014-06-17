/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imagepreprocessing.wizard;

import imagepreprocessing.filter.ImageFilterChain;
import imagepreprocessing.manipulation.ImageManipulation;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.netbeans.imr.utils.ImagesLoader;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Handler for Image Data Set Wizard
 *
 * @author Sanja
 */
@ActionID(category = "Wizard", id = "org.neuroph.imagepreprocessing.wizard.ImageDataSetWizardAction")
@ActionRegistration(displayName = "Image Data Set Wizard")
@ActionReference(path = "Menu/Tools", position = 0)
public final class ImageDataSetWizardAction implements ActionListener {

    private ImageFilterChain chain;
    private File imagesDir;

    @Override
    public void actionPerformed(ActionEvent e) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        ImageDataSetWizardPanel1 ndp = new ImageDataSetWizardPanel1();
        try {
            if (ndp == null) {
                throw new Exception("Project with data set required");
            }
            panels.add(ndp);
            panels.add(new ImageDataSetWizardPanel2());
            panels.add(new ImageDataSetWizardPanel3());
            String[] steps = new String[panels.size()];
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                // Default step name to component name of panel.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
            WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
            // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
            wiz.setTitleFormat(new MessageFormat("{0}"));
            wiz.setTitle("Create data set for image recognition");
            if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {

                //read data enterd in wizard
                String imageDir = (String) wiz.getProperty("imageDir");
                chain = (ImageFilterChain) wiz.getProperty("filterChain");
                int[] angles = (int[]) wiz.getProperty("rotation");
                double[] scaling = (double[]) wiz.getProperty("scaling");
                int[] trans = (int[]) wiz.getProperty("transition");
                imagesDir = new File((String) wiz.getProperty("imageDir"));
                Color translationColor = (Color) wiz.getProperty("translationColor");
                Color rotationColor = (Color) wiz.getProperty("rotationColor");

                //filter images
                for (File imageFile : imagesDir.listFiles()) {
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        filterImage(image, imageFile.getName());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                //roate images
                for (File imageFile : imagesDir.listFiles()) {
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        rotateImage(image, imageFile.getName(), angles, rotationColor);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                //scale images
                for (File imageFile : imagesDir.listFiles()) {
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        scaleImage(image, imageFile.getName(), scaling);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                //translate images
                for (File imageFile : imagesDir.listFiles()) {
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        translateImage(image, imageFile.getName(), trans, translationColor);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                InputOutput io = IOProvider.getDefault().getIO("Image data set", true);
                io.getOut().println("Processed images created in Image directory of project: " + imagesDir.getPath());

                //create data set from all images
                int samplingWidth = (Integer) wiz.getProperty("samplingWidth");
                int samplingHeight = (Integer) wiz.getProperty("samplingHeight");
                DataSet ds = createDataSet(samplingWidth, samplingHeight);
                
                io.getOut().println("Data set 'imageRecognitionDataSet' created");
                io.getOut().close();
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Please select project", "Project required", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Rotates images for every angle in angles array.
     *
     * @param image image to be rotated
     * @param imageFileName name of image file
     * @param angles array of angles image should be rotated
     * @param backgroundColor image background color visible when image is
     * rotated
     *
     * @throws IOException
     */
    private void rotateImage(BufferedImage image, String imageFileName, int[] angles, Color backgroundColor) throws IOException {

        String fn = imageFileName.substring(0, imageFileName.indexOf("."));
        String ext = imageFileName.substring(imageFileName.indexOf(".") + 1, imageFileName.length());
        ImageManipulation mani = new ImageManipulation(image);
        for (int i : angles) {
            if (i != 0) {
                image = mani.rotate(i, backgroundColor);
                ImageIO.write(image, ext, new File(imagesDir + "/" + fn + "_rot_" + i + "." + ext));
            }
        }
    }

    /**
     * Filters image with all filters from filter chain.
     *
     * @param image image to be filtered
     * @param imageFileName name of image file
     *
     * @throws IOException
     */
    private void filterImage(BufferedImage image, String imageFileName) throws IOException {
        String fn = imageFileName.substring(0, imageFileName.indexOf("."));
        String ext = imageFileName.substring(imageFileName.indexOf(".") + 1, imageFileName.length());
        image = chain.processImage(image);
        ImageIO.write(image, ext, new File(imagesDir + "/" + fn + "." + ext));

    }

    /**
     * Scale image with every coefficient from coefficients array.
     *
     * @param image image to be scaled
     * @param imageFileName name of image file
     * @param coefficients array of scale coefficients
     *
     * @throws IOException
     */
    private void scaleImage(BufferedImage image, String imageFileName, double[] coefficients) throws IOException {
        String fn = imageFileName.substring(0, imageFileName.indexOf("."));
        String ext = imageFileName.substring(imageFileName.indexOf(".") + 1, imageFileName.length());
        ImageManipulation mani = new ImageManipulation(image);
        for (double i : coefficients) {
            if (i != 1 && i != 0) {
                image = mani.scale(i, i);

                ImageIO.write(image, ext, new File(imagesDir + "/" + fn + "_scl_" + i + "." + ext));
            }

        }
    }

    /**
     * Translate image for every number in trans array
     *
     * @param image image to be scaled
     * @param imageFileName name of image file
     * @param trans array of pixels image should be translated
     * @param backgroundColor image background color visible when image is
     * translated
     *
     * @throws IOException
     */
    private void translateImage(BufferedImage image, String imageFileName, int[] trans, Color backgroundColor) throws IOException {
        String fn = imageFileName.substring(0, imageFileName.indexOf("."));
        String ext = imageFileName.substring(imageFileName.indexOf(".") + 1, imageFileName.length());
        ImageManipulation mani = new ImageManipulation(image);
        for (int i : trans) {
            if (i != 0) {
                image = mani.translate(i, i, backgroundColor);
                ImageIO.write(image, ext, new File(imagesDir + "/" + fn + "_trans_" + i + "." + ext));
            }
        }
    }

    /**
     * Creates data set with specified sampling resolution Method copied from
     * ImageRecognition module
     *
     * @param samplingWidth
     * @param samplingHeight
     * @return
     */
    private DataSet createDataSet(int samplingWidth, int samplingHeight) {
        HashMap<String, FractionRgbData> rgbDataMap = new HashMap<String, FractionRgbData>();
        List imageLabels = new ArrayList<String>();

        try {
            //File labeledImagesDir = new File(imageDir);
            rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(imagesDir, new Dimension(samplingWidth, samplingHeight)));

            for (String imgName : rgbDataMap.keySet()) {
                StringTokenizer st = new StringTokenizer(imgName, "._");
                String imageLabel = st.nextToken();
                if (!imageLabels.contains(imageLabel)) {
                    imageLabels.add(imageLabel);
                }
            }
            Collections.sort(imageLabels);
        } catch (IOException ioe) {
            System.err.println("Unable to load images from labeled images dir: '" + imagesDir + "'");
            System.err.println(ioe.toString());
        }

        DataSet activeTrainingSet = null;

        activeTrainingSet = ImageRecognitionHelper.createTrainingSet(imageLabels, rgbDataMap);

        activeTrainingSet.setLabel("imageRecognitionDataSet");
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(activeTrainingSet);
        return activeTrainingSet;

    }

}
