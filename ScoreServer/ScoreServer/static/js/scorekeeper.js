$(document).ready(function() {

var ws = new WebSocket("ws://" + location.host + "/scorekeeper/ws");
ws.onopen = function() {
};
ws.onmessage = function (evt) {
    var data = JSON.parse(evt.data);

    if (data.clock) {
        if (data.clock.time === "120") {
            $("#start-autonomous").prop('disabled', true);
            $("#start-teleop").prop('disabled', false);
            $("#reset-clock").prop('disabled', false);
            $("#stop-clock").prop('disabled', false);
        }
        if (data.clock.time === "0") {
            $("#start-autonomous").prop('disabled', false);
            $("#start-teleop").prop('disabled', false);
            $("#reset-clock").prop('disabled', false);
            $("#stop-clock").prop('disabled', true);
        }
        if (data.clock.control === "stop-clock") {
            $("#start-autonomous").prop('disabled', false);
            $("#start-teleop").prop('disabled', false);
            $("#reset-clock").prop('disabled', false);
            $("#stop-clock").prop("disabled", true);
        }
    } else {
        var tableData = new Array();
        tableData.push("<thead><tr><th>Red Alliance</th><th>Blue Alliance</th></tr></thead><tbody><tr><th colspan='2'>Autonomous</th></tr><tr><td>Center Vortex - ");
        tableData.push(data.red.autonomous.center);
        tableData.push("</td><td>Center Vortex - ");
        tableData.push(data.blue.autonomous.center);
        tableData.push("</td></tr><tr><td>Corner Vortex - ");
        tableData.push(data.red.autonomous.corner);
        tableData.push("</td><td>Corner Vortex - ");
        tableData.push(data.blue.autonomous.corner);
        tableData.push("</td></tr><tr><th colspan='2'>TeleOp</th></tr><tr><td>Center Vortex - ");
        tableData.push(data.red.teleop.center);
        tableData.push("</td><td>Center Vortex - ");
        tableData.push(data.blue.teleop.center);
        tableData.push("</td></tr><tr><td>Corner Vortex - ");
        tableData.push(data.red.teleop.corner);
        tableData.push("</td><td>Corner Vortex - ");
        tableData.push(data.blue.teleop.corner);
        tableData.push("</td></tr></tbody></table>");

        $("#scores").html(tableData.join(""));
    }
};


$("#start-autonomous").click(function () {
    $("#start-autonomous").prop('disabled', true);
    $("#start-teleop").prop('disabled', true);
    $("#reset-clock").prop('disabled', true);
    $("#stop-clock").prop('disabled', false);
    ws.send(JSON.stringify({"clock-control": "start-autonomous"}));
})

$("#start-teleop").click(function () {
    $("#start-autonomous").prop('disabled', true);
    $("#start-teleop").prop('disabled', true);
    $("#reset-clock").prop('disabled', true);
    $("#stop-clock").prop('disabled', false);
    ws.send(JSON.stringify({"clock-control": "start-teleop"}));
})

$("#reset-clock").click(function () {
    ws.send(JSON.stringify({"clock-control": "reset-clock"}));
})

$("#stop-clock").click(function () {
    $("#start-autonomous").prop('disabled', false);
    $("#start-teleop").prop('disabled', false);
    $("#reset-clock").prop('disabled', false);
    $("#stop-clock").prop('disabled', true);
    ws.send(JSON.stringify({"clock-control": "stop-clock"}));
})

});