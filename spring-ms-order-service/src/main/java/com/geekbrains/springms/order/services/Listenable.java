package com.geekbrains.springms.order.services;

import java.util.ArrayList;
import java.util.List;

public abstract class Listenable {

    private final List<Listener> LISTENERS = new ArrayList<>();

    public void attach(Listener listener) {
        LISTENERS.add(listener);
    }

    public void detach(Listener listener) {
        LISTENERS.remove(listener);
    }

    protected void notify(Object arg) {
        for (Listener listener : LISTENERS) {
            listener.update(arg);
        }
    }
}
