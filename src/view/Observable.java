package view;

import java.awt.event.KeyEvent;

/**
 * Created by Yonne on 07/01/14.
 */
    public interface Observable
    {
        public void NotifyObservers(KeyEvent keyEvent, String input_form);
    }

