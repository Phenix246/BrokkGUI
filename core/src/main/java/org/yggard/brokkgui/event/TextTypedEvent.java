package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.hermod.EventType;

public class TextTypedEvent extends GuiInputEvent
{
    public static final EventType<TextTypedEvent> TYPE = new EventType<>(GuiInputEvent.ANY, "TEXT_INPUT_EVENT");

    private final String                          oldText, newText;

    public TextTypedEvent(final GuiNode source)
    {
        this(source, "", "");
    }

    public TextTypedEvent(final GuiNode source, final String oldText, final String newText)
    {
        super(source);
        this.oldText = oldText;
        this.newText = newText;
    }

    public String getOldText()
    {
        return this.oldText;
    }

    public String getNewText()
    {
        return this.newText;
    }
}