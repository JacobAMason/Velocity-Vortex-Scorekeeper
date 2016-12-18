import json

from bottle import static_file, Bottle, request, abort

from gevent.pywsgi import WSGIServer
from geventwebsocket import WebSocketError
from geventwebsocket.handler import WebSocketHandler

from ScoreKeeper import ScoreKeeper

app = Bottle()
scorekeeper = ScoreKeeper()


@app.get("/hello")
def hello():
    return "Hello World"


@app.route('/scorekeeper/ws')
def handle_websocket():
    wsock = request.environ.get('wsgi.websocket')
    if not wsock:
        abort(400, 'Expected WebSocket request.')
    print "Accepted new connection. Sending current scores."
    wsock.send(scorekeeper.get_score_json())
    scorekeeper.connections.add(wsock)

    while True:
        try:
            data = wsock.receive()
            if data is not None:
                data = json.loads(data)
                if "clock-control" in data:
                    scorekeeper.clock_control(data["clock-control"])
                if "clock" in data:
                    print "Clock", data["clock"]
                    scorekeeper.connections.websocket_broadcast(json.dumps(data))
        except TypeError as e:
            print "Fumbled response:", e
        except WebSocketError:
            break

@app.post("/scorekeeper/reset")
def post_reset():
    reset = request.json.get('reset')
    if reset == 'reset':
        scorekeeper.reset()
    else:
        abort(400)


@app.post("/scorekeeper/submit")
def post_score():
    gameMode = request.json.get('gameMode')
    alliance = request.json.get('alliance')
    goal = request.json.get('goal')
    score = request.json.get('score')
    if scorekeeper.update_score(gameMode, alliance, goal, score):
        return json.dumps({"status": "ok"})
    else:
        abort(400, "Score POST didn't contain correct keys")



@app.route('/<filename:path>')
def send_static(filename):
    if filename.startswith("static/"):
        filename = filename[7:]
    if '.' not in filename:
        filename += ".html"
    print "Trying to load", filename
    return static_file(filename, root="static")


if __name__ == '__main__':
    server = WSGIServer(("0.0.0.0", 3486), application=app,
                        handler_class=WebSocketHandler)
    server.serve_forever()
