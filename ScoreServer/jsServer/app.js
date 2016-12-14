var express = require('express');
var passport = require('passport');
var Strategy = require('passport-http').DigestStrategy;
var app = express()

passport.use(new Strategy(
    function(username, password, done) {
        User.findOne({ username: username }, function (err, user) {
            if (err) { return done(err); }
            if (!user) { return done(null, false); }
            if (!user,verifyPassword(password)) { return done(null, false); }
            return done(null, user);
        });
    }
));

app.post('/login',
    passport.authenticate('basic', { session: false }),
    function(req, res) {
        res.json(req.user);
    });

app.get('/', function (req, res) {
    res.send('Hello World')
})

app.listen(3486, function () {
    console.log('Listening on 3486')
})

