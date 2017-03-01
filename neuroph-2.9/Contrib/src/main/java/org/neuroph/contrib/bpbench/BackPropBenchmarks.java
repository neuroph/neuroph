/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Mladen
 */
public class BackPropBenchmarks {

    public static void main(String[] args) throws IOException {
        RunBackpropagation x = new RunBackpropagation();
        RunQuickPropagation q = new RunQuickPropagation();
        
        PrintWriter out = new PrintWriter(new FileWriter("rezultati.txt"));
        int backprop = 0;
        int quickprop = 0;
//       for (int i = 0; i < 5; i++) {
//            x.nauci();
//            out.println("BACK! iter: " + x.noOfIter);
//            backprop += x.noOfIter;
//        }
        out.println("BACK! ukupno iteracija: " + backprop);
       for (int i = 0; i < 5; i++) {
            q.nauci();
            out.println("QUICK! iter: " + q.noOfIter);
            quickprop += q.noOfIter;
        }
        out.println("QUICK! ukupno iteracija: " + quickprop);
        out.flush();
        out.close();

    }
}
