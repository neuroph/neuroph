package org.neuroph.core.learning;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.neuroph.core.data.DataSet;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LearningRuleTest {

    public final LearningRule testableLearningRule = new LearningRule() {
        @Override
        public void learn(DataSet trainingSet) {
            //do nothing - it's only test
        }};

    @Test
    public void testSetGetTrainingData() {
        DataSet trainingSet = new DataSet(0);
        testableLearningRule.setTrainingSet(trainingSet);
        Assert.assertEquals(testableLearningRule.getTrainingSet(), trainingSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddListenerThrowExceptionIfListerNull() {
        testableLearningRule.addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveListenerThrowExceptionIfListerNull() {
        testableLearningRule.removeListener(null);
    }

    @Test
    public void testIsStopped() {
        assertFalse(testableLearningRule.isStopped());
        testableLearningRule.stopLearning();
        assertTrue(testableLearningRule.isStopped());
    }

}