package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseMapProperty;
import fr.ourten.teabeans.value.MapProperty;

public class StyleHolder
{
    private final IStyleable                  parent;
    private final MapProperty<String, Object> propreties;

    public StyleHolder(IStyleable parent)
    {
        this.parent = parent;
        this.propreties = new BaseMapProperty<>(null);
    }

    public Object getStyle(String key)
    {
        if (this.propreties.containsKey(key))
            return this.propreties.get(key);
        else if (this.parent != null && this.parent.getStyleHolder() != null)
            return this.parent.getStyleHolder().getStyle(key);
        else
            return null;
    }
}
