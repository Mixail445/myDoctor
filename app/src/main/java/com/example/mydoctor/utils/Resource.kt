package com.example.mydoctor.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

/**
 * Интерфейс для получения строковых ресурсов из приложения.
 */
interface Resource {
    /**
     * Получает строку по заданному идентификатору ресурса.
     *
     * @param resId Идентификатор ресурса строки.
     * @return Строка, соответствующая заданному идентификатору.
     */
    fun getString(@StringRes resId: Int): String

}

/**
 * Реализация интерфейса Resource для управления строковыми ресурсами.
 *
 * @param context Контекст приложения, используемый для доступа к ресурсам.
 */
class ResourceManager @Inject constructor(private val context: Context) : Resource {
    /**
     * Получает строку по заданному идентификатору ресурса.
     *
     * @param resId Идентификатор ресурса строки.
     * @return Строка, соответствующая заданному идентификатору.
     */
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

}