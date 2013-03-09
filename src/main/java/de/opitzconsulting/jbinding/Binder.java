package de.opitzconsulting.jbinding;

import javax.swing.JComponent;

public final class Binder {

    public static Binding bind(JComponent component) {
        return new Binding(component);
    }

    private Binder() {
        // Nicht instantiierbare Util-Klasse
    }

}
