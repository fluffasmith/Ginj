package info.ginj.ui;

import info.ginj.CaptureEditingFrame;
import info.ginj.Prefs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ColorSelectorWindow extends JWindow {

    private final CaptureEditingFrame frame;

    public ColorSelectorWindow(CaptureEditingFrame captureEditingFrame) {
        super(captureEditingFrame);
        this.frame = captureEditingFrame;

        final GinjBorderedPanel colorPanel = new GinjBorderedPanel();
        getContentPane().add(colorPanel);

        colorPanel.setLayout(new GridBagLayout());
        GridBagConstraints c;

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 2, 2));
        final Color currentColor = captureEditingFrame.getCurrentColor();
        for (int buttonNumber = 0; buttonNumber < 9; buttonNumber++) {
            // Get color for this button
            final Color buttonColor = Prefs.getColorWithSuffix(Prefs.Key.FIXED_PALETTE_COLOR_PREFIX, String.valueOf(buttonNumber));
            // Create button with that color
            final GinjColorToggleButton colorToggleButton = new GinjColorToggleButton(buttonColor);
            // Select the button if it is the currently selected color
            if (currentColor != null && currentColor.equals(buttonColor)) {
                colorToggleButton.setSelected(true);
            }
            // On click select that color
            colorToggleButton.addActionListener(e -> {
                frame.setCurrentColor(colorToggleButton.getColor());
                dispose();
            });
            // And add the button to the popup window
            buttonPanel.add(colorToggleButton);
        }
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets.set(4, 4, 2, 4);
        colorPanel.add(buttonPanel, c);

        JButton customColorButton = new JButton("Custom...");
        customColorButton.addActionListener(e -> {
            // TODO Open colorChooser
            // TODO Upon selection, store color in list of recently used colors (unless it's already the last one)
        });
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.insets.set(2, 4, 3, 4);
        colorPanel.add(customColorButton, c);

        // Close when losing focus
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                ColorSelectorWindow.this.dispose();
            }
        });

        pack();
    }
}
