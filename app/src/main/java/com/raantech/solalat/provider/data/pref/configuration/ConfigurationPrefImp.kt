package com.raantech.solalat.provider.data.pref.configuration

import com.raantech.solalat.provider.common.CommonEnums
import com.raantech.solalat.provider.utils.pref.PrefConstants.APP_LANGUAGE_VALUE
import com.raantech.solalat.provider.utils.pref.SharedPreferencesUtil
import javax.inject.Inject

class ConfigurationPrefImp @Inject constructor(private val prefUtil: SharedPreferencesUtil) :
    ConfigurationPref {

    override fun setAppLanguageValue(selectedLanguageValue: String) {
        prefUtil.setStringPreferences(APP_LANGUAGE_VALUE, selectedLanguageValue)
    }

    override fun getAppLanguageValue(): String {
        return prefUtil.getStringPreferences(
            APP_LANGUAGE_VALUE,
           CommonEnums.Languages.English.value
        )
    }

}