package com.test

import com.test.proto.Furies.*
import com.test.proto.StationServiceGrpcKt
import com.test.proto.startInstrumentResponse
import io.grpc.ServerBuilder

class StationController(val station: Int) {
    fun start() {
        val service = StationService()
        val server = ServerBuilder.forPort(PORT).addService(service).build()
        Runtime.getRuntime().addShutdownHook(Thread { server.shutdown(); server.awaitTermination() })
        println("starting StationController")
        server.start()
        server.awaitTermination()
        println("quitting StationController")
    }

    companion object { const val PORT = 8980 }
}

class StationService : StationServiceGrpcKt.StationServiceCoroutineImplBase() {
    override suspend fun startInstrument(request: StartInstrumentRequest): StartInstrumentResponse {
        println("received RPC startInstrument(${request.which})")
        return startInstrumentResponse { }
    }
}
