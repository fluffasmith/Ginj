package info.ginj.action;

import info.ginj.tool.Overlay;

import java.awt.*;

public class ModifyOverlayAction extends AbstractUndoableAction {
    private final Overlay overlay;
    private final int handleIndex;
    private final Point initialPosition;
    private Point finalPosition;

    public ModifyOverlayAction(Overlay overlay, int handleIndex, Point initialPosition) {
        this.overlay = overlay;
        this.handleIndex = handleIndex;
        this.initialPosition = initialPosition;
    }

    public String getPresentationName() {
        return "modify " + overlay.getPresentationName().toLowerCase();
    }

    public void setTargetPoint(Point finalPosition) {
        this.finalPosition = finalPosition;
    }


    public void execute() {
        overlay.moveHandle(handleIndex, finalPosition);
    }


    public void undo() {
        super.undo();
        overlay.moveHandle(handleIndex, initialPosition);
    }

    public void redo() {
        super.redo();
        execute();
    }

}
