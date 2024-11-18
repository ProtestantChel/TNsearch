package com.exampletest.testtt.Service;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class ChatService {
    private final EmitterProcessor<String> chatProcessor = EmitterProcessor.create();
    private final FluxSink<String> chatSink = chatProcessor.sink();

    public Flux<String> getChatMessages() {
        return chatProcessor.publish().autoConnect();
    }

    public void sendMessage(String message) {
        chatSink.next(message);
    }
}
