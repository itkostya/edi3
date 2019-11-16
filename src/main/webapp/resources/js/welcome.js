function getUser(URL) {
    var email = $("#email").val();
    var user = null;

    $.ajax({
        url: URL,
        contentType: "application/json",
        dataType: 'json',
        type: "POST",
        email : email,
        success: function (data) {
            user = data;
        },
        error: function () {
            console.log('error');
        }
    });

    return user;
}

function renderUser() {
    var user = getUser();
    $("#userData").htmlText = user;
}