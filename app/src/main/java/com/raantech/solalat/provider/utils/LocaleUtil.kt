package com.raantech.solalat.provider.utils

import java.util.*

class LocaleUtil {

    companion object {
        fun getLanguage(): String {
            return getLocal().language
        }

        fun getLocal(): Locale {
            return Locale.getDefault()
        }
    }
}