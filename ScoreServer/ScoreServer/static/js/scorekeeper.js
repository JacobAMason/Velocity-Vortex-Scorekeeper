var ws = new WebSocket("ws://" + location.host + "/scorekeeper/ws");
ws.onopen = function() {
};
ws.onmessage = function (evt) {
    var data = JSON.parse(evt.data);


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
};

function startClock() {
    console.log("clock started");
    ws.send("clock started");
}

$(document).ready(function() {

$("#start-clock").click(function () {
    $(this).prop('disabled', true);
    $("#stop-clock").prop('disabled', false);
    startClock();
})

});