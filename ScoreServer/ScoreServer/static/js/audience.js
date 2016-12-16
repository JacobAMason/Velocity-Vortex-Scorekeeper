var audio_startMatch = new Audio('sound/STARTMATCH.wav');
var audio_endMatch = new Audio('sound/ENDMATCH.wav');
var audio_30SecondsLeft = new Audio('sound/3BELLS.wav')
var audio_fogHornMatch = new Audio('sound/FOGHORN.wav');

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

var timeInterval;
function startClock() {
    var remainingTime = new Date(0);
    remainingTime.setSeconds(5);
    updateClock(remainingTime);
    audio_startMatch.play();
    timeInterval = setInterval(updateClock, 1000, remainingTime);
}

function updateClock(remainingTime) {
    var seconds = remainingTime.getSeconds()
    seconds = (seconds < 10 ? '0' : '') + seconds
    $("#clock").html(remainingTime.getMinutes() + ':' + seconds);
    if (remainingTime.getTime() === 30000){
        audio_30SecondsLeft.play();
    }
    if (remainingTime.getTime() <= 0) {
        audio_endMatch.play();
        clearInterval(timeInterval);
        $("#start-clock").prop('disabled', false);
        $("#stop-clock").prop('disabled', true);
    }
    remainingTime.setTime(remainingTime.getTime() - 1000);
}

$("#start-clock").click(function () {
    $(this).prop('disabled', true);
    $("#stop-clock").prop('disabled', false);
    startClock();
})

});