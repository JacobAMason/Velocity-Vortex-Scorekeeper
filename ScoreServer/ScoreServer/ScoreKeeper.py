import json
from threading import Thread, Event

from geventwebsocket import WebSocketError


class Clock(Thread):
    def __init__(self, stopEvent, wsConnections, startTime, stopTime):
        Thread.__init__(self)
        self.stopEvent = stopEvent
        self.wsConnections = wsConnections
        self.startTime = startTime
        self.stopTime = stopTime

    def run(self):
        counter = self.startTime
        while not self.stopEvent.wait(1) and counter > self.stopTime:
            counter -= 1
            self.wsConnections.websocket_broadcast(json.dumps({"clock": {"time": str(counter)}}))


class Connections:
    def __init__(self):
        self.connections = set()

    def add(self, wsock):
        self.connections.add(wsock)

    def websocket_broadcast(self, serializedJSON):
        for wsock in list(self.connections):
            try:
                wsock.send(serializedJSON)
            except WebSocketError:
                self.connections.remove(wsock)


class ScoreKeeper:
    def __init__(self):
        self.connections = Connections()
        self.clock = None
        self.stopClockEvent = None
        self.scores = {
            'red': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}},
            'blue': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}}
        }

    def update_score(self, gameMode, alliance, goal, score):
        if alliance in self.scores:  # TODO: There has to be a better way of doing this ugly nested garbage
            if gameMode in self.scores[alliance]:
                if goal in self.scores[alliance][gameMode]:
                    self.scores[alliance][gameMode][goal] = int(score)
                    self.connections.websocket_broadcast(json.dumps(self.scores))
                    return True
        return False

    def clock_control(self, clockAction):
        print "Clock Control Signal", clockAction
        if clockAction == "start-autonomous":
            if self.clock and self.clock.isAlive():
                return
            self.stopClockEvent = Event()
            self.clock = Clock(self.stopClockEvent, self.connections, startTime=150, stopTime=120)
            self.clock.start()
        elif clockAction == "start-teleop":
            if self.clock and self.clock.isAlive():
                return
            self.stopClockEvent = Event()
            self.clock = Clock(self.stopClockEvent, self.connections, startTime=120, stopTime=0)
            self.clock.start()
        elif clockAction == "reset-clock":
            pass
        elif clockAction == "stop-clock":
            if not (self.stopClockEvent and self.clock):
                return
            self.stopClockEvent.set()
            self.clock = None
        self.connections.websocket_broadcast(json.dumps({"clock": {"control": clockAction}}))

    def reset(self):
        self.scores = {
            'red': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}},
            'blue': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}}
        }
        self.connections.websocket_broadcast(json.dumps(self.scores))

    def get_score_json(self):
        return json.dumps(self.scores)
