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
//EMAIL VERIFICATION

async function emailVerificationCodeQuery() {
    event.preventDefault();
    var email = document.getElementById("email").value;

    var login = document.getElementById("login").value;
    var password = document.getElementById("password").value;
    var sent = await $.get('/api/send-email/' + email);
    var counter = 3;
    var verification_code = prompt('Please, write code from email here..\rYou have only ' + counter + ' attempts.\n');
    while (verification_code != sent && counter > 0) {
        if (verification_code == null) break;
        counter--;
        verification_code = prompt('Please, write code from email here..\rYou have only ' + counter + ' attempts.\n');
    }
    document.location.href = "/register/register?email=" + email + "&login=" + login + "&password=" + password;
}


//------------------------------------------------------------------------
//



