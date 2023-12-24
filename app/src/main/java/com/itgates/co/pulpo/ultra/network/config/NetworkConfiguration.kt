package com.itgates.co.pulpo.ultra.network.config

import com.google.gson.Gson
import com.itgates.co.pulpo.ultra.BuildConfig
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.OnlineConfigurationData

object NetworkConfiguration {

    var configuration: OnlineConfigurationData = OnlineConfigurationData(
        0, 0, "", "", "https://pulpo.com" + "/android/",
    )

    fun convertToString(): String {
        return Gson().toJson(configuration)
    }

    fun fromStringToConfigurationData(text: String): OnlineConfigurationData {
        return Gson().fromJson(text, OnlineConfigurationData::class.java)
    }

    fun getEndPointsPrefix(): String {
        return if (configuration.system == "P") ".php" else ""
    }
}