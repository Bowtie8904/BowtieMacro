package core;

import java.awt.event.KeyEvent;
import java.io.File;

import bt.bot.BotKey;
import bt.gui.fx.core.instance.ScreenInstanceDispatcher;
import bt.key.KeyBoardHook;
import bt.log.Logger;
import core.config.Configuration;
import core.macro.MacroManager;
import core.view.ListScreen;
import core.view.ScreenManager;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static void main(String[] args)
    {
        Logger.global().hookSystemOut();
        Logger.global().hookSystemErr();
        var config = new Configuration(new File("./config.xml"));
        var macroManager = new MacroManager();
        config.setMacroManager(macroManager);
        config.load();

        for (var k : BotKey.keys)
        {
            Logger.global().print(k.getLiteral());
        }

        Logger.global().print(macroManager.toString());

        ScreenInstanceDispatcher.get().subscribeTo(ListScreen.class, screen ->
        {
            screen.setMacroManager(macroManager);
        });

        ScreenManager.main(args);
    }
}