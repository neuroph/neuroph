package org.neuroph.netbeans.visual.palette;

/**
 *
 * @author Zoran Sevarac
 */
public class PalleteItem {

    String icon;
    String title;
    String category;
    Class dropClass;

    public Class getDropClass() {
        return dropClass;
    }

    public void setDropClass(Class dropClass) {
        this.dropClass = dropClass;
    }

    public PalleteItem() {
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


    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }
}
