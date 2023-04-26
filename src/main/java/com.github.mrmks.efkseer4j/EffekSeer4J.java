package com.github.mrmks.efkseer4j;

import Effekseer.swig.EffekseerBackendCore;
import Effekseer.swig.EffekseerCoreDeviceType;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;

public class EffekSeer4J {

    private static class Library {

        private static boolean loaded = false, success = false;
        static synchronized boolean load() {
            if (!loaded) {

                loaded = true;

                String overwrite = System.getProperty("effekseer.path", null);
                if (overwrite != null) {
                    System.load(overwrite);
                    return success = true;
                }

                class RuntimeExceptionNonStack extends RuntimeException {
                    RuntimeExceptionNonStack(Throwable tr) { super(tr); }
                    RuntimeExceptionNonStack(String msg) { super(msg); }
                    @Override
                    public synchronized Throwable fillInStackTrace() { return this; }
                }

                boolean win = System.getProperty("os.name").toLowerCase(Locale.US).contains("win");
                String lib = win ? "EffekseerNativeForJava.dll" : "libEffekseerNativeForJava.so";
                File tmpFile;
                try {
                    tmpFile = File.createTempFile("efkseer4j-native", win ? ".dll" : ".so");
                    tmpFile.deleteOnExit();
                } catch (IOException e) {
                    throw new RuntimeExceptionNonStack(e);
                }

                String rs = "native/" + (win ? "windows" : "linux") + "/x86_64/" + lib;
                InputStream stream = Library.class.getClassLoader().getResourceAsStream(rs);
                if (stream == null) {
                    throw new RuntimeExceptionNonStack("Unable to load resource stream from jar file, which should never happen;");
                } else {
                    try (Closeable _stream = stream) {
                        Files.copy(stream, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        throw new RuntimeExceptionNonStack(ex);
                    }
                }

                try {
                    System.load(tmpFile.getAbsolutePath());
                    success = true;
                } catch (Throwable tr) {
                    throw tr instanceof RuntimeException ? (RuntimeException) tr : new RuntimeExceptionNonStack(tr);
                }
            }

            return loaded && success;
        }
    }

    public enum Device {
        UNKNOWN(EffekseerCoreDeviceType.Unknown),
        OPENGL(EffekseerCoreDeviceType.OpenGL),
        DIRECTX9(EffekseerCoreDeviceType.DirectX9),
        DIRECTX11(EffekseerCoreDeviceType.DirectX11)
        ;

        private final int swigValue;

        Device(EffekseerCoreDeviceType swigType) {
            this.swigValue = swigType.swigValue();
        }

        static Device fromSwig(int swigValue) {
            for (Device types : values())
                if (types.swigValue == swigValue) return types;

            return null;
        }
    }

    // ======== inner classes / static methods ========
    private static void checkLoaded() {
        if (!Library.load())
            throw new IllegalStateException("Binary library for effekseer has not been loaded yet.");
    }

    private static Device current = null;

    /**
     * Initialize effekseer with given device.
     * @return true if we successfully initialized with given device, false otherwise.
     * @throws IllegalStateException when trying to initialize a initialized effekseer engine with different device;
     * @throws NullPointerException if given device is null;
     */
    public static synchronized boolean setup(Device device) {

        checkLoaded();

        Objects.requireNonNull(device);
        Device cur = getDevice();
        if (cur == device) {
            return true;
        } else {
            if (cur == Device.UNKNOWN) {
                boolean flag;
                if (device == Device.OPENGL) {
                    flag = EffekseerBackendCore.InitializeWithOpenGL();
                } else {
                    flag = false;
                }

                if (flag) current = device;

                return flag;
            } else {
                throw new IllegalStateException("Trying to initialize effekseer again with different device: cur: (" + cur + ") -> new: (" + device + ")");
            }
        }
    }

    /**
     * terminate effekseer engine.
     */
    public static synchronized void finish() {

        checkLoaded();

        EffekseerBackendCore.Terminate();
    }

    /**
     * @return The device effekseer is running on.
     */
    public static synchronized Device getDevice() {

        checkLoaded();

        return Device.fromSwig(EffekseerBackendCore.GetDevice().swigValue());
    }

    static float[] matrixTranspose(float[] matrix) {
        if (current == Device.OPENGL) {
            float[] copy = new float[16];

            copy[0] = matrix[0]; copy[1] = matrix[4]; copy[2] = matrix[8]; copy[3] = matrix[12];
            copy[4] = matrix[1]; copy[5] = matrix[5]; copy[6] = matrix[9]; copy[7] = matrix[13];
            copy[8] = matrix[2]; copy[9] = matrix[6]; copy[10] = matrix[10]; copy[11] = matrix[14];
            copy[12] = matrix[3]; copy[13] = matrix[7]; copy[14] = matrix[11]; copy[15] = matrix[15];

            return copy;
        }

        return matrix;
    }

    static float[][] matrixTranspose(float[][] matrix) {
        if (current == Device.OPENGL) {
            float[][] copy = new float[4][];

            copy[0] = new float[]{ matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0] };
            copy[1] = new float[]{ matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1] };
            copy[2] = new float[]{ matrix[0][2], matrix[1][2], matrix[2][2], matrix[3][2] };
            copy[3] = new float[]{ matrix[0][3], matrix[1][3], matrix[2][3], matrix[3][3] };

            return copy;
        }

        return matrix;
    }
}
