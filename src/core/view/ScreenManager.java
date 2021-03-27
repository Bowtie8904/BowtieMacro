package core.view;

import bt.gui.fx.core.FxScreenManager;

/**
 * @author &#8904
 *
 */
public class ScreenManager extends FxScreenManager
{
    /**
     * @see bt.gui.fx.core.FxScreenManager#loadScreens()
     */
    @Override
    protected void loadScreens()
    {
    }

    /**
     * @see bt.gui.fx.core.FxScreenManager#startApplication()
     */
    @Override
    protected void startApplication()
    {
        setScreen(ListScreen.class);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}