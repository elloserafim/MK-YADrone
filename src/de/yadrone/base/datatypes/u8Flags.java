/*
 *  Copyright (C) 2010-2011 by Claas Anders "CaScAdE" Rathje
 *  admiralcascade@gmail.com
 *  Licensed under: Creative Commons / Non Commercial / Share Alike
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/
 *
 */
package de.yadrone.base.datatypes;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 8bit unsigned int which is used as bit-flag
 *
 * @author Claas Anders "CaScAdE" Rathje
 */
public class u8Flags extends u8 {

    String[] labels;
    ArrayList<JCheckBox> checkboxes;
    JPanel checkboxpanel;

    public u8Flags() {
        super();
    }

    public u8Flags(String name) {
        super(name);
    }

    public u8Flags(String name, String[] labels) {
        super(name);
        this.labels = labels;
    }
}

