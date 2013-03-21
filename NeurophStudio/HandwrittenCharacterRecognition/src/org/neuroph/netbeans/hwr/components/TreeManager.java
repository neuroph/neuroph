/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neuroph.netbeans.hwr.components;

import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.neuroph.netbeans.project.CurrentProject;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Damir Kocic
 */
public class TreeManager {

    private static DefaultTreeModel model;
    private static final String PATH = "Letters";
     //private static final String PATH = "Characters";

    /**
     * This method sets the model for the given tree
     *
     * @param tree the jTree whose model needs to be set
     */
    public static void setLettersTreeModel(JTree tree) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(PATH);
        File rootFolder = new File(getPath());
        File[] files = rootFolder.listFiles();
        if (files == null) {
            tree.setModel(model);
        } else {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    root.add(new DefaultMutableTreeNode(files[i].getName()));
                }
            }
        }
        model = new DefaultTreeModel(root);
        tree.setModel(model);
    }

    /**
     * Retruns the tree model
     *
     * @return DefaultTreeModel the model for the tree
     */
    public static DefaultTreeModel getLettersModel() {
        return model;
    }

    /**
     * Returns the String that represends the path to the folder whose data
     * needs to be show
     *
     * @return String that represends the path to the folder
     */
    public static String getPath() {
        return  ((FileObject)CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/" + PATH;
//        return PATH;
    }
}
