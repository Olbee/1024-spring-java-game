//------------------------------------------------------------------------
// KEYBOARD LISTENER
document.addEventListener('keydown', ev => {
    if (ev.code === "ArrowUp") refreshGame1024field("/game1024/field?command=up");
    else if (ev.code === "ArrowDown") refreshGame1024field("/game1024/field?command=down");
    else if (ev.code === "ArrowLeft") refreshGame1024field("/game1024/field?command=left");
    else if (ev.code === "ArrowRight") refreshGame1024field("/game1024/field?command=right");
});


//------------------------------------------------------------------------
//UPDATING FIELD
$(document).ready(function () {
    refreshGame1024field("/game1024/field");
    updateComments();
    updateScores();
});


async function refreshGame1024field(url) {
    var state = await $.get('/game1024/state');
    if (state === "PLAYING") {
        $.ajax({
            url: url,
        }).done(function (html) {
            $("#field").html(html);
            showScore();
        });
    }
}

//------------------------------------------------------------------------
//EMAIL VERIFICATION

async function emailVerificationCodeQuery() {
    event.preventDefault();
    var email = document.getElementById("email").value;
    var login = document.getElementById("login").value;

    var login_already_used = await $.get('/api/account/1024/loginUsed/' + login);
    var email_already_used = await $.get('/api/account/1024/emailUsed/' + email);
    if (login_already_used !== true && email_already_used !== true) {
        var password = document.getElementById("password").value;
        var sent = await $.get('/api/send-email/' + email);
        var counter = 3;

        var verification_code = prompt('Please, write code from email here..\rYou have only ' + counter + ' attempts.\n');
        if (verification_code == null) return;

        while (verification_code !== sent && counter > 1) {
            counter--;
            verification_code = prompt('Please, write code from email here..\rYou have only ' + counter + ' attempts.\n');
            if (verification_code == null) return;
        }
        if (verification_code === sent) {
            document.location.href = "/register/register?email=" + email + "&login=" + login + "&password=" + password;
        } else {
            alert("You have entered an incorrect verification code.");
        }
    }
    else {
        if (email_already_used === true) alert("Account with this email is already exists.\nPlease, try another.");
        else alert("Account with this email address is already exists.\nPlease, try another.");
    }
}

async function newGame() {

}


//------------------------------------------------------------------------
// ADD COMMENT FUNCTION
function addComment() {
    var username = document.getElementById("userName").textContent;
    var comment = document.getElementById("comment").value;


    var new_comment = new Object();
    new_comment.player = username;
    new_comment.game = "1024";
    new_comment.comment = comment;
    new_comment.commented_on = new Date();

    $.ajax({
        url: '/api/comment',
        type: 'POST',
        data: JSON.stringify(new_comment),
        contentType: 'application/json'
    });
    setTimeout(function (){
        updateComments();
    }, 400);
}

//------------------------------------------------------------------------
// FORMATE DATE
function formatDate(date) {
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();

    if(seconds < 10) seconds = '0' + seconds;
    if(minutes < 10) minutes = '0' + minutes;
    if(hour < 10) hour = '0' + hour;
    if(day < 10) day = '0' + day;
    if(month < 10) month = '0' + month;
    return day + '/' + month + '/' + year + ' ' + hour + ':' + minutes + ':' + seconds;
}

//------------------------------------------------------------------------
// UPDATE COMMENTS TABLE
function updateComments() {
    $("#commentJS").empty();
    $.ajax({
        url: '/api/comment/1024'
    }).done(function(response){
        for (var i = 0; i < response.length ; i++) {
            var comment = response[i];
            var commentedOn = new Date(comment.commented_on);
            var mydate = formatDate(commentedOn);
            $("#commentJS").append("<tr><td>" + comment.player + "</td><td>" + comment.comment + "</td><td>" + mydate + "</td></tr>");
        }
    })
}

//------------------------------------------------------------------------
// ADD SCORE FUNCTION
async function addScore() {

    var state = await $.get('/game1024/state');
    if (state === "WON" || state === "LOST") {
        var username = document.getElementById("userName").textContent;
        var score = await $.get('/game1024/score');

        var new_score = new Object();
        new_score.player = username;
        new_score.game = "1024";
        new_score.score = score;
        new_score.played_on = new Date();

        $.ajax({
            url: '/api/score',
            type: 'POST',
            data: JSON.stringify(new_score),
            contentType: 'application/json'
        });
        setTimeout(function () {
            updateScores();
        }, 400);

    }
}

//------------------------------------------------------------------------
// UPDATE SCORE TABLE
function updateScores() {
    $("#scoresJS").empty();
    $.ajax({
        url: '/api/score/1024'
    }).done(function(response){
        for (var i = 0; i < response.length ; i++) {
            var score = response[i];
            var playedOn = new Date(score.played_on);
            var mydate = formatDate(playedOn)
            $("#scoresJS").append("<tr><td>" + score.player + "</td><td>" + score.points + "</td><td>" + mydate + "</td></tr>");
        }
    })
}
//------------------------------------------------------------------------
// SHOW SCORE VALUE
async function showScore() {
    var score = await $.get('/game1024/score');
    $("#showScore").empty().append(score);
}

