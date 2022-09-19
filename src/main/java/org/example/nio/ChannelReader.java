package org.example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelReader {

    private final SocketChannel channel;
    private final ByteBuffer buffer;
    private boolean headerRead = true;
    private byte[] data;
    private int index;


    public ChannelReader(SocketChannel channel) {
        this.channel = channel;
        buffer = ByteBuffer.allocate(1024);
    }

    public void read() {
        try {
            channel.read(buffer);
            if (headerRead) {
                if (buffer.position() < 4) {
                    return;
                }
                buffer.flip();
                int size = buffer.getInt();
                data = new byte[size];
                headerRead = false;
            }
            while (buffer.hasRemaining()) {
                data[index++] = buffer.get();
            }
            buffer.clear();
            if (index == data.length) {
                System.out.println("Received from client: " + new String(data, StandardCharsets.UTF_8));
                String response = "OK";
                byte[] data = response.getBytes(StandardCharsets.UTF_8);
                buffer.putInt(data.length);
                buffer.put(data); // todo: Не работало, потому что забыл данные в буфер положить
                buffer.flip();
                channel.write(buffer);
                System.out.println("Response sent");
                channel.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
