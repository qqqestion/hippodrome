package ru.emoji.tashkent.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ActionOnEnterListener implements KeyListener {
    private OnKeyClickListenerListener listener;
    private static ActionOnEnterListener instance;

    public ActionOnEnterListener(OnKeyClickListenerListener listener) {
        this.listener = listener;
        setInstance(this);
    }

    public static ActionOnEnterListener getInstance() {
        return instance;
    }

    public static void setInstance(ActionOnEnterListener instance) {
        ActionOnEnterListener.instance = instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            listener.onSave();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
