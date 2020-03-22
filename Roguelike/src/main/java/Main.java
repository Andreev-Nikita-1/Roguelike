import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import com.googlecode.lanterna.terminal.virtual.DefaultVirtualTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.lanterna.input.KeyType.*;


public class Main {
    public static void main(String[] args) throws IOException {
        AppLogic.initialize();
        Controller.initialize();
        new Thread(() -> {
            try {
                Controller.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
//        Controller.mainController.run();
//        Controller.mainController.drawMap();
    }
}
