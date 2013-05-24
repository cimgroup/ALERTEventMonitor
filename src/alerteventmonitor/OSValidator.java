/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alerteventmonitor;

/**
 *
 * @author sasa.stojanovic
 */
public class OSValidator {
    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * 
     * @return 
     * @startRealisation  Dejan Milosavljevic  11.01.2013.
     * @finalModification Dejan Milosavljevic  11.01.2013.
     */
    public static boolean isWindows() {
	return (OS.indexOf("win") >= 0);
    }

    /**
     * 
     * @return 
     * @startRealisation  Dejan Milosavljevic  11.01.2013.
     * @finalModification Dejan Milosavljevic  11.01.2013.
     */
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    /**
     * 
     * @return 
     * @startRealisation  Dejan Milosavljevic  11.01.2013.
     * @finalModification Dejan Milosavljevic  11.01.2013.
     */
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    /**
     * 
     * @return 
     * @startRealisation  Dejan Milosavljevic  11.01.2013.
     * @finalModification Dejan Milosavljevic  11.01.2013.
     */
    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
    
    /**
     * 
     * @return 
     * @startRealisation  Dejan Milosavljevic  08.04.2013.
     * @finalModification Dejan Milosavljevic  08.04.2013.
     */
    public static boolean is64BitVM() {
        String bits = System.getProperty("sun.arch.data.model", "?");
        if (bits.equals("64")) {
            return true;
        }
        if (bits.equals("?")) {
            // probably sun.arch.data.model isn't available
            // maybe not a Sun JVM?
            // try with the vm.name property
            return 
                System.getProperty("java.vm.name")
                .toLowerCase().indexOf("64") >= 0;
            } 
        // probably 32bit
        return false;
    }

    /**
     * 
     * @startRealisation  Dejan Milosavljevic  23.04.2013.
     * @finalModification Dejan Milosavljevic  24.04.2013.
     * @return 
     */
    public static String GetAppPath() {
        String sPath = "";
        
        try {
            if (OSValidator.isWindows()) {
                sPath = System.getProperty("user.dir");
            }
            else if (OSValidator.isUnix()) {
                sPath = EventMonitor.class.getResource(EventMonitor.class.getSimpleName() + ".class").toURI().toString();
                if (sPath != null && !sPath.isEmpty()) {
                    String [] sTemp = sPath.replace("\\", "/").split("!");
                    if (sTemp.length > 0) sPath = sTemp[0];
                    sTemp = sPath.split(":");
                    if (sTemp.length > 0) sPath = sTemp[sTemp.length - 1];
                    sTemp = sPath.split("/");
                    sPath = Join(sTemp, "/", true);
                }
                else {
                    sPath = "";
                }
            }
        }
        catch(Exception e) {
            sPath = e.getMessage();
        }
        
        return sPath;
    }
    
    /**
     * 
     * @startRealisation  Dejan Milosavljevic  24.04.2013.
     * @finalModification Dejan Milosavljevic  24.04.2013.
     * @param oList
     * @param sSeparator
     * @return 
     */
    private static String Join(String[] oList, String sSeparator, boolean bExcludeLast) {
        StringBuilder sbNew = new StringBuilder();
        if (oList != null && oList.length > 0){
            sbNew.append(oList[0]);
            int iCount = oList.length;
            if (bExcludeLast) iCount = oList.length - 1;
            for (int i = 1; i < iCount; i++) {
                sbNew.append(sSeparator).append(oList[i]);
            }
        }
        return sbNew.toString();
    }
}
