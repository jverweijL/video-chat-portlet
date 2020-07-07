package com.liferay.demo.react.video.socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.*;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jan Verweij
 */
@Component(
        immediate = true,
        property = {
                "org.osgi.http.websocket.endpoint.path=" +
                        VideoChatSocket.ECHO_WEBSOCKET_PATH
        },
        service = Endpoint.class
)
public class VideoChatSocket extends Endpoint {

    public static final String ECHO_WEBSOCKET_PATH = "/o/videochat";

    private static final Set<Session> sessions = Collections
            .synchronizedSet(new HashSet<Session>());


    @Override
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        session.setMaxBinaryMessageBufferSize(1024*512);
        sessions.add(session);

        session.addMessageHandler(
            new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String text) {
                    try {
                        RemoteEndpoint.Basic remoteEndpoint =
                                session.getBasicRemote();

                        remoteEndpoint.sendText(text);
                    }
                    catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }

            }
        );
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Goodbye ! " + closeReason.getReasonPhrase());
        sessions.remove(session);
        super.onClose(session, closeReason);
    }


}