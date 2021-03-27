package core.macro.obj;

import org.dom4j.Document;
import org.dom4j.Node;

import bt.bot.BotActionSetReader;
import bt.bot.action.BotAction;
import bt.bot.exc.BotActionFormatException;
import bt.io.xml.XML;
import bt.io.xml.Xmlable;
/**
 * @author &#8904
 *
 */
public class Macro implements Xmlable
{
    private BotAction action;

    public BotAction getAction()
    {
        return this.action;
    }

    public void init(Node macroNode) throws BotActionFormatException
    {
        String keyword = macroNode.valueOf(XML.lowerAttribute("Keyword"));
        String value = macroNode.valueOf(XML.lowerAttribute("Value"));

        var parser = new BotActionSetReader();

        this.action = parser.createBotAction(keyword, value);
    }

    /**
     * @see bt.utils.xml.Xmlable#toXML()
     */
    @Override
    public Document toXML()
    {
        var macroElement = XML.element("Macro")
                              .addAttribute("Keyword", this.action.getKeyword())
                              .addAttribute("Value", this.action.getValue());

        return XML.builder().addElement(macroElement).toXML();
    }

    @Override
    public String toString()
    {
        return this.action.toString();
    }
}