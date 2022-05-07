//------------------------------------------------------------------------
//UPDATING FIELD
// keyboard listener.
document.addEventListener('keydown', ev => {
    if (ev.code === "ArrowUp") refreshGame1024field("/game1024/field?command=up");
    else if (ev.code === "ArrowDown") refreshGame1024field("/game1024/field?command=down");
    else if (ev.code === "ArrowLeft") refreshGame1024field("/game1024/field?command=left");
    else if (ev.code === "ArrowRight") refreshGame1024field("/game1024/field?command=right");
});

//refresh field after receiving a request
$(document).ready(function () {
    refreshGame1024field("/game1024/field");
});


function refreshGame1024field(url) {
    $.ajax({
        url: url,
    }).done(function (html) {
        $("#field").html(html);
    });
}

//------------------------------------------------------------------------
//UPDATING ADDCOMMENT
