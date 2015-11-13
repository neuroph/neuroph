package org.neuroph.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.LMS;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Shivanth
 * @author Jubin - cleaned up added mock and powermock
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = NeuralNetwork.class)
public class NeuralNetworkTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
//	@Rule public TemporaryFolder folder= new TemporaryFolder();

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
        neuralNetwork = new NeuralNetwork<LMS>();
        Layer layer1 = mock(Layer.class);
        Layer layer2 = mock(Layer.class);
        Layer layer3 = mock(Layer.class);

        neuralNetwork.addLayer(layer1);
        neuralNetwork.addLayer(layer2);
        neuralNetwork.addLayer(layer3);

        Neuron[] neurons = new Neuron[2];
        for (int i = 0; i < 2; i++) {
            neurons[i] = mock(Neuron.class);
        }
        neuralNetwork.setInputNeurons(neurons);

        for (int i = 0; i < 2; i++) {
            neurons[i] = mock(Neuron.class);
        }
        neuralNetwork.setOutputNeurons(neurons);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPrelims() {
        assertEquals(3, neuralNetwork.getLayers().length);
    }

    @Test
    public void testInputNeurons() {
        assertEquals(2, neuralNetwork.getInputNeurons().length);
        assertThat(3, not(neuralNetwork.getInputNeurons().length));
    }

    @Test
    public void testOutputNeuros() {
        assertEquals(2, neuralNetwork.getOutputNeurons().length);
        assertThat(3, not(neuralNetwork.getOutputNeurons().length));
    }

    @Test
    public void shouldFireNetworkEvent() {
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        NeuralNetworkEventListener nnlist2 = mock(NeuralNetworkEventListener.class);
        NeuralNetworkEventListener nnlist3 = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        neuralNetwork.addListener(nnlist2);
        neuralNetwork.addListener(nnlist3);

        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);

        neuralNetwork.fireNetworkEvent(NNevt);

        verify(nnlist).handleNeuralNetworkEvent(NNevt);
        verify(nnlist2).handleNeuralNetworkEvent(NNevt);
        verify(nnlist3).handleNeuralNetworkEvent(NNevt);
    }

    @Test
    public void shouldNotFireNetworkEvent() {
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
        neuralNetwork.fireNetworkEvent(NNevt);
        verify(nnlist, Mockito.times(0)).handleNeuralNetworkEvent(NNevt);;
    }

    //not sure the test cases for these static methods are correct. 
    //the coverage report doesnt show for these test cases.

    @Test(expected = NeurophException.class)
    public void shouldNotCreatefromFile() {
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        NeuralNetwork.createFromFile(file);
    }

    @Test(expected = NeurophException.class)
    public void shouldNotCreatefromFilepath() {
        NeuralNetwork nn = NeuralNetwork.load("jujubi.txt");
    }

    @Test
    public void shouldCreatefromFileStream() {
        try {
            neuralNetwork.setLabel("TestNetLabel");
            neuralNetwork.save("testNet.nnet");
            NeuralNetwork nn = NeuralNetwork.load(new FileInputStream("testNet.nnet"));
            assertThat(nn.getLabel(), is("TestNetLabel"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void shouldCreatefromFile() throws Exception {
        neuralNetwork.setLabel("TestNetLabel");
        neuralNetwork.save("testNet.nnet");
        NeuralNetwork nn = NeuralNetwork.createFromFile("testNet.nnet");
        assertThat(nn.getLabel(), is("TestNetLabel"));
    }

    @Test
    public void testSave() throws IOException {
        neuralNetwork.setLabel("TestNetLabel");
        neuralNetwork.save("testNet.nnet");
        NeuralNetwork nn = NeuralNetwork.load("testNet.nnet");
        assertThat(nn.getLabel(), is("TestNetLabel"));
    }

    @Test
    public void testAddLayer() {
        Layer layer1 = mock(Layer.class);
        double count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        neuralNetwork.addLayer(layer1);
        assertEquals((count + 1), neuralNetwork.getLayersCount(), 0);
        verify(nnlist, times(1)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testAddNullLayer() {
        Layer layer1 = null;
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Layer cant be null!");
        neuralNetwork.addLayer(layer1);
        verify(nnlist, times(0)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test
    public void testAddLayerIndex() {
        Layer layer1 = mock(Layer.class);
        double count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        neuralNetwork.addLayer(0, layer1);
        assertEquals((count + 1), neuralNetwork.getLayersCount(), 0);
        verify(nnlist, times(1)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testAddNullLayerIndex() {
        Layer layer1 = null;
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Layer cant be null!");
        neuralNetwork.addLayer(0, layer1);
        verify(nnlist, times(0)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test
    public void testRemoveLayerAt() {
        int count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        neuralNetwork.removeLayerAt(count - 1);
        verify(nnlist, times(1)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test
    public void testRemoveLayer() {
        int count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        Layer l1 = neuralNetwork.getLayerAt(count - 1);
        neuralNetwork.removeLayer(l1);
        verify(nnlist, times(1)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test//(expected=ArrayIndexOutOfBoundsException.class)
    public void testRemoveLayerAtMax() {
        int count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        exception.expect(ArrayIndexOutOfBoundsException.class);
        neuralNetwork.removeLayerAt(count);
        verify(nnlist, times(0)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test//(expected=RuntimeException.class)
    public void testRemoveLayerNotThere() {
       // int count = neuralNetwork.getLayersCount();
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        Layer l1 = new Layer(2);
        exception.expect(RuntimeException.class);
        exception.expectMessage("Layer not in Neural n/w");
        neuralNetwork.removeLayer(l1);//firing an event not supposed to be there
        verify(nnlist, times(0)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test//(expected=VectorSizeMismatchException.class)
    public void testSetInput() {
        double[] input = {1.0, 2.0};
        neuralNetwork.setInput(input);
        for (int i = 0; i < neuralNetwork.getInputNeurons().length; i++) {
            verify(neuralNetwork.getInputNeurons()[i], times(1)).setInput(input[i]);
        }

        double[] input1 = {1.0, 2.0, 3.0};
        exception.expect(VectorSizeMismatchException.class);
        exception.expectMessage("Input vector size does not match network input dimension!");
        neuralNetwork.setInput(input1);

    }

    @Test
    public void testOutputNeurons() {

        double[] outputNeuron = neuralNetwork.getOutput();
        assertEquals(2, outputNeuron.length, 0);

    }

    @Test
    public void testCalculate() {
        Layer[] layers = neuralNetwork.getLayers();
        for (Layer layer : layers) {
            Mockito.doNothing().when(layer).calculate();
        }
        NeuralNetworkEventListener nnlist = mock(NeuralNetworkEventListener.class);
        neuralNetwork.addListener(nnlist);
        neuralNetwork.calculate();
        verify(nnlist, times(1)).handleNeuralNetworkEvent(any(NeuralNetworkEvent.class));
    }

    @Test
    public void testReset() {
        Layer[] layers = neuralNetwork.getLayers();
        for (Layer layer : layers) {
            Mockito.doNothing().when(layer).reset();
        }
        neuralNetwork.reset();
        for (Layer layer : layers) {
            verify(layer, times(1)).reset();
        }
    }

    @Test
    public void testSetLearningRule() {
        LearningRule learningRule = mock(LearningRule.class);
        Mockito.doCallRealMethod().when(learningRule).setNeuralNetwork(neuralNetwork);
        neuralNetwork.setLearningRule(learningRule);
        verify(learningRule, times(1)).setNeuralNetwork(any(NeuralNetwork.class));
        assertNotNull(neuralNetwork.getLearningRule());
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testSetNullLearningRule() {
        LearningRule learningRule = null;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Learning rule can't be null!");
        neuralNetwork.setLearningRule(learningRule);

    }

    @Test
    public void testSetNullLearningRule1() {
        LearningRule learningRule = null;
        try {
            neuralNetwork.setLearningRule(learningRule);
        } catch (IllegalArgumentException e) {

        }
        assertEquals(null, neuralNetwork.getLearningRule());
    }

    @Test
    public void testLearn() {
        DataSet ds = mock(DataSet.class);
        LearningRule learningRule = mock(LearningRule.class);
        neuralNetwork.setLearningRule(learningRule);
        neuralNetwork.learn(ds);
        verify(learningRule, times(1)).learn(ds);
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testNullLearn() {
        DataSet ds = null;
        LearningRule learningRule = mock(LearningRule.class);
        neuralNetwork.setLearningRule(learningRule);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Training set is null!");
        neuralNetwork.learn(ds);
    }

    @Test
    public void testLearn_with_learningrule() {
        DataSet ds = mock(DataSet.class);
        LearningRule learningRule = mock(LearningRule.class);
        neuralNetwork.learn(ds, learningRule);
        verify(learningRule, times(1)).learn(ds);
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testLearn_with_null_learningrule() {
        DataSet ds = mock(DataSet.class);
        LearningRule learningRule = null;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Learning rule can't be null!");
        neuralNetwork.learn(ds, learningRule);
    }

    @Test
    public void testGetWeights() {
        NeuralNetwork neuralNetwork = new NeuralNetwork<>();
        Layer layer0 = new Layer();
        Layer layer1 = new Layer();
        neuralNetwork.addLayer(layer0);
        neuralNetwork.addLayer(layer1);
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());

        for (Layer layer : neuralNetwork.getLayers()) {
            for (Neuron nn : layer.getNeurons()) {
                Connection connection = new Connection(new Neuron(), nn, 2.0);
                nn.addInputConnection(connection);
            }
        }

        double[] weights = ArrayUtils.toPrimitive(neuralNetwork.getWeights());
        double[] w1 = new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        Assert.assertArrayEquals(w1, weights, 0);
    }

    @Test
    public void testSetWeights() {
        NeuralNetwork neuralNetwork = new NeuralNetwork<>();
        Layer layer0 = new Layer();
        Layer layer1 = new Layer();
        neuralNetwork.addLayer(layer0);
        neuralNetwork.addLayer(layer1);
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());

        for (Layer layer : neuralNetwork.getLayers()) {
            for (Neuron nn : layer.getNeurons()) {
                Connection connection = new Connection(new Neuron(), nn);
                nn.addInputConnection(connection);
            }
        }
        double[] w1 = new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        neuralNetwork.setWeights(w1);
        Assert.assertArrayEquals(w1, ArrayUtils.toPrimitive(neuralNetwork.getWeights()), 0);

    }

    @Test
    public void testIsEmpty() {
        NeuralNetwork neuralNetwork = new NeuralNetwork<>();
        assertThat(neuralNetwork.isEmpty(), is(true));
    }

    @Test
    public void testCreateConnection() {
        NeuralNetwork neuralNetwork = new NeuralNetwork<>();
        Layer layer0 = new Layer();
        Layer layer1 = new Layer();
        neuralNetwork.addLayer(layer0);
        neuralNetwork.addLayer(layer1);
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer0.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());
        layer1.addNeuron(new Neuron());

        neuralNetwork.createConnection(layer0.getNeurons()[0], layer1.getNeurons()[0], 2);
        Connection[] conn = layer1.getNeurons()[0].getInputConnections();
        assertNotNull(conn);
        assertThat(conn.length, is(1));
    }

    @Test
    public void testAddListener() {
        NeuralNetworkEventListener nnlist3 = mock(NeuralNetworkEventListener.class);
        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
        neuralNetwork.addListener(nnlist3);
        neuralNetwork.fireNetworkEvent(NNevt);
        verify(nnlist3, times(1)).handleNeuralNetworkEvent(NNevt);
    }

    @Test
    public void testRemoveListener() {
        NeuralNetworkEventListener nnlist3 = mock(NeuralNetworkEventListener.class);
        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
        neuralNetwork.addListener(nnlist3);
        neuralNetwork.removeListener(nnlist3);
        neuralNetwork.fireNetworkEvent(NNevt);
        verify(nnlist3, times(0)).handleNeuralNetworkEvent(NNevt);
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testAddNullListener() {
        NeuralNetworkEventListener nnlist3 = null;
        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
        exception.expect(IllegalArgumentException.class);
        neuralNetwork.addListener(nnlist3);
        neuralNetwork.fireNetworkEvent(NNevt);
    }

    @Test//(expected=IllegalArgumentException.class)
    public void testRemoveNullListener() {
        NeuralNetworkEventListener nnlist3 = null;
        NeuralNetworkEvent NNevt = mock(NeuralNetworkEvent.class);
        exception.expect(IllegalArgumentException.class);
        neuralNetwork.removeListener(nnlist3);
        neuralNetwork.fireNetworkEvent(NNevt);
    }

    @Test
    public void testLabel() {
        neuralNetwork.setLabel("hello");
        assertThat(neuralNetwork.getLabel(), is("hello"));
    }

    @Test
    public void testToString() {
        neuralNetwork.setLabel("Hello");
        assertThat(neuralNetwork.getLabel(), is(neuralNetwork.toString()));
    }

    @Test
    public void testSetOutputLabels() {
        NeuralNetwork neuralNetwork = new NeuralNetwork<>();
        Neuron[] neurons = new Neuron[2];
        for (int i = 0; i < 2; i++) {
            neurons[i] = new Neuron();
        }
        neuralNetwork.setOutputNeurons(neurons);

        String[] labels = {"hello", "good", "day"};
        neuralNetwork.setOutputLabels(labels);
        for (int i = 0; i < neuralNetwork.getOutputNeurons().length; i++) {
            assertThat(neuralNetwork.getOutputNeurons()[i].getLabel(),
                    is(labels[i]));
        }
    }

    @Test
    public void testGetOutputsCount() {
        assertThat(neuralNetwork.getOutputsCount(),
                is(2));
    }

    @Test
    public void testGetInputsCount() {
        assertThat(neuralNetwork.getInputsCount(),
                is(2));
    }
}
