import java.io.File
import java.util.Properties

object BuildHelper {

    private val isKeyStorePropertiesAvailable: Boolean
        get() = File("keystore.properties").exists()

    fun getBuildProperties(): Properties {
        val properties = Properties()
        val file = File("secret.properties")

        if (file.exists()) properties.load(file.inputStream())

        return properties
    }

    fun getKeyStoreProperties(): Properties = try {
        if (isKeyStorePropertiesAvailable) getKeyStorePropertiesFromFile()
        else getKeyStorePropertiesFromEnv()
    } catch (ex: Exception) {
        Properties().apply {
            setProperty("CORE_KEY_PASSWORD", "")
            setProperty("CORE_ALIAS", "")
        }
    }

    private fun getKeyStorePropertiesFromFile(): Properties {
        val file = File("keystore.properties")
        val properties = Properties()
        properties.load(file.inputStream())
        return properties
    }

    private fun getKeyStorePropertiesFromEnv(): Properties {
        val env: Map<String, String> = System.getenv()
        val properties = Properties()

        properties.setProperty("CORE_KEY_PASSWORD", env["CORE_KEY_PASSWORD"])
        properties.setProperty("CORE_ALIAS", env["CORE_ALIAS"])

        return properties
    }

}
