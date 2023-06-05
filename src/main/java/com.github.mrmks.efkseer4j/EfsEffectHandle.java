package com.github.mrmks.efkseer4j;

import java.util.Objects;

@SuppressWarnings("unused")
public final class EfsEffectHandle {
    public final int handle;
    private final EfsProgram manager;
    public boolean isVisible = true;
    public boolean isPaused = false;

    EfsEffectHandle(int handle, EfsProgram manager) {
        this.handle = handle;
        this.manager = manager;
        setVisibility(true);
        resume();
    }

    public void pause() {
        manager.core.SetPaused(this.handle, true);
        isPaused = true;
    }

    public void resume() {
        manager.core.SetPaused(this.handle, false);
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setLayer(int layer) {
        manager.core.SetLayer(this.handle, layer);
    }

    public void sendTrigger(int trigger) {
        manager.core.SendTrigger(this.handle, trigger);
    }

    public void setVisibility(boolean visible) {
        manager.core.SetShown(this.handle, visible);
        isVisible = visible;
    }

    public void stop() {
        manager.core.Stop(this.handle);
    }

    public void setProgress(float frame) {
        manager.core.UpdateHandleToMoveToFrame(this.handle, frame);
    }

    public void moveTo(float x, float y, float z) {
        manager.core.SetEffectPosition(this.handle, x, y, z);
    }

    public void rotateTo(float x, float y, float z) {
        manager.core.SetEffectRotation(this.handle, x, y, z);
    }

    public void scaleTo(float x, float y, float z) {
        manager.core.SetEffectScale(this.handle, x, y, z);
    }

    public void setTransformMatrix(float[] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        manager.core.SetEffectTransformMatrix(
                this.handle,
                matrix[0], matrix[1], matrix[2], matrix[3],
                matrix[4], matrix[5], matrix[6], matrix[7],
                matrix[8], matrix[9], matrix[10], matrix[11]
        );
    }

    public void setTransformMatrix(float[][] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        byte indx = 0;
        manager.core.SetEffectTransformMatrix(
                this.handle,
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx][3]
        );
    }

    public void setBaseTransformMatrix(float[] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        manager.core.SetEffectTransformBaseMatrix(
                this.handle,
                matrix[0], matrix[1], matrix[2], matrix[3],
                matrix[4], matrix[5], matrix[6], matrix[7],
                matrix[8], matrix[9], matrix[10], matrix[11]
        );
    }

    public void setBaseTransformMatrix(float[][] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        byte indx = 0;
        manager.core.SetEffectTransformBaseMatrix(
                this.handle,
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx][3]
        );
    }

    public boolean exists() {
        return manager.core.Exists(this.handle);
    }

    public void setDynamicInput(int index, float value) {
        manager.core.SetDynamicInput(this.handle, index, value);
    }

    public float getDynamicInput(int index) {
        return manager.core.GetDynamicInput(this.handle, index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!getClass().isInstance(object)) return false;
        EfsEffectHandle that = (EfsEffectHandle) object;
        return handle == that.handle && Objects.equals(manager, that.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle, manager);
    }
}
