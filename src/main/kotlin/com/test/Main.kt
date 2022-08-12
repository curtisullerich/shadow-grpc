package com.test

import com.test.proto.Furies
import com.test.proto.StationServiceGrpcKt
import com.test.proto.startInstrumentRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main() {
    runBlocking {
        StartInstrumentClient().startInstrument(Furies.Instrument.INSTRUMENT_1)
    }
}

class StartInstrumentClient() : Closeable {
    private val channel = ManagedChannelBuilder.forAddress("localhost", StationController.PORT).usePlaintext().build()
    private val stub = StationServiceGrpcKt.StationServiceCoroutineStub(channel)
    suspend fun startInstrument(inst: Furies.Instrument) {
        val request = startInstrumentRequest { which = inst }
        stub.startInstrument(request)
    }
    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
