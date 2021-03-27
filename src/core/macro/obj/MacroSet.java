package core.macro.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bt.scheduler.Threads;
import org.dom4j.Document;
import org.dom4j.Node;

import bt.bot.BotActionExecutor;
import bt.bot.BotKey;
import bt.bot.exc.BotActionFormatException;
import bt.io.xml.XML;
import bt.io.xml.Xmlable;
import bt.key.KeyAction;
import bt.key.KeyBoardHook;
import core.config.Configuration;

/**
 * @author &#8904
 *
 */
public class MacroSet implements Xmlable
{
    private List<Macro> macros;
    private String name;
    private String triggerKey;
    private boolean enabled;
    private BotActionExecutor executor;

    public MacroSet()
    {
        this.macros = new ArrayList<>();
    }

    public void execute()
    {
        if (Configuration.get().isActive())
        {
            Threads.get().executeCached(() -> {
                this.executor = new BotActionExecutor();
                this.executor.execute(this.macros.stream().map(Macro::getAction).collect(Collectors.toList()));
            }, "Macro (" + name + ")");
        }
    }

    public void init(Node setNode) throws BotActionFormatException
    {
        this.name = setNode.valueOf(XML.lowerAttribute("Name"));
        this.triggerKey = setNode.valueOf(XML.lowerAttribute("TriggerKey"));
        this.enabled = Boolean.parseBoolean(setNode.valueOf(XML.lowerAttribute("Enabled")));
        String cancelKey = setNode.valueOf(XML.lowerAttribute("CancelKey"));

        if (cancelKey != null && !cancelKey.isEmpty())
        {
            KeyAction action = null;
            int[] key = getKeyAndModifier(cancelKey);

            action = new KeyAction(key[0], key[1], e -> {
                if (this.executor != null)
                {
                    this.executor.stop();
                }
            });

            KeyBoardHook.get().addKeyAction(action);
        }

        Macro macro = null;
        List<Node> macroNodes = setNode.selectNodes("./" + XML.lowerNode("Macro"));

        for (Node node : macroNodes)
        {
            macro = new Macro();
            macro.init(node);
            this.macros.add(macro);
        }

        if (this.enabled)
        {
            KeyAction action = null;
            int[] key = getKeyAndModifier(this.triggerKey);

            action = new KeyAction(key[0], key[1], e -> execute());

            KeyBoardHook.get().addKeyAction(action);
        }
    }

    private int[] getKeyAndModifier(String keyLiteral)
    {
        String[] keyParts = keyLiteral.toLowerCase().trim().split(" ");
        String key = null;
        String modifier = null;

        int keyCode = 0;
        int modifierCode = KeyAction.NO_MODIFIER;

        if (keyParts.length > 1)
        {
            modifier = keyParts[0];
            key = keyParts[1];
        }
        else
        {
            key = keyParts[0];
        }

        keyCode = BotKey.forLiteral(key).getCode();

        if (modifier != null)
        {
            switch (modifier)
            {
                case "shift":
                    modifierCode = KeyAction.SHIFT_MODIFIER;
                    break;
                case "ctrl":
                    modifierCode = KeyAction.CTRL_MODIFIER;
                    break;
                case "alt":
                    modifierCode = KeyAction.ALT_MODIFIER;
                    break;
            }
        }

        return new int[] {keyCode, modifierCode};
    }

    /**
     * @return the macros
     */
    public List<Macro> getMacros()
    {
        return this.macros;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the triggerKey
     */
    public String getTriggerKey()
    {
        return this.triggerKey;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled()
    {
        return this.enabled;
    }

    /**
     * @see bt.utils.xml.Xmlable#toXML()
     */
    @Override
    public Document toXML()
    {
        var setElement = XML.element("MacroSet")
                            .addAttribute("Name", this.name)
                            .addAttribute("TriggerKey", this.triggerKey);

        for (Macro macro : this.macros)
        {
            setElement.addElement(macro.toXML());
        }

        return XML.builder().addElement(setElement).toXML();
    }

    @Override
    public String toString()
    {
        String value = "MacroSet name=" + this.name + ", triggerKey=" + this.triggerKey + ", enabled=" + this.enabled + " {";

        for (var macro : this.macros)
        {
            value += macro.toString() + ", ";
        }

        if (!this.macros.isEmpty())
        {
            value = value.substring(0, value.length() - 2);
        }

        value += "}";

        return value;
    }
}