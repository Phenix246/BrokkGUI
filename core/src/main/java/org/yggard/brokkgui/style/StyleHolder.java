package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseMapProperty;
import fr.ourten.teabeans.value.MapProperty;

public class StyleHolder
{
    private final IStyleable                            container;
    private IStyleable                                  parent;
    private final MapProperty<String, StyleProperty<?>> propreties;

    public StyleHolder(final IStyleable container)
    {
        this.container = container;
        this.propreties = new BaseMapProperty<>(null);
    }

    public StyleProperty<?> getStyle(final String key)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("key not valid");
        if (this.propreties.containsKey(key))
            return this.propreties.get(key);
        else if (this.parent != null && this.parent.getStyleHolder() != null)
            return this.parent.getStyleHolder().getStyle(key);
        else
            return null;
    }

    public void addStyle(final String key, final StyleProperty<?> value)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("key not valid");
        this.propreties.put(key, value);
    }

    public IStyleable getContainer()
    {
        return this.container;
    }

    public IStyleable getParent()
    {
        return this.parent;
    }

    public void setParent(final IStyleable parent)
    {
        this.parent = parent;
    }
}
