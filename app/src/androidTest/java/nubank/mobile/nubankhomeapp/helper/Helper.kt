package nubank.mobile.nubankhomeapp.helper

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun waitFor(timeMillis: Long = 2000L) { runBlocking { delay(timeMillis) } }