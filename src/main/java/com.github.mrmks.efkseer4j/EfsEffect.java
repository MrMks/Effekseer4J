package com.github.mrmks.efkseer4j;

import Effekseer.swig.EffekseerEffectCore;
import Effekseer.swig.EffekseerTextureType;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public final class EfsEffect {

    public enum Texture {
        COLOR(EffekseerTextureType.Color),
        NORMAL(EffekseerTextureType.Normal),
        DISTORTION(EffekseerTextureType.Distortion),
        ;

        private final EffekseerTextureType swigType;
        Texture(EffekseerTextureType swigType) {
            this.swigType = swigType;
        }

        EffekseerTextureType unwrap() {
            return swigType;
        }

        static Texture fromSwig(int swigValue) {
            for (Texture type : values())
                if (type.swigType.swigValue() == swigValue)
                    return type;

            return null;
        }
    }

    final EffekseerEffectCore core;

    private boolean isLoaded = false;

    public EfsEffect() {
        core = new EffekseerEffectCore();
    }

    public void delete() {
        core.delete();
    }

    public static byte[] readInputStream(InputStream stream, int length, boolean close) throws IOException {
        if (stream == null) return new byte[0];

        if (length == 0) {
            if (close) try (InputStream in = stream) {;}
            return new byte[0];
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] worker = new byte[8192];
            int len, max = length < 0 ? Integer.MAX_VALUE : length;

            try (Closeable closeable = close ? stream : () -> {}) {
                while ((len = stream.read(worker)) >= 0 && max > 0) {
                    out.write(worker, 0, Math.min(max, len));
                    max -= len;
                }
            }

            return out.toByteArray();
        }
    }

    // ======== effect info ========
    public boolean load(InputStream stream, int length, float amplifier, boolean closeStream) {
        try {
            byte[] bytes = readInputStream(stream, length, closeStream);
            return load(bytes, bytes.length, amplifier);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean load(InputStream stream, float amplifier, boolean closeStream) {
        return load(stream, -1, amplifier, closeStream);
    }

    public boolean load(byte[] data, float amplifier) {
        return load(data, data.length, amplifier);
    }

    public boolean load(byte[] data, int length, float amplifier) {
        return isLoaded = core.Load(data, length, amplifier);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    // ======== textures ========

    public boolean loadTexture(InputStream stream, int length, int index, Texture type, boolean closeStream) {
        try {
            byte[] bytes = readInputStream(stream, length, closeStream);
            return loadTexture(bytes, bytes.length, index, type);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadTexture(InputStream stream, int index, Texture type, boolean closeStream) {
        return loadTexture(stream, -1, index, type, closeStream);
    }

    public boolean loadTexture(byte[] data, int index, Texture type) {
        return loadTexture(data, data.length, index, type);
    }

    public boolean loadTexture(byte[] data, int length, int index, Texture type) {
        return core.LoadTexture(data, length, index, type.unwrap());
    }

    public boolean isTextureLoaded(int index, Texture type) {
        return core.HasTextureLoaded(index, type.unwrap());
    }

    public int textureCount(Texture type) {
        return core.GetTextureCount(type.unwrap());
    }

    public int textureCount() {
        int sum = 0;
        for (Texture value : Texture.values()) sum += core.GetTextureCount(value.unwrap());
        return sum;
    }

    public String getTexturePath(int index, Texture type) {
        return core.GetTexturePath(index, type.unwrap());
    }

    // ======== curves ========

    public boolean loadCurve(InputStream stream, int length, int index, boolean closeStream) {
        try {
            byte[] bytes = readInputStream(stream, length, closeStream);
            return loadCurve(bytes, bytes.length, index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadCurve(InputStream stream, int index, boolean closeStream) {
        return loadCurve(stream, -1, index, closeStream);
    }

    public boolean loadCurve(byte[] data, int index) {
        return loadCurve(data, data.length, index);
    }

    public boolean loadCurve(byte[] data, int length, int index) {
        return core.LoadCurve(data, length, index);
    }

    public boolean isCurveLoaded(int index) {
        return core.HasCurveLoaded(index);
    }

    public int curveCount() {
        return core.GetCurveCount();
    }

    public String getCurvePath(int index) {
        return core.GetCurvePath(index);
    }

    // ======== material ========

    public boolean loadMaterial(InputStream stream, int length, int index, boolean closeStream) {
        try {
            byte[] bytes = readInputStream(stream, length, closeStream);
            return loadMaterial(bytes, bytes.length, index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadMaterial(InputStream stream, int index, boolean closeStream) {
        return loadMaterial(stream, -1, index, closeStream);
    }

    public boolean loadMaterial(byte[] data, int index) {
        return loadMaterial(data, data.length, index);
    }

    public boolean loadMaterial(byte[] data, int length, int index) {
        return core.LoadMaterial(data, length, index);
    }

    public boolean isMaterialLoaded(int index) {
        return core.HasMaterialLoaded(index);
    }

    public int materialCount() {
        return core.GetMaterialCount();
    }

    public String getMaterialPath(int index) {
        return core.GetMaterialPath(index);
    }

    // ======== model ========

    public boolean loadModel(InputStream stream, int length, int index, boolean closeStream) {
        try {
            byte[] bytes = readInputStream(stream, length, closeStream);
            return loadModel(bytes, bytes.length, index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadModel(InputStream stream, int index, boolean closeStream) {
        return loadModel(stream, -1, index, closeStream);
    }

    public boolean loadModel(byte[] data, int index) {
        return loadModel(data, data.length, index);
    }

    public boolean loadModel(byte[] data, int length, int index) {
        return core.LoadModel(data, length, index);
    }

    public boolean isModelLoaded(int index) {
        return core.HasModelLoaded(index);
    }

    public int modelCount() {
        return core.GetModelCount();
    }

    public String getModelPath(int index) {
        return core.GetModelPath(index);
    }

    // ======== terms ========

    public int minTerm() {
        return core.GetTermMin();
    }

    public int maxTerm() {
        return core.GetTermMax();
    }
}
