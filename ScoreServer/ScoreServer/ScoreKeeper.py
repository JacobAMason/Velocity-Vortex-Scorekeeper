import json


class ScoreKeeper:
    def __init__(self):
        self.scores = {
            'red': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}},
            'blue': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}}
        }

    def update_score(self, gameMode, alliance, goal, score):
        if alliance in self.scores:  # TODO: There has to be a better way of doing this ugly nested garbage
            if gameMode in self.scores[alliance]:
                if goal in self.scores[alliance][gameMode]:
                    self.scores[alliance][gameMode][goal] = int(score)
                    return True
        return False

    def reset(self):
        self.scores = {
            'red': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}},
            'blue': {'autonomous': {'corner': 0, 'center': 0}, 'teleop': {'corner': 0, 'center': 0}}
        }

    def get_score_json(self):
        return json.dumps(self.scores)
