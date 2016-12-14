class ScoreKeeper:
    def __init__(self):
        self.scores = {
            'red': {'corner': 0, 'center': 0},
            'blue': {'corner': 0, 'center': 0}
        }

    def update_score(self, alliance, goal, score):
        try:
            if goal in self.scores[alliance]:
                self.scores[alliance][goal] = int(score)
                return True
            else:
                return False
        except KeyError:
            return False

    def reset(self):
        self.scores = {
            'red': {'corner': 0, 'center': 0},
            'blue': {'corner': 0, 'center': 0}
        }

    def get_scores(self):
        return self.scores
