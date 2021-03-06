package info.ginj.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class DisplayConfiguration {
    List<Display> displayList = new ArrayList<>();
    private boolean nativeInfoAvailable;

    public void addDisplay(Rectangle logicalRectangle, Insets insets, AffineTransform defaultTransform) {
        displayList.add(new Display(logicalRectangle, insets, defaultTransform));
    }

    public List<Display> getDisplayList() {
        return displayList;
    }

    public void setNativeInfoAvailable(boolean nativeInfoAvailable) {
        this.nativeInfoAvailable = nativeInfoAvailable;
    }

    public boolean isNativeInfoAvailable() {
        return nativeInfoAvailable;
    }

    public static class Display {
        private final Rectangle logicalRectangle;
        private final Insets insets;
        private final AffineTransform defaultTransform;
        private Rectangle physicalRectangle;

        public Display(Rectangle logicalRectangle, Insets insets, AffineTransform defaultTransform) {
            this.logicalRectangle = logicalRectangle;
            this.insets = insets;
            this.physicalRectangle = logicalRectangle; // By default
            this.defaultTransform = defaultTransform;
        }

        public Rectangle getLogicalRectangle() {
            return logicalRectangle;
        }

        public Insets getInsets() {
            return insets;
        }

        public Rectangle getPhysicalRectangle() {
            return physicalRectangle;
        }

        public void setPhysicalRectangle(Rectangle physicalRectangle) {
            this.physicalRectangle = physicalRectangle;
        }

        public double getXScalingFactor() {
            return ((double)physicalRectangle.width)/logicalRectangle.width;
        }

        public double getYScalingFactor() {
            return ((double)physicalRectangle.height)/logicalRectangle.height;
        }

        public AffineTransform getDefaultTransform() {
            return defaultTransform;
        }
    }
}
