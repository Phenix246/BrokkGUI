package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.hermod.EventType;

public class DisableEvent extends GuiStateEvent
{
    public static final EventType<DisableEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "DISABLE_STATE_EVENT");

    private final boolean                       disabled;

    public DisableEvent(final GuiNode source)
    {
        this(source, false);
    }

    public DisableEvent(final GuiNode source, final boolean disabled)
    {
        super(source);

        this.disabled = disabled;
    }

    public boolean isDisabled()
    {
        return this.disabled;
    }
}