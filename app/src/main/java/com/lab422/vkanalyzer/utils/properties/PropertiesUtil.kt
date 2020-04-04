package com.lab422.vkanalyzer.utils.properties

import android.content.Context
import android.content.res.AssetManager
import com.lab422.vkanalyzer.BuildConfig
import java.io.IOException
import java.io.InputStream
import java.util.Properties



object PropertiesUtil {

    private const val analyticsPropertiesFile = "analytics.properties"
    private const val amplitudeDebugKey = "amplitudeDebugKey"
    private const val amplitudeReleaseKey = "amplitudeReleaseKey"

    private const val yandexMetricaDebugKey = "yandexMetricaDebugKey"
    private const val  yandexMetricaReleaseKey = "yandexMetricaReleaseKey"

    fun getAmplitudeKey(context: Context): String {
        val key = if (BuildConfig.DEBUG) amplitudeDebugKey else amplitudeReleaseKey
        return getProperty(key, analyticsPropertiesFile, context)
    }

    fun getYandexMetricaKey(context: Context): String {
        val key = if (BuildConfig.DEBUG) yandexMetricaDebugKey else yandexMetricaReleaseKey
        return getProperty(key, analyticsPropertiesFile, context)
    }

    @Throws(IOException::class)
    private fun getProperty(key: String, fileName: String, context: Context): String {
        val properties = Properties()
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        properties.load(inputStream)
        inputStream.close()
        return properties.getProperty(key)
    }
}