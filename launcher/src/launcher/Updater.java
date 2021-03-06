package launcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Updater {
    private static final String spsScriptDir = "http://www.simplepathstudios.com/script/aigilas/";
    //Working update paths
    private File update = new File("aigilas-update.zip");
    private File updateDir = new File("aigilas-update");

    //License path
    private File licenseCache = new File("assets/data/license.dat");

    public Updater() {

    }

    public boolean runIfNeeded(String license) {
        boolean success = false;
        if (checkLicense(license) && checkVersion() && downloadUpdate(license) && applyUpdate()) {
            success = true;
        }
        clean();
        return success;
    }


    private void cacheLicense(String license) {
        if (!licenseIsCached()) {
            try {
                FileUtils.writeStringToFile(licenseCache, license);
            }
            catch (IOException e) {
                LaunchLogger.info("There was a problem caching your license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
    }

    public String getCachedLicense() {
        if (licenseCache.exists()) {
            try {
                return FileUtils.readFileToString(licenseCache);
            }
            catch (IOException e) {
                LaunchLogger.info("There was a problem reading the cached license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
        return null;
    }

    public boolean licenseIsCached() {
        return licenseCache.exists();
    }

    private boolean checkLicense(String license) {
        try {
            LaunchLogger.info("Checking to see if a stable edition license has been entered.");
            if (license != null && !license.isEmpty()) {
                URL licenseCheckUrl = new URL(spsScriptDir+"download.php?target=aigilas-validate&license=" + license);
                String response = IOUtils.toString(licenseCheckUrl.openStream());
                if (response.contains("true")) {
                    LaunchLogger.info(LaunchLogger.Tab + "License is valid.");
                    cacheLicense(license);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "No valid license found.");
        return false;
    }

    private boolean checkVersion() {
        try {
            LaunchLogger.info("Checking for updates.");

            File versionPath = new File("assets/data/version.dat");
            if (versionPath.exists()) {
                String myVersion = FileUtils.readFileToString(versionPath);
                LaunchLogger.info("Detected version: " + myVersion);

                URL versionCheckUrl = new URL(spsScriptDir+"download.php?target=aigilas-version&version=" + myVersion);

                String result = IOUtils.toString(versionCheckUrl.openStream());
                if (result.contains("true")) {
                    LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is out of date.");
                    return true;
                }
            }
            else {
                LaunchLogger.info(LaunchLogger.Tab + "No version information was found");
                LaunchLogger.info(LaunchLogger.Tab + versionPath.getAbsolutePath());
                return false;
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is up to date. No update required.");
        return false;
    }

    private boolean downloadUpdate(String license) {
        try {
            LaunchLogger.info("Preparing the update location");

            if (update.exists()) {
                FileUtils.forceDelete(update);
            }

            int responseTimeoutMs = 10000;
            int downloadTimeoutMs = 60000;
            LaunchLogger.info("Attempting to download an update using license: [" + license + "]");
            String spsLicenseUrl = spsScriptDir+"download.php?target=aigilas&license=" + license;
            LaunchLogger.info("Downloading latest stable edition");
            FileUtils.copyURLToFile(new URL(spsLicenseUrl), update, responseTimeoutMs, downloadTimeoutMs);
        }
        catch (Exception e) {
            LaunchLogger.info(LaunchLogger.Tab + "There was a problem downloading the update.");
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Update downloaded successfully.");
        return true;
    }

    private boolean applyUpdate() {
        try {
            Archive.unzip(update);
            LaunchLogger.info("Replacing old content");
            File updateAssets = new File("aigilas-update/assets");
            File baseAssets = new File("./");
            FileUtils.copyDirectoryToDirectory(updateAssets, baseAssets);

            File updateCore = new File("aigilas-update/aigilas.jar");
            File baseCore = new File("aigilas.jar");
            FileUtils.copyFile(updateCore, baseCore);
        }
        catch (Exception e) {
            LaunchLogger.info(LaunchLogger.Tab + "There was a problem applying the update.");
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Update applied successfully.");
        return true;
    }

    private boolean clean() {
        try {
            LaunchLogger.info("Cleaning up temporary files");
            if (update.exists()) {
                FileUtils.forceDelete(update);
            }
            if (updateDir.exists()) {
                FileUtils.deleteDirectory(updateDir);
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        return true;
    }
}
