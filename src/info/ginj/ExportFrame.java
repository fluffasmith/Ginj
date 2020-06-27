package info.ginj;

import info.ginj.export.ExportMonitor;
import info.ginj.export.GinjExporter;
import info.ginj.ui.GinjLabel;
import info.ginj.ui.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExportFrame extends JFrame implements ExportMonitor {

    private final JLabel stateLabel;
    private final JLabel sizeLabel;
    private final BoundedRangeModel progressModel;
    private final Window parentWindow;
    private final Capture capture;
    private GinjExporter exporter;

    public ExportFrame(Window parentWindow, Capture capture, GinjExporter exporter) {
        super();
        this.parentWindow = parentWindow;
        this.capture = capture;
        this.exporter = exporter;

        // No window title bar or border.
        // Note: setDefaultLookAndFeelDecorated(true); must not have been called anywhere for this to work
        setUndecorated(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Add state label
        stateLabel = new GinjLabel("Exporting...");

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 16, 0, 16);
        mainPanel.add(stateLabel, c);


        // Add progress bar
        progressModel = new DefaultBoundedRangeModel();
        JProgressBar progressBar = new JProgressBar(progressModel);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(4, 16, 4, 16);
        mainPanel.add(progressBar, c);

        if (exporter != null) {
            // Add cancel button
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> onCancel());

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.weightx = 0;
            c.insets = new Insets(0, 0, 0, 16);
            mainPanel.add(cancelButton, c);
        }


        // Add size label
        sizeLabel = new GinjLabel(" ");

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 16, 4, 16);
        mainPanel.add(sizeLabel, c);


        // Add default "draggable window" behaviour
        Util.addDraggableWindowMouseBehaviour(this, mainPanel);

        getContentPane().add(mainPanel);

        pack();
        setSize(280,70);

        // Center window
        // TODO should pop up next to the star icon
        setLocationRelativeTo(null);
    }


    @Override
    public void log(String state, int progress, long currentSizeBytes, long totalSizeBytes) {
        stateLabel.setText(state);
        progressModel.setValue(progress);
        sizeLabel.setText((currentSizeBytes / 1024) + " / " + (totalSizeBytes / 1024) + " kB");
    }

    @Override
    public void log(String state, int progress, String sizeProgress) {
        stateLabel.setText(state);
        progressModel.setValue(progress);
        sizeLabel.setText(sizeProgress);
    }

    @Override
    public void log(String state, int progress) {
        stateLabel.setText(state);
        progressModel.setValue(progress);
    }

    @Override
    public void log(String state) {
        stateLabel.setText(state);
    }

    private void onCancel() {
        if (exporter != null) {
            exporter.cancel();
        }
        // "Reopen" the capture window
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        }

        closeExportWindow();
    }

    @Override
    public void complete(String state) {
        // Free up capture Window
        if (parentWindow != null) {
            parentWindow.dispose();
        }

        // Store image in history, no matter the export type
        saveInHistory(capture);

        closeExportWindow();

        // TODO Should be the notification window with auto close
        JOptionPane.showMessageDialog(this, state, "Export complete", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void failed(String state) {
        // "Reopen" the capture window
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        }

        closeExportWindow();
    }

    private void closeExportWindow() {
        // Close this window
        exporter = null;
        dispose();
    }


    /**
     * Prepares and starts the export
     * @param accountNumber the accountNumber to export this capture to (if relevant)
     * @return true if export was started, false otherwise
     */
    public boolean startExport(String accountNumber) {
        if (exporter.prepare(capture, accountNumber)) {
            Thread exportThread = new Thread(() -> exporter.exportCapture(capture, accountNumber));
            exportThread.start();
            return true;
        }
        else {
            closeExportWindow();
            return false;
        }
    }


    private void saveInHistory(Capture capture) {
        File historyFolder = new File("ZZhistoryFolder"); // TODO get from params
        if (!historyFolder.exists()) {
            if (!historyFolder.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Could not create history folder (" + historyFolder.getAbsolutePath() + ")", "Save error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Save image
        File targetFile = new File(historyFolder, capture.getId() + ".png");
        try {
            if (capture.isVideo) {
                // Move file to history
                // TODO should move the source, not the rendered version !
                Files.move(capture.getFile().toPath(), targetFile.toPath());
            }
            else {
                // Write the image to disk
                // TODO should save the source, not the rendered version !
                if (!ImageIO.write(capture.getImage(), "png", targetFile)) {
                    JOptionPane.showMessageDialog(this, "Saving capture to history failed (" + targetFile.getAbsolutePath() + ")", "Save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (IOException e) {
            Util.alertError(this, "Save error", "Error saving file to history");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error '" + e.getMessage() + "'  - Full error on the console", "Save error", JOptionPane.ERROR_MESSAGE);
        }

        // TODO save overlays to XML

        // TODO save thumbnail with overlays

    }

}