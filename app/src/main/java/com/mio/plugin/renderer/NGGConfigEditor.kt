import android.os.Environment
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

class NGGConfigEditor {
    companion object {
        private var NGG_DIRECTORY_PATH = Environment.getExternalStorageDirectory().path + "/NGG"
        private var CONFIG_FILE_PATH = "$NGG_DIRECTORY_PATH/config.json"
        var LOG_FILE_PATH = "$NGG_DIRECTORY_PATH/latest.log"

        private var configJson: JSONObject? = null

        private fun logError(message: String) {
            Log.e("NGGConfigEditor", message)
        }

        private fun logDebug(message: String) {
            Log.d("NGGConfigEditor", message)
        }

        fun configRefresh() {
            try {
                val directory = File(NGG_DIRECTORY_PATH)
                if (!directory.exists()) {
                    val created = directory.mkdirs()
                    if (created) {
                        logDebug("Directory created: $NGG_DIRECTORY_PATH")
                    } else {
                        logError("Failed to create directory: $NGG_DIRECTORY_PATH")
                    }
                    return
                }
                val file = File(CONFIG_FILE_PATH)
                if (!file.exists()) {
                    logError("Unable to open config file $CONFIG_FILE_PATH")
                    return
                }

                val fileContent = FileInputStream(file).use { inputStream ->
                    inputStream.readBytes().toString(Charset.defaultCharset())
                }

                configJson = try {
                    JSONObject(fileContent)
                } catch (e: Exception) {
                    logError("Error parsing config JSON: ${e.message}")
                    null
                }
            } catch (e: IOException) {
                logError("Unable to read config file $CONFIG_FILE_PATH: ${e.message}")
            }
        }

        fun configGetInt(name: String): Int {
            val json = configJson ?: return -1
            return if (json.has(name) && json.opt(name) is Int) {
                json.getInt(name)
            } else {
                logDebug("Config item '$name' not found or not an integer.")
                -1
            }
        }

        fun configGetString(name: String): String? {
            val json = configJson ?: return null
            return if (json.has(name) && json.opt(name) is String) {
                json.getString(name)
            } else {
                logDebug("Config item '$name' not found or not a string.")
                null
            }
        }

        fun configSetString(name: String, value: String) {
            if (configJson == null) {
                configJson = JSONObject()
            }
            configJson?.put(name, value)
            logDebug("Config item '$name' set to '$value'.")
        }

        fun configSetInt(name: String, value: Int) {
            if (configJson == null) {
                configJson = JSONObject()
            }
            configJson?.put(name, value)
            logDebug("Config item '$name' set to '$value'.")
        }

        fun configSaveToFile() {
            try {
                val file = File(CONFIG_FILE_PATH)
                if (!file.exists()) {
                    file.parentFile?.mkdirs() 
                    file.createNewFile()
                }

                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.use {
                    it.write(configJson?.toString(4)?.toByteArray(Charset.defaultCharset()) ?: ByteArray(0))
                }
                logDebug("Config successfully saved to $CONFIG_FILE_PATH.")
            } catch (e: IOException) {
                logError("Error saving config to file: ${e.message}")
            }
        }
    }
}
