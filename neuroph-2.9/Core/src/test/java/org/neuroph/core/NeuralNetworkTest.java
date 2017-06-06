package org.neuroph.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.plugins.PluginBase;
import org.neuroph.util.random.WeightsRandomizer;

/**
 *
 * @author Shivanth, Jubin, Tijana
 */
public class NeuralNetworkTest {

    NeuralNetwork<LearningRule> instance;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        instance = new NeuralNetwork<>();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testAddLayer() {
        Layer l = Mockito.mock(Layer.class);
        instance.addLayer(l);
        assertTrue(instance.getLayers().get(0) == l);
        assertTrue(instance.getLayers().size() == 1);
    }

    @Test
    public void testAddLayerIndex() {
        Layer l = Mockito.mock(Layer.class);
        instance.addLayer(0, l);
        assertTrue(instance.getLayers().get(0) == l);
        assertTrue(instance.getLayers().size() == 1);
        instance.addLayer(1, l);
        assertTrue(instance.getLayers().get(1) == l);
        assertTrue(instance.getLayers().size() == 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddLayerIndexWithException() throws Exception {
        Layer l = new Layer();
        instance.addLayer(5, l);
    }

    @Test
    public void testAddListener() {
        NeuralNetworkEventListener l = Mockito.mock(NeuralNetworkEventListener.class);
        instance.addListener(l);
        NeuralNetworkEvent event = Mockito.mock(NeuralNetworkEvent.class);
        instance.fireNetworkEvent(event);
        Mockito.verify(l).handleNeuralNetworkEvent(event);

        NeuralNetworkEventListener l1 = Mockito.mock(NeuralNetworkEventListener.class);
        Mockito.verify(l1, Mockito.never()).handleNeuralNetworkEvent(event);
    }

    @Test
    public void testAddPlugin() {
        PluginBase p = Mockito.mock(PluginBase.class);
        instance.addPlugin(p);
        assertTrue(instance.getPlugin(p.getClass()) == p);

        PluginBase p1 = Mockito.mock(PluginBase.class);
        assertFalse(instance.getPlugin(p1.getClass()) == p1);
    }

    @Test
    public void testCalculate() {
        NeuralNetworkEventListener l = Mockito.mock(NeuralNetworkEventListener.class);
        instance.addListener(l);
        instance.calculate();
        Mockito.verify(l).handleNeuralNetworkEvent(Mockito.any(NeuralNetworkEvent.class));
    }

    @Test
    public void testCreateConnection() {
        Neuron n = new Neuron();
        Neuron n1 = new Neuron();
        instance.setInputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
            }
        });
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n1);
            }
        });
        instance.createConnection(instance.getInputNeurons().get(0), instance.getOutputNeurons().get(0), 5);
        assertTrue(instance.getOutputNeurons().get(0).getInputConnections().size() == 1);
        assertTrue(instance.getOutputNeurons().get(0).getInputConnections().get(0).getWeight().getValue() == 5);
        assertTrue(instance.getInputNeurons().get(0).getInputConnections().isEmpty());
    }

    public void testCreateFromFile() {
        Layer l = Mockito.mock(Layer.class);
        instance.addLayer(l);
        instance.save("test.nnet");
        @SuppressWarnings("unchecked")
        NeuralNetwork<LearningRule> nn1 = NeuralNetwork.createFromFile("test.nnet");
        assertTrue(instance.getLayers().size() == nn1.getLayers().size());
        assertTrue(instance.getLayers().get(0) == nn1.getLayers().get(0));
    }

    @Test
    public void testGetInputNeurons() {
        assertTrue(instance.getInputNeurons().isEmpty());
        Neuron n = Mockito.mock(Neuron.class);
        instance.setInputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
            }
        });
        assertTrue(instance.getInputNeurons().get(0) == n);
        assertTrue(instance.getInputNeurons().size() == 1);
    }

    @Test
    public void testGetInputsCount() {
        assertTrue(instance.getInputsCount() == 0);
        Neuron n = Mockito.mock(Neuron.class);
        instance.setInputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
                add(n);
            }
        });
        assertTrue(instance.getInputsCount() == 2);
    }

    @Test
    public void testGetLabel() {
        assertTrue(instance.getLabel().length() == 0);
        instance.setLabel("Test");
        assertTrue(instance.getLabel().length() == 4);
        assertTrue(instance.getLabel().equals("Test"));
    }

    @Test
    public void testGetLayerAt() {
        Layer l = Mockito.mock(Layer.class);
        Layer l1 = Mockito.mock(Layer.class);
        instance.addLayer(0, l);
        instance.addLayer(1, l1);
        assertTrue(instance.getLayers().get(0) == instance.getLayerAt(0));
        assertTrue(instance.getLayers().get(1) == instance.getLayerAt(1));
        assertTrue(instance.getLayerAt(0) == l);
        assertTrue(instance.getLayerAt(1) == l1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetLayerAtException() throws Exception {
        instance.getLayerAt(0);
    }

    @Test
    public void testGetLayers() {
        assertTrue(instance.getLayers().isEmpty());
        Layer l = Mockito.mock(Layer.class);
        Layer l1 = Mockito.mock(Layer.class);
        instance.addLayer(0, l);
        instance.addLayer(1, l1);
        assertTrue(instance.getLayers().get(0) == l);
        assertTrue(instance.getLayers().get(1) == l1);
        assertTrue(instance.getLayers().size() == 2);
    }

    @Test
    public void testGetLayersCount() {
        assertTrue(instance.getLayersCount() == 0);
        Layer l = Mockito.mock(Layer.class);
        Layer l1 = Mockito.mock(Layer.class);
        instance.addLayer(0, l);
        instance.addLayer(1, l1);
        assertTrue(instance.getLayers().size() == instance.getLayersCount());
        assertTrue(instance.getLayersCount() == 2);
    }

    @Test
    public void testGetLearningRule() {
        assertNull(instance.getLearningRule());
        LearningRule l = Mockito.mock(LearningRule.class);
        instance.setLearningRule(l);
        assertTrue(instance.getLearningRule() == l);
    }

    @Test
    public void testGetNetworkType() {
        assertNull(instance.getNetworkType());
        instance.setNetworkType(NeuralNetworkType.PERCEPTRON);
        assertTrue(instance.getNetworkType() == NeuralNetworkType.PERCEPTRON);
        assertTrue(instance.getNetworkType().toString().equals("PERCEPTRON"));
    }

    @Test
    public void testGetOutput() {
        assertNull(instance.getOutput());
        Neuron n = Mockito.mock(Neuron.class);
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
            }
        });
        assertTrue(instance.getOutput().length == 1);
        assertTrue(instance.getOutput()[0] == 0);
    }

    @Test
    public void testGetOutputNeurons() {
        assertTrue(instance.getOutputNeurons().isEmpty());
        Neuron n = Mockito.mock(Neuron.class);
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
            }
        });
        assertTrue(instance.getOutputNeurons().size() == 1);
        assertTrue(instance.getOutputNeurons().get(0) == n);
    }

    @Test
    public void testGetOutputsCount() {
        assertTrue(instance.getOutputsCount() == 0);
        Neuron n = Mockito.mock(Neuron.class);
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
                add(n);
            }
        });
        assertTrue(instance.getOutputsCount() == 2);
    }

    @Test
    public void testGetPlugin() {
        PluginBase p = Mockito.mock(PluginBase.class);
        assertNull(instance.getPlugin(p.getClass()));
        instance.addPlugin(p);
        assertTrue(instance.getPlugin(p.getClass()) == p);
    }

    @Test
    public void testGetWeights() {
        assertTrue(instance.getWeights().length == 0);
        Layer l1 = new Layer();
        l1.addNeuron(new Neuron());
        l1.addNeuron(new Neuron());
        Layer l2 = new Layer();
        l2.addNeuron(new Neuron());

        instance.addLayer(l1);
        instance.addLayer(l2);

        instance.createConnection(l1.getNeuronAt(0), l2.getNeuronAt(0), 5);
        instance.createConnection(l1.getNeuronAt(1), l2.getNeuronAt(0), 3);

        assertTrue(instance.getWeights().length == 2);
        assertTrue(instance.getWeights()[0] == 5);
        assertTrue(instance.getWeights()[1] == 3);
    }

    @Test
    public void testIndexOf() {
        Layer l = Mockito.mock(Layer.class);
        assertTrue(instance.indexOf(l) == -1);
        instance.addLayer(l);
        assertTrue(instance.indexOf(l) == 0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        Layer l = Mockito.mock(Layer.class);
        instance.addLayer(l);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testLearn() {
        DataSet ds = Mockito.mock(DataSet.class);
        LearningRule l = Mockito.mock(LearningRule.class);
        instance.setLearningRule(l);
        instance.learn(ds);
        Mockito.verify(l).learn(ds);
    }

    @Test(expected = NullPointerException.class)
    public void testLearnException() throws Exception {
        DataSet ds = Mockito.mock(DataSet.class);
        instance.learn(ds);
    }

    @Test
    public void testLearnWithLr() {
        DataSet ds = Mockito.mock(DataSet.class);
        LearningRule l = Mockito.mock(LearningRule.class);
        instance.learn(ds, l);
        Mockito.verify(l).learn(ds);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLoad() {
        instance.addLayer(Mockito.mock(Layer.class));
        instance.save("test.nnet");
        NeuralNetwork<LearningRule> nn1;
        try {
            nn1 = NeuralNetwork.load(new FileInputStream("test.nnet"));
            assertTrue(instance.getLayers().size() == nn1.getLayers().size());
        } catch (FileNotFoundException e) {

        }
    }

    @Test
    public void testRandomizeWeights() {
        assertTrue(instance.getWeights().length == 0);
        Layer l1 = new Layer();
        l1.addNeuron(new Neuron());
        l1.addNeuron(new Neuron());
        Layer l2 = new Layer();
        l2.addNeuron(new Neuron());

        instance.addLayer(l1);
        instance.addLayer(l2);

        instance.createConnection(l1.getNeuronAt(0), l2.getNeuronAt(0), 1);
        instance.createConnection(l1.getNeuronAt(1), l2.getNeuronAt(0), 1);

        instance.randomizeWeights(new WeightsRandomizer(new Random(123)));
        Random r = new Random(123);
        assertEquals(r.nextDouble()- 0.5, instance.getWeights()[0], 0.0);
        assertEquals(r.nextDouble()- 0.5, instance.getWeights()[1], 0.0);
    }

    @Test
    public void testRandomizeWeightsMinMax() {
        assertTrue(instance.getWeights().length == 0);
        Layer l1 = new Layer();
        l1.addNeuron(new Neuron());
        l1.addNeuron(new Neuron());
        Layer l2 = new Layer();
        l2.addNeuron(new Neuron());

        instance.addLayer(l1);
        instance.addLayer(l2);

        instance.createConnection(l1.getNeuronAt(0), l2.getNeuronAt(0), 5);
        instance.createConnection(l1.getNeuronAt(1), l2.getNeuronAt(0), 3);
        instance.randomizeWeights(-0.9, 0.9);

        assertTrue(instance.getWeights()[0] < 0.9 && instance.getWeights()[0] > -0.9);
        assertTrue(instance.getWeights()[1] < 0.9 && instance.getWeights()[1] > -0.9);
    }

    @Test
    public void testRemoveLayer() {
        Layer l = Mockito.mock(Layer.class);
        instance.addLayer(l);
        assertTrue(instance.getLayers().get(0) == l);
        instance.removeLayer(l);
        assertTrue(instance.getLayers().isEmpty());
    }

    @Test
    public void testRemoveLayerIndex() {
        instance.addLayer(0, Mockito.mock(Layer.class));
        instance.removeLayerAt(0);
        assertTrue(instance.getLayers().isEmpty());
    }

    @Test
    public void testRemoveListener() {
        NeuralNetworkEventListener l = Mockito.mock(NeuralNetworkEventListener.class);
        instance.addListener(l);
        NeuralNetworkEvent event = Mockito.mock(NeuralNetworkEvent.class);
        instance.fireNetworkEvent(event);
        Mockito.verify(l, Mockito.times(1)).handleNeuralNetworkEvent(event);
        instance.removeListener(l);
        instance.fireNetworkEvent(event);
        Mockito.verify(l, Mockito.times(1)).handleNeuralNetworkEvent(event);
    }

    @Test
    public void testRemovePlugin() {
        PluginBase p = Mockito.mock(PluginBase.class);
        instance.addPlugin(p);
        assertTrue(instance.getPlugin(p.getClass()) == p);
        instance.removePlugin(p.getClass());
        assertNull(instance.getPlugin(p.getClass()));
    }

    @Test
    public void testReset() {
        Layer l = Mockito.mock(Layer.class);
        Layer l1 = Mockito.mock(Layer.class);
        instance.addLayer(l);
        instance.addLayer(l1);
        instance.reset();
        Mockito.verify(l).reset();
        Mockito.verify(l1).reset();
    }

    @Test
    public void testSave() {
        instance.save("test.nnet");
        File f = new File("test.nnet");
        assertTrue(f.exists());
    }

    @Test
    public void testSetInput() {
        Neuron n = Mockito.mock(Neuron.class);
        Neuron n1 = Mockito.mock(Neuron.class);
        instance.setInputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
                add(n1);
            }
        });
        instance.setInput(0, 1);
        Mockito.verify(n).setInput(0);
        Mockito.verify(n1).setInput(1);
    }

    @Test
    public void testSetInputNeurons() {
        Neuron n = Mockito.mock(Neuron.class);
        Neuron n1 = Mockito.mock(Neuron.class);
        instance.setInputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
                add(n1);
            }
        });
        assertTrue(instance.getInputNeurons().get(0) == n);
        assertTrue(instance.getInputNeurons().get(1) == n1);
        assertTrue(instance.getInputNeurons().size() == 2);
    }

    @Test(expected = NullPointerException.class)
    public void testSetInputNeuronsNull() throws Exception {
        instance.setInputNeurons(null);
    }

    @Test
    public void testSetLabel() {
        instance.setLabel("Test");
        assertTrue(instance.getLabel().equals("Test"));
    }

    @Test
    public void testSetLearningRule() {
        LearningRule l = Mockito.mock(LearningRule.class);
        instance.setLearningRule(l);
        assertTrue(instance.getLearningRule() == l);
    }

    @Test
    public void testSetNetworkType() {
        instance.setNetworkType(NeuralNetworkType.PERCEPTRON);
        assertTrue(instance.getNetworkType() == NeuralNetworkType.PERCEPTRON);
        assertTrue(instance.getNetworkType().toString().equals("PERCEPTRON"));
        instance.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        assertTrue(instance.getNetworkType() == NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        assertTrue(instance.getNetworkType().toString().equals("MULTI_LAYER_PERCEPTRON"));
    }

    @Test
    public void testSetOutputLabels() {
        Neuron n = Mockito.mock(Neuron.class);
        Neuron n1 = Mockito.mock(Neuron.class);
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
                add(n1);
            }
        });
        instance.setOutputLabels(new String[]{"a", "b"});
        Mockito.verify(n).setLabel("a");
        Mockito.verify(n1).setLabel("b");
    }

    @Test
    public void testSetOutputNeurons() {
        Neuron n = Mockito.mock(Neuron.class);
        instance.setOutputNeurons(new ArrayList<Neuron>() {
            {
                add(n);
            }
        });
        assertTrue(instance.getOutputNeurons().get(0) == n);
        assertTrue(instance.getOutputNeurons().size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetOutputNeuronsNull() throws Exception {
        instance.setOutputNeurons(null);
    }

    @Test
    public void testSetWeights() {
        assertTrue(instance.getWeights().length == 0);
        Layer l1 = new Layer();
        l1.addNeuron(new Neuron());
        l1.addNeuron(new Neuron());
        Layer l2 = new Layer();
        l2.addNeuron(new Neuron());

        instance.addLayer(l1);
        instance.addLayer(l2);

        instance.createConnection(l1.getNeuronAt(0), l2.getNeuronAt(0), 5);
        instance.createConnection(l1.getNeuronAt(1), l2.getNeuronAt(0), 3);

        instance.setWeights(new double[]{6, 4});

        assertTrue(instance.getWeights().length == 2);
        assertTrue(instance.getWeights()[0] == 6);
        assertTrue(instance.getWeights()[1] == 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullListener() {
        NeuralNetworkEventListener nnlist3 = null;
        NeuralNetworkEvent NNevt = Mockito.mock(NeuralNetworkEvent.class);
        instance.removeListener(nnlist3);
        instance.fireNetworkEvent(NNevt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullListener() {
        NeuralNetworkEventListener nnlist3 = null;
        NeuralNetworkEvent NNevt = Mockito.mock(NeuralNetworkEvent.class);
        instance.addListener(nnlist3);
        instance.fireNetworkEvent(NNevt);
    }

    @Test
    public void shouldCreatefromFileStream() {
        try {
            instance.setLabel("TestNetLabel");
            instance.save("testNet.nnet");
            @SuppressWarnings("rawtypes")
            NeuralNetwork nn = NeuralNetwork.load(new FileInputStream("testNet.nnet"));
            assertEquals(nn.getLabel(), "TestNetLabel");
        } catch (FileNotFoundException e) {
        }
    }

    @Test
    public void shouldCreatefromFile() throws Exception {
        instance.setLabel("TestNetLabel");
        instance.save("testNet.nnet");
        @SuppressWarnings("rawtypes")
        NeuralNetwork nn = NeuralNetwork.createFromFile("testNet.nnet");
        assertEquals(nn.getLabel(), "TestNetLabel");
    }

}
