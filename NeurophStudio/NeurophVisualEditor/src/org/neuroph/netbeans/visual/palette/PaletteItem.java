package org.neuroph.netbeans.visual.palette;

/**
 *
 * @author Zoran Sevarac
 */
public class PaletteItem {

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
