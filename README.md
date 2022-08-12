# shadow-grpc
This is a minimal grpc project packaged with shadowJar, to demonstrate that ./gradlew run and ./gradlew runShadow behave differently.

Issuing an RPC (with no server running) with ./gradlew run says "Connection refused: localhost/[0:0:0:0:0:0:0:1]:8980" as expected.

Running ./gradlew runShadow instead shows this unexpected error:
Exception in thread "main" io.grpc.StatusException: UNKNOWN
        at io.grpc.Status.asException(Status.java:550)
        at io.grpc.kotlin.ClientCalls$rpcImpl$1$1$1.onClose(ClientCalls.kt:296)
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:562)
        at io.grpc.internal.ClientCallImpl.access$300(ClientCallImpl.java:70)
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:743)
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:722)
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37)
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:833)
Caused by: java.nio.channels.UnsupportedAddressTypeException
        at java.base/sun.nio.ch.Net.checkAddress(Net.java:146)
        at java.base/sun.nio.ch.Net.checkAddress(Net.java:157)
        at java.base/sun.nio.ch.SocketChannelImpl.checkRemote(SocketChannelImpl.java:816)
        at java.base/sun.nio.ch.SocketChannelImpl.connect(SocketChannelImpl.java:839)
        at io.netty.util.internal.SocketUtils$3.run(SocketUtils.java:91)
        at io.netty.util.internal.SocketUtils$3.run(SocketUtils.java:88)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:569)
        at io.netty.util.internal.SocketUtils.connect(SocketUtils.java:88)
        at io.netty.channel.socket.nio.NioSocketChannel.doConnect(NioSocketChannel.java:322)
        at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.connect(AbstractNioChannel.java:248)
        at io.netty.channel.DefaultChannelPipeline$HeadContext.connect(DefaultChannelPipeline.java:1342)
        at io.netty.channel.AbstractChannelHandlerContext.invokeConnect(AbstractChannelHandlerContext.java:548)
        at io.netty.channel.AbstractChannelHandlerContext.connect(AbstractChannelHandlerContext.java:533)
        at io.netty.channel.ChannelDuplexHandler.connect(ChannelDuplexHandler.java:54)
        at io.grpc.netty.WriteBufferingAndExceptionHandler.connect(WriteBufferingAndExceptionHandler.java:157)
        at io.netty.channel.AbstractChannelHandlerContext.invokeConnect(AbstractChannelHandlerContext.java:548)
        at io.netty.channel.AbstractChannelHandlerContext.access$1000(AbstractChannelHandlerContext.java:61)
        at io.netty.channel.AbstractChannelHandlerContext$9.run(AbstractChannelHandlerContext.java:538)
        at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:174)
        at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:167)
        at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:470)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:995)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        ... 1 more
