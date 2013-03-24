package org.neuroph.netbeans.main;

/**
 *
 * @author zoran
 */
        public class LearningInfo {
            Integer iteration;
            Double error;


            public LearningInfo(Integer iteration, Double error) {
                this.iteration = iteration;
                this.error = error;
            }

            public Double getError() {
                return error;
            }

            public Integer getIteration() {
                return iteration;
            }

        }