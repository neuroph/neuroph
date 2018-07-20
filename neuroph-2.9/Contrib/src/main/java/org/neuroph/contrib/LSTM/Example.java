/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.LSTM;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.TransferFunctionType;

public class Example {

    public static NeuralNetwork assembleNeuralNetwork() {

        Layer inputLayer = new Layer();
        inputLayer.addNeuron(new BiasNeuron());
        inputLayer.addNeuron(new BiasNeuron());

        Layer hiddenLayerOne = new Layer();
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());

        Layer hiddenLayerTwo = new Layer();
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());

        Layer outputLayer = new Layer();
        outputLayer.addNeuron(new BiasNeuron());

        NeuralNetwork ann = new NeuralNetwork();

        ann.addLayer(0, inputLayer);
        ann.addLayer(1, hiddenLayerOne);
        ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(1));
        ann.addLayer(2, hiddenLayerTwo);
        ConnectionFactory.fullConnect(ann.getLayerAt(1), ann.getLayerAt(2));
       ann.addLayer(3, outputLayer);
        ConnectionFactory.fullConnect(ann.getLayerAt(2), ann.getLayerAt(3));
        ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(ann.getLayersCount() - 1), false);

        ann.setInputNeurons(inputLayer.getNeurons());
        ann.setOutputNeurons(outputLayer.getNeurons());

        ann.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        return ann;
    }

    public static NeuralNetwork trainNeuralNetwork(NeuralNetwork ann) {
        int inputSize = 2;
        int outputSize = 1;
        DataSet ds = new DataSet(inputSize, outputSize);

        DataSetRow rOne = new DataSetRow(new double[] { 0, 1 }, new double[] { 1 });
        ds.addRow(rOne);
        DataSetRow rTwo = new DataSetRow(new double[] { 1, 1 }, new double[] { 0 });
        ds.addRow(rTwo);
        DataSetRow rThree = new DataSetRow(new double[] { 0, 0 }, new double[] { 0 });
        ds.addRow(rThree);
        DataSetRow rFour = new DataSetRow(new double[] { 1, 0 }, new double[] { 1 });
        ds.addRow(rFour);

        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setMaxIterations(10000);

        ann.learn(ds, backPropagation);
        return ann;
    }
    
    public static void main(String... args) {
      System.out.println("Time stamp N1:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));
      String exampleText="I shall go unto other priests for their assistance in this sacrifice of mine, and conciliating them \n" +
                            "by sweet words and gifts, I shall represent unto them the business I have on hand, so that they may \n" +
                            "accomplish it.' Having said this, the monarch became silent. And, O chastiser of foes, when those priests \n" +
                            "well knew that they could not assist at the king's sacrifice, they pretended to be angry, and addressing \n" +
                            "that best of monarchs said, 'O best of kings, thy sacrifices are incessant! By assisting thee always, we \n" +
                            "have all been fatigued. And as we have been wearied in consequence of these labours, it behoveth thee \n" +
                            "to give us leave. O sinless one, from loss of judgment thou canst not wait (but urgest us repeatedly). Go \n" +
                            "unto Rudra! He will assist at thy sacrifice!' Hearing those words of censure and wrath, king Swetaki \n" +
                            "became angry. And the monarch wending to the mountains of Kailasa, devoted himself to asceticism \n" +
                            "there. And, O king, the monarch began to worship Mahadeva, with fixed attention, and by observing the \n" +
                            "most rigid vows. And foregoing all food at times, he passed a long period. The monarch ate only fruits \n" +
                            "and roots sometimes at the twelfth and sometimes at the sixteenth hour of the whole day. King Swetaki \n" +
                            "stood for six months, rapt in attention, with arms upraised and steadfast eyes, like the trunk of a tree or a \n" +
                            "column rooted to the ground. And, O Bharata, Sankara at last gratified with that tiger among kings, who \n" +
                            "was undergoing such hard penances, showed himself unto him. And the god spake unto the monarch in a \n" +
                            "calm and grave voice, saying, 'O tiger among kings, O chastiser of foes, I have been gratified with thee \n" +
                            "for thy asceticism! Blest be thou! Ask now the boon that thou, O king, desirest.' Hearing these words of \n" +
                            "Rudra of immeasurable energy, the royal sage bowed unto that deity and replied, saying, 'O illustrious \n" +
                            "one, O thou that art worshipped by the three worlds, if thou hast been gratified with me, then, O god of \n" +
                            "gods, assist me thyself, O lord of the celestials, in my sacrifice!' Hearing these words spoken by the \n" +
                            "monarch, the illustrious god was gratified, and smilingly said, 'We do not ourselves assist at sacrifices: \n" +
                            "but as thou, O king, hast undergone severe penances, desirous of obtaining a boon, I will, O chastiser of \n" +
                            "foes, assist at thy sacrifice, upon, O king, this condition.' And Rudra continued, 'If, O king of kings, thou \n" +
                            "canst, for twelve years, pour without intermission libations of clarified butter into the fire, thyself wishing never"
                            + " more to assist at his sacrifices. The king, however, repeatedly asked those \n" +
                            "Ritwiks to come to him. But they came not to his sacrifice in consequence of the painful state of their \n" +
                            "eyes. The king, therefore, invited at the command of his own Ritwiks, others like unto them, and \n" +
                            "completed the sacrifice that he had begun. After some days had elapsed, king Swetaki desired to perform \n" +
                            "another sacrifice which should extend for a hundred years. But the illustrious monarch obtained not any \n" +
                            "priest to assist him in it. The celebrated king then, with his friends and relatives, casting off all sloth, \n" +
                            "repeatedly courted his priests with great persistence, by bowing down unto them, by conciliatory \n" +
                            "speeches, and by gifts of wealth. All of them, however, refused to accomplish the purpose which that \n" +
                            "king of immeasurable energy had in view. Then that royal sage, getting angry, addressed those \n" +
                            "Brahmanas sitting in their asylums, and said, 'If, ye Brahmanas, I were a fallen person, or, if, I were \n" +
                            "wanting in homage and service to you, I should then deserve to be abandoned without scruple by you \n" +
                            "and by other Brahmanas at the same time. But as I am neither degraded nor wanting in homage to you, it \n" +
                            "behoveth you not to obstruct the performance by me of my sacrifice or to abandon me thus, ye foremost \n" +
                            "of Brahmanas, without adequate reason. I seek, ye Brahmanas, your protection! It behoveth you to be \n" +
                            "propitious unto me. But, ye foremost of Brahmanas, if you abandon me from enmity alone or any \n" +
                            "improper of Durvasa. Even that Brahmana endued with great energy will assist you in thy sacrifice. Let, therefore, \n" +
                            "every preparation be made.' Hearing these words uttered by Rudra, the king, returning to his own capital, \n" +
                            "began to collect all that was necessary. After everything had been collected, the monarch again \n" +
                            "presented himself before Rudra and said, 'Every necessary article hath been collected, and all my \n" +
                            "preparations are complete, through thy grace, O god of gods ! Let me, therefore, be installed at the \n" +
                            "sacrifice tomorrow.' Having heard these words of that illustrious king, Rudra summoned Durvasa before \n" +
                            "him and said. 'This, O Durvasa, is that best of monarchs called Swetaki. At my command, O best of \n" +
                            "Brahmanas, assist even this king in his sacrifice.' And the Rishi Durvasa said unto Rudra, 'So be it.' Then \n" +
                            "the sacrifice for which king Swetaki had made those preparations, took place. And the illustrious \n" +
                            "monarch's sacrifice was performed according to the ordinance and in proper season. And the gifts, on \n" +
                            "that occasion, unto the Brahmanas were large. And after that monarch's sacrifice had come to an end, all \n" +
                            "the other priests who had come to assist at it went away with Durvasa's leave. All other Sadasyas also of \n" +
                            "immeasurable energy, who had been installed at that sacrifice, then went away. That exalted monarch \n" +
                            "then entered his own palace, worshipped by exalted Brahmanas conversant with the Vedas, eulogised by \n" +
                            "chanters of panegyrical hymns and congratulated by the citizens.";
      HashMap encoding=TextEncoder.getEncoding(exampleText.replaceAll("\n", "").replaceAll(",", "").replace(".", " .").replaceAll(",", "").split(" "));
      int inputNeurons=5;
      int outputNeurons=1;
      DataSet trainingSet= TextEncoder.getDataSet(exampleText,encoding, inputNeurons,outputNeurons);
      
        int maxIterations = 10000;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,inputNeurons, 9, outputNeurons);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.000001);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.6);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        
        neuralNet.learn(trainingSet);
        System.out.println("Time stamp N2:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date())
                +"\t Iterarions="+((LMS) neuralNet.getLearningRule()).getCurrentIteration()
        +"\t Error ="+((LMS) neuralNet.getLearningRule()).getTotalNetworkError());
        
        String exampleTestText="But if you abandon me bla bla";
        
        
       // for (DataSetRow testDataRow : testSet.getRows()) {
       for(int i=0;i<6;i++)
       {   DataSet testSet= TextEncoder.getDataSet(exampleTestText,encoding, inputNeurons,outputNeurons);
            HashMap decoding=TextEncoder.getDecoding(encoding); 
           DataSetRow testDataRow = testSet.getRowAt(testSet.size()-1);
            neuralNet.setInput(testDataRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            System.out.print(" Input: " + Arrays.toString(testDataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput)) ;
            
            for(double textCode:testDataRow.getInput()){
                 //System.out.print(decoding.get(TextEncoder.round(textCode,encoding.size()/5))+" " );
      
            }
            
            for(double textCode:networkOutput){
                 //System.out.print(TextEncoder.findClosestMatch(decoding, TextEncoder.round(textCode,encoding.size()/5)) ); // decoding.get(TextEncoder.round(textCode,encoding.size()/5))+" " 
                 exampleTestText=exampleTestText +" "+TextEncoder.findClosestMatch(decoding, TextEncoder.round(textCode,encoding.size()/5));
            }
            
            if(i==0){
                exampleTestText=exampleTestText.replaceAll("bla bla", "");
            }
            System.out.println("\n"+exampleTestText);
            
    }
           // System.out.print(TextEncoder.round(textCode,encoding.size()/10-10)+" OutputText: " +decoding.get(TextEncoder.round(textCode,encoding.size()/10-10)) );
     //  }
        
        System.out.println("\n"+exampleTestText);
    }
}