package com.github.mrmks.efkseer4j;

import Effekseer.swig.EffekseerManagerCore;

import java.util.Objects;

@SuppressWarnings("unused")
/**
 * EfsProgram should be deleted at the end of the thread and before destroy the OpenGL context.
 * Otherwise, software may exit with a non-zero code.
 */
public final class EfsProgram {

    final EffekseerManagerCore core = new EffekseerManagerCore() {

        @Override
        public boolean equals(Object obj) {
            return getClass().isInstance(obj)
                    && getCPtr(this) == getCPtr((EffekseerManagerCore) obj);
        }

        @Override
        public int hashCode() {
            return Long.hashCode(getCPtr(this));
        }
    };

    public EfsProgram() {}

    public boolean initialize(int maxSprites, boolean srgb) {
        return core.Initialize(maxSprites, srgb);
    }

    public boolean initialize(int maxSprites) {
        return core.Initialize(maxSprites);
    }

    public void update(float delta) {
        core.Update(delta);
    }

    public void startUpdate() {
        core.BeginUpdate();
    }

    public void endUpdate() {
        core.EndUpdate();
    }

    public void delete() {
        core.delete();
    }

    public EfsEffectHandle playEffect(EfsEffect effect) {
        int handle = core.Play(effect.core);
        return new EfsEffectHandle(handle, this);
    }

    public void stopEffects() {
        core.StopAllEffects();
    }

    public void draw() {
        drawBack();
        drawFront();
    }

    public void draw(int layer) {
        drawBack(layer);
        drawFront(layer);
    }

    public void drawBack() {
        core.DrawBack();
    }

    public void drawBack(int layer) {
        core.DrawBack(layer);
    }

    public void drawFront() {
        core.DrawFront();
    }

    public void drawFront(int layer) {
        core.DrawFront(layer);
    }

    public void setViewport(int width, int height) {
        core.SetViewProjectionMatrixWithSimpleWindow(width, height);
    }

    public void setupWorkerThreads(int count) {
        core.LaunchWorkerThreads(count);
    }

    public void setLayerParameter(int layer, float posX, float posY, float posZ, float distanceBias) {
        core.SetLayerParameter(layer, posX, posY, posZ, distanceBias);
    }

    public void setCameraParameter(float frontX, float frontY, float frontZ, float posX, float posY, float posZ) {
        core.SetCameraParameter(frontX, frontY, frontZ, posX, posY, posZ);
    }

    public void setCameraMatrix(float[] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        core.SetCameraMatrix(
                matrix[0], matrix[1], matrix[2], matrix[3],
                matrix[4], matrix[5], matrix[6], matrix[7],
                matrix[8], matrix[9], matrix[10], matrix[11],
                matrix[12], matrix[13], matrix[14], matrix[15]
        );
    }

    public void setCameraMatrix(float[][] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        byte indx = 0;
        core.SetCameraMatrix(
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx][3]
        );
    }

    public void setProjectionMatrix(float[] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        core.SetProjectionMatrix(
                matrix[0], matrix[1], matrix[2], matrix[3],
                matrix[4], matrix[5], matrix[6], matrix[7],
                matrix[8], matrix[9], matrix[10], matrix[11],
                matrix[12], matrix[13], matrix[14], matrix[15]
        );
    }

    public void setProjectionMatrix(float[][] matrix) {
        matrix = EffekSeer4J.matrixTranspose(matrix);
        byte indx = 0;
        core.SetProjectionMatrix(
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx++][3],
                matrix[indx][0], matrix[indx][1], matrix[indx][2], matrix[indx][3]
        );
    }

    public void setDepth(long texture, boolean hasMipmap) {
        core.SetDepth(texture, hasMipmap);
    }

    public void unsetDepth() {
        core.UnsetDepth();
    }

    public void setBackground(long texture, boolean hasMipmap) {
        core.SetBackground(texture, hasMipmap);
    }

    public void unsetBackground() {
        core.UnsetBackground();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof EfsProgram
                && core.equals(((EfsProgram) object).core);
    }

    @Override
    public int hashCode() {
        return Objects.hash(core);
    }
}
