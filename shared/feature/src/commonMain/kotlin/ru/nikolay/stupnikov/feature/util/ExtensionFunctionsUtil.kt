package ru.nikolay.stupnikov.feature.util

import dev.icerock.moko.mvvm.livedata.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.addAll(list: List<T>) {
    val value = this.value
    value.addAll(list)
    this.value = value
}

fun <T> MutableLiveData<MutableList<T>>.clear() {
    this.value.clear()
}