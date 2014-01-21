package state;

import view.LoggingPanel;
import view.Observable;
import view.Observer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by Yonne on 08/01/14.
 */
public class Watcher implements KeyListener, Observable {
    /**
     * Watches for key-presses
     * The following methods handle the key-events
     * Classes that handle these events are notified
     * when a key is pressed
     */
    private ArrayList<Observer> obsList;
    LoggingPanel log;

    public Watcher(LoggingPanel log)
    {
        this.log = log;
        obsList = new ArrayList<Observer>();
    }
    @Override
    public void NotifyObservers(KeyEvent keyEvent, String input_form)
    {
        log.write("Notifying");
        for(Observer obs : obsList)
        {
            obs.update(keyEvent,input_form);
        }
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        NotifyObservers(e, "pressed");
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        NotifyObservers(e, "released");
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        NotifyObservers(e, "typed");
    }

    public void add_observer(Observer obs)
    {
        if (obs != null)
            obsList.add(obs);
    }

    public void del_observer(Observer obs)
    {
        if (obs != null)
            obsList.remove(obs);
    }
}
