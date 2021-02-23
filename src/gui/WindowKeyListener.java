package gui;

import logic.Habitat;
import logic.Main;

import java.awt.event.*;

public class WindowKeyListener implements KeyListener {
    WindowKeyListener(){
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Invoked when a key has been typed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_B -> Habitat.instance().startSimulation();
            case KeyEvent.VK_E -> Habitat.instance().stopSimulation();
            case KeyEvent.VK_T -> WindowMain.Instance().setShowSimulationTime(!WindowMain.Instance().getShowSimulationTime());
        }
        Main.printLog(Integer.toString(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
