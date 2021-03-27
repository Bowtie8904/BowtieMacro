package core.view;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import bt.gui.fx.core.FxScreen;
import bt.gui.fx.core.annot.FxmlElement;
import bt.gui.fx.core.annot.css.FxStyleClass;
import bt.gui.fx.core.annot.handl.FxHandler;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnAction;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseEntered;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseExited;
import bt.gui.fx.core.annot.setup.FxSetup;
import bt.gui.fx.util.ButtonHandling;
import bt.gui.fx.util.CssUtils;
import core.config.Configuration;
import core.css.ButtonCss;
import core.macro.MacroManager;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author &#8904
 *
 */
@FxStyleClass(ButtonCss.class)
public class ListScreen extends FxScreen
{
    @FxmlElement
    private JFXListView list;

    @FxmlElement
    @FxSetup(css = ButtonCss.BLUE_BACKGROUND)
    @FxHandler(type = FxOnAction.class, method = "requestLoad", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton loadButton;

    @FxmlElement
    @FxSetup(css = ButtonCss.GREEN_BACKGROUND)
    @FxHandler(type = FxOnAction.class, method = "toggleActive", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton toggleButton;

    private MacroManager macroManager;

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScreen()
     */
    @Override
    protected void prepareScreen()
    {
        loadMacros();
    }

    public void loadMacros()
    {
        List<String> macros = new ArrayList<>();

        String macroString = "";

        for (var set : this.macroManager.getMacroSets())
        {
            macroString = "";

            if (!set.isEnabled())
            {
                macroString = "(disabled) ";
            }

            macroString += "[" + set.getTriggerKey() + "]  -  " + set.getName();

            macros.add(macroString);
        }

        this.list.setItems(FXCollections.observableArrayList(macros));
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareStage(javafx.stage.Stage)
     */
    @Override
    protected void prepareStage(Stage stage)
    {
        stage.setTitle("Macro Manager");
        setIcons("/icon.png");
    }

    @Override
    public void kill()
    {
        super.kill();
        System.exit(0);
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScene(javafx.scene.Scene)
     */
    @Override
    protected void prepareScene(Scene scene)
    {
    }

    public void setMacroManager(MacroManager manager)
    {
        this.macroManager = manager;
    }

    private void requestLoad()
    {
        Configuration.get().load();
        loadMacros();
    }

    private void toggleActive()
    {
        Configuration.get().setActive(!Configuration.get().isActive());

        if (Configuration.get().isActive())
        {
            CssUtils.addStyleClass(this.toggleButton, ButtonCss.GREEN_BACKGROUND);
            CssUtils.removeStyleClass(this.toggleButton, ButtonCss.RED_BACKGROUND);
        }
        else
        {
            CssUtils.addStyleClass(this.toggleButton, ButtonCss.RED_BACKGROUND);
            CssUtils.removeStyleClass(this.toggleButton, ButtonCss.GREEN_BACKGROUND);
        }
    }
}