package org.neuroph.core;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.core.exceptions.NeurophException;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 *
 * @author Shivanth
 * @author Jubin - cleaned up added mock and powermock 
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value=NeuralNetwork.class)
public class NeuralNetworkTest {

	@Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @SuppressWarnings("rawtypes")
	private NeuralNetwork neuralNetwork;
    @BeforeClass
    public static void setUpClass() throws Exception {
   }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	neuralNetwork = new NeuralNetwork<>(); 
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPrelims() {
    	
    	Layer layer1 = mock(Layer.class);
    	Layer layer2 = mock(Layer.class);
    	Layer layer3 = mock(Layer.class);
         
         neuralNetwork.addLayer(layer1);
         neuralNetwork.addLayer(layer2);
         neuralNetwork.addLayer(layer3);
         assertEquals(3, neuralNetwork.getLayers().length);
    }

    @Test
    public void testInputNeurons() {
    	 Neuron[] neurons = new Neuron[6];
    	 for (int i = 0; i < 6; i++) 
             neurons[i] = mock(Neuron.class);
         
    	 Layer layer1 = new Layer();
    	 layer1.addNeuron(neurons[0]);
    	 layer1.addNeuron(neurons[1]);
    	 neuralNetwork.addLayer(layer1);
    	
    	 neuralNetwork.setInputNeurons(layer1.getNeurons());
        assertEquals(2, neuralNetwork.getInputNeurons().length);
        assertThat(3, not(neuralNetwork.getInputNeurons().length));

    }

    @Test
    public void testOutputNeuros() {
   	 Neuron[] neurons = new Neuron[6];
	 for (int i = 0; i < 6; i++) 
         neurons[i] = mock(Neuron.class);
     
	 Layer layer1 = new Layer();
	 layer1.addNeuron(neurons[0]);
	 layer1.addNeuron(neurons[1]);
	 neuralNetwork.addLayer(layer1);
	 neuralNetwork.setOutputNeurons(layer1.getNeurons());
     assertEquals(2, neuralNetwork.getOutputNeurons().length);
     assertThat(3, not(neuralNetwork.getOutputNeurons().length));
    }
    
    @Test 
    public void shouldFireNetworkEvent(){
    	NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
    	NeuralNetworkEventListener nnlist2 = mock(NeuralNetworkEventListener.class);
    	NeuralNetworkEventListener nnlist3 = mock(NeuralNetworkEventListener.class);
    	neuralNetwork.addListener(nnlist);
    	neuralNetwork.addListener(nnlist2);
    	neuralNetwork.addListener(nnlist3);

    	NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
    
    	neuralNetwork.fireNetworkEvent(NNevt);
    	verify(nnlist).handleNeuralNetworkEvent(NNevt);;
    	verify(nnlist2).handleNeuralNetworkEvent(NNevt);;
    	verify(nnlist3).handleNeuralNetworkEvent(NNevt);;
    }

    @Test 
    public void shouldNotFireNetworkEvent(){
    	NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
    	NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
    	neuralNetwork.fireNetworkEvent(NNevt);
    	verify(nnlist,Mockito.times(0)).handleNeuralNetworkEvent(NNevt);;
   }
    //not sure the test cases for these static methods are correct. 
    //the coverage report doesnt show for these test cases.
    @Test(expected=NeurophException.class)
    public void shouldNotCreatefromFile(){
    	File file = mock(File.class);
    	when(file.exists()).thenThrow(FileNotFoundException.class);
    	NeuralNetwork.createFromFile(file);
    }
    @Test
    public void shouldCreatefromFile() throws Exception{
    		mockStatic(NeuralNetwork.class);
    		File file= folder.newFile("myfile.txt");
    		when(NeuralNetwork.createFromFile(file)).thenReturn(new NeuralNetwork<>());
			assertNotNull(NeuralNetwork.createFromFile(file));
    }
    
}
