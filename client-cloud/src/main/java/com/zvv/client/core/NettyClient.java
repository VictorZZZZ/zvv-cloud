package com.zvv.client.core;

import com.zvv.client.gui.LoginController;
import com.zvv.client.gui.MainController;
import core.coders.ZvvDecoder;
import core.coders.ZvvEncoder;
import core.messsages.AbstractMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NettyClient implements Runnable {
    private static final String HOST="localhost";
    private static final int PORT = 8189;
    private ChannelFuture channelFuture;
    @Getter
    private static LoginController loginController;
    @Getter
    private static MainController mainController;

    public NettyClient(LoginController loginController, MainController mainController) {
        this.loginController = loginController;
        this.mainController = mainController;
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch)
                        throws Exception {
                    ch.pipeline().addLast(
                            new ZvvEncoder(),
                            new ZvvDecoder(),
                            new ClientInputHandler(loginController,mainController));
                }
            });

            try {
                channelFuture = b.connect(HOST, PORT).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void send(AbstractMessage abstractMessage){
        log.info("Отправка сообщения");
        channelFuture.channel().writeAndFlush(abstractMessage);
    }

    public void stop() {
        channelFuture.channel().close();
    }
}
