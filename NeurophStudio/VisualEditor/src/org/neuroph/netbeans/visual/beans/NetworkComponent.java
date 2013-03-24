/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.beans;

/**
 *
 * @author Kasia
 */
public class NetworkComponent {

    Integer id;
    String icon;
    String title;
    String category;

    public NetworkComponent() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
