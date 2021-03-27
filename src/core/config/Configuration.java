package core.config;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.Node;

import bt.io.xml.XML;
import bt.io.xml.Xmlable;
import bt.key.KeyBoardHook;
import bt.log.Logger;
import core.macro.MacroManager;

/**
 * @author &#8904
 *
 */
public class Configuration implements Xmlable
{
    private static volatile Configuration instance;

    public static Configuration get()
    {
        return instance;
    }

    private MacroManager macroManager;
    private File configFile;
    private boolean isActive = true;

    public Configuration(File configFile)
    {
        this.configFile = configFile;
        instance = this;
    }

    /**
     * @return the isActive
     */
    public boolean isActive()
    {
        return this.isActive;
    }

    /**
     * @param isActive
     *            the isActive to set
     */
    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    /**
     * @return the macroManager
     */
    public MacroManager getMacroManager()
    {
        return this.macroManager;
    }

    /**
     * @param macroManager
     *            the macroManager to set
     */
    public void setMacroManager(MacroManager macroManager)
    {
        this.macroManager = macroManager;
    }

    public void load()
    {
        try
        {
            var doc = XML.parse(this.configFile);

            Node loggerNode = doc.selectSingleNode("//" + XML.lowerNode("Logger"));
            Logger.global().setEnabled(Boolean.parseBoolean(loggerNode.valueOf(XML.lowerAttribute("Enabled"))));

            KeyBoardHook.get().clearKeyActions();
            this.macroManager.init(doc.selectSingleNode("//" + XML.lowerNode("MacroSets")));
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
    }

    public void save()
    {
        try
        {
            XML.save(toXML(), this.configFile);
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }
    }

    /**
     * @see bt.utils.xml.Xmlable#toXML()
     */
    @Override
    public Document toXML()
    {
        var configElement = XML.element("MacroConfiguration");
        configElement.addElement(this.macroManager);

        return XML.builder().addElement(configElement).toXML();
    }
}