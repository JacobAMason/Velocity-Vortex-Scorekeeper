var audio_startMatch = new Audio('sound/STARTMATCH.wav');
var audio_endMatch = new Audio('sound/ENDMATCH.wav');
var audio_30SecondsLeft = new Audio('sound/3BELLS.wav')
var audio_fogHornMatch = new Audio('sound/FOGHORN.wav');
var ws = new WebSocket("ws://" + location.host + "/scorekeeper/ws");

$(document).ready(function(){

var mq = window.matchMedia("(min-aspect-ratio: 32/21)");
mq.addListener(aspectRatioChange);
aspectRatioChange(mq);
function aspectRatioChange(mq) {
    if (mq.matches) {
        $("#background-image").attr("src", "images/bg16x9.png")
    } else {
        $("#background-image").attr("src", "images/bg4x3.png")
    }
}


String.prototype.toMMSS = function () {
    var sec_num = parseInt(this, 10); // don't forget the second param
    var minutes = Math.floor(sec_num / 60);
    var seconds = sec_num - (minutes * 60);

    if (seconds < 10) {seconds = "0"+seconds;}
    return minutes+':'+seconds;
}


ws.onmessage = function (evt) {
    var data = JSON.parse(evt.data);

    if (data.clock) {
        if (data.clock.control) {
            if (data.clock.control === "start-autonomous") {
                $("#clock").html("2:30");
                audio_startMatch.play();
            } else if (data.clock.control === "start-teleop") {
                audio_startMatch.play();
            } else if (data.clock.control === "reset-clock") {
                $("#clock").html("2:30");
            } else if (data.clock.control === "stop-clock") {
                $("#clock").html("<font color='red'>0:00</font>");
                audio_fogHornMatch.play();
            }
        } else if (data.clock.time) {
            $("#clock").html(data.clock.time.toMMSS());
            if (data.clock.time === "120" || data.clock.time === "0") {
                audio_endMatch.play();
            } else if (data.clock.time === "30") {
                audio_30SecondsLeft.play();
            }
        }
    } else {
        $("#auto-blue-center").html(data.blue.autonomous.center);
        $("#auto-blue-corner").html(data.blue.autonomous.corner);
        $("#auto-red-center").html(data.red.autonomous.center);
        $("#auto-red-corner").html(data.red.autonomous.corner);
        $("#teleop-blue-center").html(data.blue.teleop.center);
        $("#teleop-blue-corner").html(data.blue.teleop.corner);
        $("#teleop-red-center").html(data.red.teleop.center);
        $("#teleop-red-corner").html(data.red.teleop.corner);
    }
};
});