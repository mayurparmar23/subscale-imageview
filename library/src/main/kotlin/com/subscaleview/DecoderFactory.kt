package com.subscaleview

interface DecoderFactory<T> {
    fun make(): T
}
