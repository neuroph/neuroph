/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.fxvis;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.fxyz.tools.CubeViewer;

/**
 *
 * @author zoran
 */
public class JFXVisualizationPanel extends JFXPanel {

//    private PerspectiveCamera camera;
//    private final double sceneWidth = 600;
//    private final double sceneHeight = 600;
//    private double cameraDistance = 5000;    
//    

   public Scene scene;
    public Node sceneRoot;
    public CubeViewer cube;
    final PerspectiveCamera camera = new PerspectiveCamera(true);
//    final XForm cameraXform = new XForm();
    private double cameraDistance = 5000;
    private double axesSize = 1000;
    private double gridLineSpacing = 100;    

    public JFXVisualizationPanel() {
        super();
        // double size, double spacing, double distance
/*        axesSize = size;
        gridLineSpacing = spacing;
        cameraDistance = distance;*/
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });        
        
    }
    
    private void createScene() {
        // Creating the scene object and configuring initial size and background color
//        cube = new CubeViewer(axesSize,gridLineSpacing,false);  //This is a custom JavaFX 3D Group node I made
//        scene = new Scene(cube, 1024, 768, true);
//        scene.setFill(Color.AQUA);
        Group  root  =  new  Group();
        Scene  scene  =  new  Scene(root, Color.ALICEBLUE);
        Text  text  =  new  Text();
        
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("Welcome JavaFX!");

        root.getChildren().add(text);        
        
        
//        buildCamera(cube); 
//        
//        handleMouse(scene, cube); // Configuring mouse actions
//        handleTouch(scene,cube); //handle gestures and stuff

        // Adding camera and assigning the scene to the JFXPanel
        scene.setCamera(camera);
        this.setScene(scene);  //Remember this step because you have the JFXPanel as an intermediate layer
    }    
    
    
}
