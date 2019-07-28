/*
 * Copyright 2019 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.allanwang.kau.kpref

interface KPrefBuilder {
    fun KPref.kpref(key: String, fallback: Boolean, postSetter: (value: Boolean) -> Unit = {}): KPrefDelegate<Boolean>

    fun KPref.kpref(key: String, fallback: Float, postSetter: (value: Float) -> Unit = {}): KPrefDelegate<Float>

    @Deprecated(
        "Double is not supported in SharedPreferences; cast to float yourself",
        ReplaceWith("kpref(key, fallback.toFloat(), postSetter)"),
        DeprecationLevel.WARNING
    )
    fun KPref.kpref(key: String, fallback: Double, postSetter: (value: Float) -> Unit = {}): KPrefDelegate<Float> =
        kpref(key, fallback.toFloat(), postSetter)

    fun KPref.kpref(key: String, fallback: Int, postSetter: (value: Int) -> Unit = {}): KPrefDelegate<Int>

    fun KPref.kpref(key: String, fallback: Long, postSetter: (value: Long) -> Unit = {}): KPrefDelegate<Long>

    fun KPref.kpref(
        key: String,
        fallback: Set<String>,
        postSetter: (value: Set<String>) -> Unit = {}
    ): KPrefDelegate<Set<String>>

    fun KPref.kpref(key: String, fallback: String, postSetter: (value: String) -> Unit = {}): KPrefDelegate<String>

    fun KPref.kprefSingle(key: String): KPrefSingleDelegate
}

object KPrefBuilderAndroid : KPrefBuilder {

    override fun KPref.kpref(key: String, fallback: Boolean, postSetter: (value: Boolean) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefBooleanTransaction, postSetter)

    override fun KPref.kpref(key: String, fallback: Float, postSetter: (value: Float) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefFloatTransaction, postSetter)

    override fun KPref.kpref(key: String, fallback: Int, postSetter: (value: Int) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefIntTransaction, postSetter)

    override fun KPref.kpref(key: String, fallback: Long, postSetter: (value: Long) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefLongTransaction, postSetter)

    override fun KPref.kpref(key: String, fallback: Set<String>, postSetter: (value: Set<String>) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefSetTransaction, postSetter)

    override fun KPref.kpref(key: String, fallback: String, postSetter: (value: String) -> Unit) =
        KPrefDelegateAndroid(key, fallback, this, KPrefStringTransaction, postSetter)

    override fun KPref.kprefSingle(key: String) = KPrefSingleDelegateAndroid(key, this)
}

object KPrefBuilderInMemory : KPrefBuilder {

    override fun KPref.kpref(key: String, fallback: Boolean, postSetter: (value: Boolean) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kpref(key: String, fallback: Float, postSetter: (value: Float) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kpref(key: String, fallback: Int, postSetter: (value: Int) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kpref(key: String, fallback: Long, postSetter: (value: Long) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kpref(key: String, fallback: Set<String>, postSetter: (value: Set<String>) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kpref(key: String, fallback: String, postSetter: (value: String) -> Unit) =
        KPrefDelegateInMemory(key, fallback, this, postSetter)

    override fun KPref.kprefSingle(key: String) = KPrefSingleDelegateInMemory(key, this)
}
