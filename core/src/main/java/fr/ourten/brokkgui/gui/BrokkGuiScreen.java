package fr.ourten.brokkgui.gui;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import fr.ourten.brokkgui.GuiFocusManager;
import fr.ourten.brokkgui.internal.IBrokkGuiImpl;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.panel.GuiPane;
import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;

public class BrokkGuiScreen
{
    private GuiPane                       mainPanel;
    private final ArrayList<SubGuiScreen> windows;
    private IGuiRenderer                  renderer;

    private final BaseProperty<Float>     widthProperty, heightProperty, xPosProperty, yPosProperty;

    private final BaseProperty<Float>     xRelativePosProperty, yRelativePosProperty;

    private final BaseProperty<Integer>   screenWidthProperty, screenHeightProperty;

    private IBrokkGuiImpl                 wrapper;

    public BrokkGuiScreen(final float xRelativePos, final float yRelativePos, final float width, final float height)
    {
        this.widthProperty = new BaseProperty<>(width, "widthProperty");
        this.heightProperty = new BaseProperty<>(height, "heightProperty");

        this.xRelativePosProperty = new BaseProperty<>(xRelativePos, "xRelativePosProperty");
        this.yRelativePosProperty = new BaseProperty<>(yRelativePos, "yRelativePosProperty");

        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        this.windows = Lists.newArrayList();

        this.screenWidthProperty = new BaseProperty<>(0, "screenWidthProperty");
        this.screenHeightProperty = new BaseProperty<>(0, "screenHeightProperty");

        this.setMainPanel(new GuiPane());
    }

    public BrokkGuiScreen(final float width, final float height)
    {
        this(0, 0, width, height);
    }

    public BrokkGuiScreen()
    {
        this(0, 0);
    }

    public void setWrapper(final IBrokkGuiImpl wrapper)
    {
        this.wrapper = wrapper;

        this.renderer = wrapper.getRenderer();

        this.xPosProperty.bind(new BaseBinding<Float>()
        {
            {
                super.bind(BrokkGuiScreen.this.getScreenWidthProperty());
                super.bind(BrokkGuiScreen.this.xRelativePosProperty);
                super.bind(BrokkGuiScreen.this.widthProperty);
            }

            @Override
            public Float computeValue()
            {
                return BrokkGuiScreen.this.getScreenWidthProperty().getValue()
                        / (1 / BrokkGuiScreen.this.xRelativePosProperty.getValue())
                        - BrokkGuiScreen.this.widthProperty.getValue() / 2;
            }
        });
        this.yPosProperty.bind(new BaseBinding<Float>()
        {
            {
                super.bind(BrokkGuiScreen.this.yRelativePosProperty);
                super.bind(BrokkGuiScreen.this.getScreenHeightProperty());
                super.bind(BrokkGuiScreen.this.heightProperty);
            }

            @Override
            public Float computeValue()
            {
                return BrokkGuiScreen.this.getScreenHeightProperty().getValue()
                        / (1 / BrokkGuiScreen.this.yRelativePosProperty.getValue())
                        - BrokkGuiScreen.this.heightProperty.getValue() / 2;
            }
        });
    }

    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        for (final EGuiRenderPass pass : EGuiRenderPass.VALUES)
        {
            this.renderer.beginPass(pass);
            this.mainPanel.renderNode(this.renderer, pass, mouseX, mouseY);
            this.renderer.endPass(pass);
        }

        if (!this.windows.isEmpty())
            for (int i = this.windows.size() - 1; i >= 0; i--)
            {
                if (this.windows.get(i).hasWarFog())
                    this.renderer.getHelper().drawColoredRect(this.renderer, 0, 0, this.getWidth(), this.getHeight(),
                            5 + i, Color.BLACK.addAlpha(-0.5f));
                this.windows.get(i).setzLevel(5 + i);

                for (final EGuiRenderPass pass : EGuiRenderPass.VALUES)
                {
                    this.renderer.beginPass(pass);
                    this.windows.get(i).renderNode(this.renderer, pass, mouseX, mouseY);
                    this.renderer.endPass(pass);
                }
            }
    }

    public void initGui()
    {
    }

    public void onClick(final int mouseX, final int mouseY, final int key)
    {
        if (!this.windows.isEmpty())
        {
            if (this.windows.get(0).isPointInside(mouseX, mouseY))
                this.windows.get(0).handleClick(mouseX, mouseY, key);
            else if (this.windows.get(0).closeOnClick())
                this.removeSubGui(this.windows.get(0));
        }
        else if (this.mainPanel.isPointInside(mouseX, mouseY))
            this.mainPanel.handleClick(mouseX, mouseY, key);
    }

    public void handleMouseInput()
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
            GuiFocusManager.getInstance().getFocusedNode().handleMouseInput();
    }

    public void onKeyTyped(final char c, final int key)
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
            GuiFocusManager.getInstance().getFocusedNode().handleKeyInput(c, key);
    }

    public void addSubGui(final SubGuiScreen subGui)
    {
        this.windows.add(0, subGui);
        subGui.open();
    }

    public void removeSubGui(final SubGuiScreen subGui)
    {
        subGui.close();
        this.windows.remove(subGui);
    }

    public boolean hasSubGui(final SubGuiScreen subGui)
    {
        return this.windows.contains(subGui);
    }

    public void close()
    {
        this.wrapper.closeGui();
    }

    public GuiPane getMainPanel()
    {
        return this.mainPanel;
    }

    public void setMainPanel(final GuiPane mainPanel)
    {
        if (this.mainPanel != null)
        {
            this.mainPanel.getWidthProperty().unbind();
            this.mainPanel.getHeightProperty().unbind();
            this.mainPanel.getxPosProperty().unbind();
            this.mainPanel.getyPosProperty().unbind();
        }

        this.mainPanel = mainPanel;

        this.mainPanel.getWidthProperty().bind(this.widthProperty);
        this.mainPanel.getHeightProperty().bind(this.heightProperty);

        this.mainPanel.getxPosProperty().bind(this.xPosProperty);
        this.mainPanel.getyPosProperty().bind(this.yPosProperty);
    }

    public BaseProperty<Float> getWidthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> getHeightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> getxPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> getyPosProperty()
    {
        return this.yPosProperty;
    }

    public BaseProperty<Float> getxRelativePosProperty()
    {
        return this.xRelativePosProperty;
    }

    public BaseProperty<Float> getyRelativePosProperty()
    {
        return this.yRelativePosProperty;
    }

    public float getWidth()
    {
        return this.widthProperty.getValue();
    }

    public float getHeight()
    {
        return this.heightProperty.getValue();
    }

    public void setWidth(final float width)
    {
        this.widthProperty.setValue(width);
    }

    public void setHeight(final float height)
    {
        this.heightProperty.setValue(height);
    }

    public float getxPos()
    {
        return this.xPosProperty.getValue();
    }

    public void setxPos(final float xPos)
    {
        this.xPosProperty.setValue(xPos);
    }

    public float getyPos()
    {
        return this.yPosProperty.getValue();
    }

    public void setyPos(final float yPos)
    {
        this.yPosProperty.setValue(yPos);
    }

    public float getxRelativePos()
    {
        return this.xRelativePosProperty.getValue();
    }

    public void setxRelativePos(final float xRelativePos)
    {
        this.xRelativePosProperty.setValue(xRelativePos);
    }

    public float getyRelativePos()
    {
        return this.yRelativePosProperty.getValue();
    }

    public void setyRelativePos(final float yRelativePos)
    {
        this.yRelativePosProperty.setValue(yRelativePos);
    }

    public BaseProperty<Integer> getScreenWidthProperty()
    {
        return this.screenWidthProperty;
    }

    public BaseProperty<Integer> getScreenHeightProperty()
    {
        return this.screenHeightProperty;
    }
}