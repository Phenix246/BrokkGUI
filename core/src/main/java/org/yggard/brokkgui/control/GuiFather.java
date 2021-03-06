package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseListProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.policy.EOverflowPolicy;

import java.util.List;

public class GuiFather extends GuiNode
{
    private final BaseListProperty<GuiNode> childrensProperty;

    private EOverflowPolicy                 overflowPolicy;

    public GuiFather()
    {
        this.childrensProperty = new BaseListProperty<>(null, "childrensProperty");

        this.overflowPolicy = EOverflowPolicy.NONE;
    }

    /**
     * @return an immutable list
     */
    public List<GuiNode> getChildrens()
    {
        return this.getChildrensProperty().getValue();
    }

    protected BaseListProperty<GuiNode> getChildrensProperty()
    {
        return this.childrensProperty;
    }

    public EOverflowPolicy getOverflowPolicy()
    {
        return this.overflowPolicy;
    }

    public void setOverflowPolicy(final EOverflowPolicy overflowPolicy)
    {
        this.overflowPolicy = overflowPolicy;
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);

        if ((this.getOverflowPolicy() == EOverflowPolicy.TRIM && pass == EGuiRenderPass.MAIN)
                || (pass == EGuiRenderPass.SPECIAL && this.getOverflowPolicy() == EOverflowPolicy.TRIM_ALL))
        {
            renderer.getHelper().beginScissor();
            renderer.getHelper().scissorBox(this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getxPos() + this.getxTranslate() + this.getWidth(),
                    this.getyPos() + this.getyTranslate() + this.getHeight());
            this.getChildrens().forEach(child -> child.renderNode(renderer, pass, mouseX, mouseY));
            renderer.getHelper().endScissor();
        }
        else
            this.getChildrens().forEach(child -> child.renderNode(renderer, pass, mouseX, mouseY));
    }

    @Override
    public void handleClick(final int mouseX, final int mouseY, final int key)
    {
        super.handleClick(mouseX, mouseY, key);
        this.getChildrens().stream().filter(node -> node.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleClick(mouseX, mouseY, key));
    }
}