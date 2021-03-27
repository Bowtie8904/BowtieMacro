package core.macro;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

import bt.bot.exc.BotActionFormatException;
import bt.io.xml.XML;
import bt.io.xml.Xmlable;
import core.macro.obj.MacroSet;

/**
 * @author &#8904
 *
 */
public class MacroManager implements Xmlable
{
    private List<MacroSet> sets;

    public MacroManager()
    {
        this.sets = new ArrayList<>();
    }

    public List<MacroSet> getMacroSets()
    {
        return this.sets;
    }

    public void init(Node managerNode) throws BotActionFormatException
    {
        this.sets.clear();
        MacroSet set = null;
        List<Node> setNodes = managerNode.selectNodes("//" + XML.lowerNode("MacroSet"));

        for (Node node : setNodes)
        {
            set = new MacroSet();
            set.init(node);
            this.sets.add(set);
        }
    }

    /**
     * @see bt.utils.xml.Xmlable#toXML()
     */
    @Override
    public Document toXML()
    {
        var setsElement = XML.element("MacroSets");

        for (MacroSet set : this.sets)
        {
            setsElement.addElement(set.toXML());
        }

        return XML.builder().addElement(setsElement).toXML();
    }

    @Override
    public String toString()
    {
        String value = "MacroManager {";

        for (var set : this.sets)
        {
            value += set.toString() + ", ";
        }

        if (!this.sets.isEmpty())
        {
            value = value.substring(0, value.length() - 2);
        }

        value += "}";

        return value;
    }
}