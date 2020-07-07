Inspired by:  

- https://liferay.dev/blogs/-/blogs/developing-javascript-frontends-with-webpack  
- https://www.valentinog.com/blog/babel/
- https://www.youtube.com/watch?v=BpN6ZwFjbCY

Even though you can do great things with react and liferay using liferay-npm-bundler there are some limitations.
One of which is dynamic dependencies.
So I tried a different approach using webpack to create everything I need and use it in a portlet.

To get started:
1. copy this module into liferay workspace modules directory
1. run `npm i`
1. run `./gradlew jar`

## Socket server
The tricky part is that you need a socket.io server.
It's in src/js/server and can be started with `npm i && npm start`

In VideoChat.js you will find this line `socket.current = io.connect("liferay-demo.ddns.net/");` which must be modified to your needs.  
Be aware of the CORS curse!

## Settings used with nginx to reverse-proxy
In case you can configure an nginx reverse-proxy this might be helpful:
```
map $http_sec_websocket_key $upgr {
    ""      "";           # If the Sec-Websocket-Key header is empty, send no upgrade header
    default "websocket";  # If the header is present, set Upgrade to "websocket"
}

map $http_sec_websocket_key $conn {
    ""      $http_connection;  # If no Sec-Websocket-Key header exists, set $conn to the incoming Connection header
    default "upgrade";         # Otherwise, set $conn to upgrade
}

upstream socketio_upstream {
    server 127.0.0.1:8000 weight=5;
    ip_hash;
}

location /socket.io {
    proxy_set_header Upgrade $upgr;
    proxy_set_header Connection $conn;
    proxy_http_version 1.1;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $host;
 }

```
