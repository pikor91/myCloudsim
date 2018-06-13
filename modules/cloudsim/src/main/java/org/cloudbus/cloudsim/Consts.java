package org.cloudbus.cloudsim;

/**
 * 
 * Defines common constants, used throughout cloudsim.
 * 
 * @author nikolay.grozev
 * 
 */
public final class Consts {

    /** Suppreses intantiation. */
    private Consts() {
    }

    /** One million. */
    public static final int MILLION = 1000000;

    // ================== Time constants ==================
    /** One minute time in seconds. */
    public static final int MINUTE = 60;
    /** One hour time in seconds. */
    public static final int HOUR = 60 * MINUTE;
    /** One day time in seconds. */
    public static final int DAY = 24 * HOUR;
    /** One week time in seconds. */
    public static final int WEEK = 24 * HOUR;

    // ================== OS constants ==================
    /** Constant for *nix Operating Systems. */
    public static final String NIX_OS = "Linux/Unix";
    /** Constant for Windows Operating Systems. */
    public static final String WINDOWS = "Windows";

    // =============== Operation constants ===============
    public static final String OPERATION = "operation";
    public static final String START_STATE = "startState";
    public static final String END_STATE = "endState";
    public static final String HOST = "host";
    public static final String VM = "vm";

    // ================ enable host switching off =============
    public static final boolean ENABLE_HS = false;
}
