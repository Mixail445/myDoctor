package com.example.mydoctor.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject


interface Resource {

    fun getString(@StringRes resId: Int): String

}


class ResourceManager @Inject constructor(private val context: Context) : Resource {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

}