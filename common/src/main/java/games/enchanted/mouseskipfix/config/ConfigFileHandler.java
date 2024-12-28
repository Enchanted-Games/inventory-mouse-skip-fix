package games.enchanted.mouseskipfix.config;

import games.enchanted.mouseskipfix.Constants;
import games.enchanted.mouseskipfix.platform.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class ConfigFileHandler {
    public static final String CONFIG_FILE_NAME = "eg_mouse_skip_fix.properties";

    private static final String SKIPPED_MOUSE_INPUTS = "skipped_mouse_inputs";

    private static String createFileContents() {
        return """
        # The amount of mouse inputs to skip before starting to control the game camera
        # after closing a screen.
        # Any value above 5 may result in noticeable delay when moving the camera
        # after closing an screen
        # Default: %s
        %s=%s
        """.formatted(
            ConfigValues.skippedMouseInputs_DEFAULT, SKIPPED_MOUSE_INPUTS, ConfigValues.skippedMouseInputs
        );
    }

    private static void setConfigFromProperties(Properties p) {
        ConfigValues.skippedMouseInputs = defaultedStringToInt(getProperty(p, SKIPPED_MOUSE_INPUTS), ConfigValues.skippedMouseInputs_DEFAULT);
    }

    public static Path getConfigFilePath() {
        return Services.PLATFORM.getConfigPath().resolve(CONFIG_FILE_NAME);
    }

    public static void saveConfig() {
        Path configFilePath = getConfigFilePath();

        // create file if it doesn't already exist
        if(!Files.exists(configFilePath) ) {
            try {
                Files.createFile(configFilePath);
            } catch ( IOException exception ) {
                Constants.LOG.error("Could not create config file \"" + CONFIG_FILE_NAME + "\"");
                exception.printStackTrace();
                return;
            }
        }

        // save properties to file
        try {
            Files.writeString(configFilePath, createFileContents(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch ( IOException exception ) {
            Constants.LOG.error("Could not write to config file \"" + CONFIG_FILE_NAME + "\"");
            exception.printStackTrace();
        }
    }

    public static void loadConfig() {
        Properties configProperties = new Properties();
        Path configFilePath = getConfigFilePath();

        // save defaults if no config exists
        if( !Files.exists(configFilePath) ) {
            saveConfig();
        }

        // read properties from config file
        try {
            configProperties.load( Files.newInputStream(configFilePath) );
        } catch ( IOException exception ) {
            Constants.LOG.error("Could not read config file \"" + CONFIG_FILE_NAME + "\"");
            exception.printStackTrace();
            return;
        }

        setConfigFromProperties(configProperties);
    }

    private static String getProperty(Properties p, String property) {
        String prop = p.getProperty(property);
        return prop == null ? "" : prop;
    }

    public static int defaultedStringToInt(String str, int defaultVal) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
