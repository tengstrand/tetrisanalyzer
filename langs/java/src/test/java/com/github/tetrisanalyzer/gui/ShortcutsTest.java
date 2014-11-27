package com.github.tetrisanalyzer.gui;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.junit.Test;

import java.util.Map;
import java.util.Stack;

import static com.github.tetrisanalyzer.gui.ZoomCalculator.ZoomWindow;
import static junit.framework.Assert.assertEquals;

public class ShortcutsTest {

    @Test
    public void nothingToExport() {
        Shortcuts shortcuts = new Shortcuts();

        String result = shortcuts.export();
        assertEquals("", result);
    }

    @Test
    public void export() {
        Shortcuts shortcuts = new Shortcuts();

        Stack<ZoomWindow> windows2 = new Stack<>();
        windows2.push(new ZoomWindow(0, 0, 1, 1));
        windows2.push(new ZoomWindow(0.5, 0.6, 0.7, 0.8));
        windows2.push(new ZoomWindow(0.55, 0.66, 0.77, 0.88));
        shortcuts.set(2, windows2);

        Stack<ZoomWindow> windows5 = new Stack<>();
        windows5.push(new ZoomWindow(0, 0, 1, 1));
        windows5.push(new ZoomWindow(0.3, 0.4, 0.5, 0.6));
        windows5.push(new ZoomWindow(0.33, 0.44, 0.55, 0.66));
        shortcuts.set(5, windows5);

        String result = shortcuts.export();

        assertEquals("windows:\n" +
                "  key-2:\n" +
                "    window-1: [0.5, 0.6, 0.7, 0.8]\n" +
                "    window-2: [0.55, 0.66, 0.77, 0.88]\n" +
                "  key-5:\n" +
                "    window-1: [0.3, 0.4, 0.5, 0.6]\n" +
                "    window-2: [0.33, 0.44, 0.55, 0.66]\n", result);
    }

    @Test
    public void importWindows() throws YamlException {
        String input =
                "key-2:\n" +
                "  window-1: [0.5, 0.6, 0.7, 0.8]\n" +
                "  window-2: [0.55, 0.66, 0.77, 0.88]\n" +
                "  window-3: [0.555, 0.666, 0.777, 0.888]\n" +
                "key-5:\n" +
                "  window-1: [0.3, 0.4, 0.5, 0.6]\n" +
                "  window-2: [0.33, 0.44, 0.55, 0.66]\n";

        Map map = (Map) new YamlReader(input).read();

        Shortcuts shortcuts = new Shortcuts(map);

        String output = shortcuts.export();

        assertEquals("windows:\n" +
                "  key-2:\n" +
                "    window-1: [0.5, 0.6, 0.7, 0.8]\n" +
                "    window-2: [0.55, 0.66, 0.77, 0.88]\n" +
                "    window-3: [0.555, 0.666, 0.777, 0.888]\n" +
                "  key-5:\n" +
                "    window-1: [0.3, 0.4, 0.5, 0.6]\n" +
                "    window-2: [0.33, 0.44, 0.55, 0.66]\n", output);
    }
}
