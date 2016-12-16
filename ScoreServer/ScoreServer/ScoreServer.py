import json

from bottle import static_file, Bottle, request, abort

from ScoreKeeper import ScoreKeeper

app = Bottle()
scorekeeper = ScoreKeeper()


@app.get("/hello")
def hello():
    return "Hello World"


connections = set()
@app.route('/scorekeeper/ws')
def handle_websocket():
    wsock = request.environ.get('wsgi.websocket')
    if not wsock:
        abort(400, 'Expected WebSocket request.')
    connections.add(wsock)
    wsock.send(scorekeeper.get_score_json())

    while True:
        try:
            data = wsock.receive()
            print data
        except WebSocketError:
            print "Socket closed"
            break


@app.post("/scorekeeper/reset")
def post_reset():
    reset = request.json.get('reset')
    if reset is 'reset':
        scorekeeper.reset()
        websocket_broadcast(scorekeeper.get_score_json())
    else:
        abort(400)


@app.post("/scorekeeper/submit")
def post_score():
    gameMode = request.json.get('gameMode')
    alliance = request.json.get('alliance')
    goal = request.json.get('goal')
    score = request.json.get('score')
    if scorekeeper.update_score(gameMode, alliance, goal, score):
        websocket_broadcast(scorekeeper.get_score_json())
        return json.dumps({"status": "ok"})
    else:
        abort(400, "Score POST didn't contain correct keys")


def websocket_broadcast(jsonData):
    for wsock in list(connections):
        try:
            wsock.send(json)
        except WebSocketError:
            connections.remove(wsock)


@app.route('/<filename:path>')
def send_static(filename):
    if filename.startswith("static/"):
        filename = filename[7:]
    if '.' not in filename:
        filename += ".html"
    print "Trying to load", filename
    return static_file(filename, root="static")


if __name__ == '__main__':
    from gevent.pywsgi import WSGIServer
    from geventwebsocket import WebSocketError
    from geventwebsocket.handler import WebSocketHandler

    server = WSGIServer(("0.0.0.0", 3486), application=app,
                        handler_class=WebSocketHandler)
    server.serve_forever()
