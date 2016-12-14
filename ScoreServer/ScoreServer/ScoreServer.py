import json

import time
from bottle import static_file, Bottle, request, abort

from ScoreKeeper import ScoreKeeper

app = Bottle()
scorekeeper = ScoreKeeper()


@app.get("/hello")
def hello():
    return "Hello World"


@app.get("/scorekeeper")
def get_scorekeeper():
    return static_file("scorekeeper.html", "pages")


connections = set()
@app.route('/scorekeeper/ws')
def handle_websocket():
    wsock = request.environ.get('wsgi.websocket')
    if not wsock:
        abort(400, 'Expected WebSocket request.')
    connections.add(wsock)

    while True:
        try:
            message = wsock.receive()
            wsock.send("Your message was: %r" % message)
        except WebSocketError:
            break


@app.post("/scorekeeper/reset")
def post_reset():
    reset = request.json.get('reset')
    if reset is 'reset':
        scorekeeper.reset()
        update_clients()
    else:
        abort(400)


@app.post("/scorekeeper/submit")
def post_score():
    alliance = request.json.get('alliance')
    goal = request.json.get('goal')
    score = request.json.get('score')
    if scorekeeper.update_score(alliance, goal, score):
        update_clients()
        return json.dumps({"status": "ok"})
    else:
        abort(400, "Score POST didn't contain correct keys")


def update_clients():
    [wsock.send(json.dumps(scorekeeper.get_scores())) for wsock in connections]


if __name__ == '__main__':
    from gevent.pywsgi import WSGIServer
    from geventwebsocket import WebSocketError
    from geventwebsocket.handler import WebSocketHandler

    server = WSGIServer(("0.0.0.0", 3486), application=app,
                        handler_class=WebSocketHandler)
    server.serve_forever()
