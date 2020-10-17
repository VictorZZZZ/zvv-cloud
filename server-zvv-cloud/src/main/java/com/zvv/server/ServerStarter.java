package com.zvv.server;

import core.coders.ZvvDecoder;
import core.coders.ZvvEncoder;
import com.zvv.server.adapters.ServerInputHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NoArgsConstructor;

public class ServerStarter {
    private static final int PORT = 8189;

    public static void main(String[] args) throws Exception {
        (new ServerStarter()).run();
    }

    public void run() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            ((ServerBootstrap)b.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(
                                            new ZvvDecoder(),
                                            new ZvvEncoder(),
                                            new ServerInputHandler());
                }
            });
            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
