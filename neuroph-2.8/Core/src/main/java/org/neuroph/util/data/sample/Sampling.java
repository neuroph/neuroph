package org.neuroph.util.data.sample;

import org.neuroph.core.data.DataSet;

/**
 *
 * @author zoran
 */
public interface Sampling {
    public DataSet[] sample(DataSet dataSet);
}
