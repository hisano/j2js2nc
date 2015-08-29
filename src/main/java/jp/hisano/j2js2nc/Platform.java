/*
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 */
package jp.hisano.j2js2nc;

/** Provide simplified platform information. */
public final class Platform {
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final int AIX = 7;
    public static final int ANDROID = 8;
    public static final int GNU = 9;
    public static final int KFREEBSD = 10;
    public static final int NETBSD = 11;
    public static final int JS = 12;

    /** Whether read-only (final) fields within Structures are supported. */
    public static final boolean RO_FIELDS;
    /** Whether this platform provides NIO Buffers. */
    public static final boolean HAS_BUFFERS;
    /** Whether this platform provides the AWT Component class; also false if
     * running headless.
     */
    public static final boolean HAS_AWT;
    /** Canonical name of this platform's math library. */
    public static final String MATH_LIBRARY_NAME;
    /** Canonical name of this platform's C runtime library. */
    public static final String C_LIBRARY_NAME;
    /** Whether in-DLL callbacks are supported. */
    public static final boolean HAS_DLL_CALLBACKS;
    /** Canonical resource prefix for the current platform.  This value is
     * used to load bundled native libraries from the class path.
     */
    public static final String RESOURCE_PREFIX;

    private static final int osType;
    /** Current platform architecture. */
    public static final String ARCH;

    static {
        osType = JS;
        boolean hasBuffers = false;
        try {
            Class.forName("java.nio.Buffer");
            hasBuffers = true;
        }
        catch(ClassNotFoundException e) {
        }
        // NOTE: we used to do Class.forName("java.awt.Component"), but that
        // has the unintended side effect of actually loading AWT native libs,
        // which can be problematic
        HAS_AWT = osType != WINDOWSCE && osType != ANDROID && osType != AIX;
        HAS_BUFFERS = hasBuffers;
        RO_FIELDS = osType != WINDOWSCE;
        C_LIBRARY_NAME = osType == WINDOWS ? "msvcrt" : osType == WINDOWSCE ? "coredll" : "c";
        MATH_LIBRARY_NAME = osType == WINDOWS ? "msvcrt" : osType == WINDOWSCE ? "coredll" : "m";
        HAS_DLL_CALLBACKS = osType == WINDOWS;
        RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
        ARCH = "js";
    }
    private Platform() { }
    public static final int getOSType() {
        return osType;
    }
    public static final boolean isJS() {
    	return osType == JS;
    }
    public static final boolean isMac() {
        return osType == MAC;
    }
    public static final boolean isAndroid() {
        return osType == ANDROID;
    }
    public static final boolean isLinux() {
        return osType == LINUX;
    }
    public static final boolean isAIX() {
        return osType == AIX;
    }
    /** @deprecated */
    public static final boolean isAix() {
        return isAIX();
    }
    public static final boolean isWindowsCE() {
        return osType == WINDOWSCE;
    }
    /** Returns true for any windows variant. */
    public static final boolean isWindows() {
        return osType == WINDOWS || osType == WINDOWSCE;
    }
    public static final boolean isSolaris() {
        return osType == SOLARIS;
    }
    public static final boolean isFreeBSD() {
        return osType == FREEBSD;
    }
    public static final boolean isOpenBSD() {
        return osType == OPENBSD;
    }
    public static final boolean isNetBSD() {
        return osType == NETBSD;
    }
    public static final boolean isGNU() {
        return osType == GNU;
    }
    public static final boolean iskFreeBSD() {
        return osType == KFREEBSD;
    }
    public static final boolean isX11() {
        // TODO: check filesystem for /usr/X11 or some other X11-specific test
        return !Platform.isWindows() && !Platform.isMac();
    }
    public static final boolean hasRuntimeExec() {
        if (isWindowsCE() && "J9".equals(System.getProperty("java.vm.name")))
            return false;
        return true;
    }
    public static final boolean is64Bit() {
        return Native.POINTER_SIZE == 8;
    }

    public static final boolean isIntel() {
        if (ARCH.equals("i386")
            || ARCH.startsWith("i686")
            || ARCH.equals("x86")
            || ARCH.equals("x86_64")
            || ARCH.equals("amd64")) {
            return true;
        } 
        return false;
    }

    public static final boolean isPPC() {
        if (ARCH.equals("ppc")
            || ARCH.equals("ppc64")
            || ARCH.equals("powerpc")
            || ARCH.equals("powerpc64")) {
            return true;
        } 
        return false;
    }

    public static final boolean isARM() {
        return ARCH.startsWith("arm");
    }

    public static final boolean isSPARC() {
        return ARCH.startsWith("sparc");
    }

    /** Generate a canonical String prefix based on the current OS 
        type/arch/name.
    */
    static String getNativeLibraryResourcePrefix() {
        return getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
    }

    /** Generate a canonical String prefix based on the given OS
        type/arch/name.
        @param osType from {@link #getOSType()}
        @param arch from <code>os.arch</code> System property
        @param name from <code>os.name</code> System property
    */
    static String getNativeLibraryResourcePrefix(int osType, String arch, String name) {
        return "js";
    }
}
