package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.ListProperty;

public interface IStyleable
{
    String getID();

    ListProperty<String> getStyleClasses();

    StyleHolder getStyleHolder();

    void setID(String id);

    void setStyle(StyleHolder style);
}
