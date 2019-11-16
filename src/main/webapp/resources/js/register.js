function registerUser(URL) {
    var firstName = $('#firstName').val();
    var lastName = $('#lastName').val();
    var password = $('#password').val();
    var email = $('#email').val();

    var user = JSON.stringify({
        firstName: firstName,
        lastName: lastName,
        password: password,
        email: email
    });

    console.log(user);

    if ((email == null) || (password == null)) {
        cleanAndAppend($('#info_result'), "Troubles: you don't fill out e-mail or password");
    }
    else {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': URL,
            'data': user,
            'dataType': 'json',
            success: function (result) {

                console.log(result);

                if (result == null ) {
                    cleanAndAppend($('#info_result'), "User is already exist in DB");
                }else {
                    document.location.href = URL + "/details";
                }
            },
            error: function () {
                cleanAndAppend($('#info_result'), "Troubles: user is already exist (or you don't fill out e-mail or password)");
            }
        });

    };

}

function registerDetails(URL) {

    var gender = $('#gender').val();
    var address = $('#house').val();
    var phoneNumber = $('#phonenumber').val();

    var userdetails = JSON.stringify({
        gender: gender,
        address: {id: address},
        phoneNumber: phoneNumber
    });


    if ((gender == null) || (address == null) || (address == -1)|| (phoneNumber == null)) {
        cleanAndAppend($('#info_result'), "Troubles: you don't fill out gender, address or phoneNumber");
    }else {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': URL,
            'data': userdetails,
            'dataType': 'json',
            success: function (result) {
                if (result == null ) {
                    cleanAndAppend($('#info_result'), "User is already exist in DB");
                }else {
                    cleanAndAppend($('#info_result'), "New user created");
                }
            },
            error: function () {
                cleanAndAppend($('#info_result'), "Troubles: user is already exist (or you don't fill out gender, address or phoneNumber)");
            }
        });
    }
}

function cleanAndAppend(param, str) {
    param.empty();
    param.append(str);
}

function changeGeography(URL, selectedindex, id_parent) {

    var chousencountry = JSON.stringify({
        id: document.getElementById(id_parent).options[selectedindex].value
    });

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'type': 'POST',
        'url': URL,
        'data': chousencountry,
        'dataType': 'json',
        success: function (result) {
            var id_child = '';
            switch (id_parent) {
                case 'country':
                    id_child = 'city';
                    break;
                case 'city':
                    id_child = 'street';
                    break;
                case 'street':
                    id_child = 'house';
                    break;
                default:
                    id_child = '';
            }
            if (id_child != '') {
                var resultHTML = '<select name= \'' + id_child + '\' id= \'' + id_child + '\' onchange="changeGeography(\'/register/chousencountry\', this.selectedIndex, \'' + id_child + '\')"> <option value="-1"></option>';
                var i = result.length;
                while (i--) {
                    resultHTML += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
                }
                resultHTML += '</select>';
                switch (id_child) {
                    case 'city':
                        $("#city").replaceWith(resultHTML);
                        $("#street").replaceWith("<select name=\"street\" id=\"street\" disabled=\"disabled\">");
                        $("#house").replaceWith("<select name=\"house\" id=\"house\" disabled=\"disabled\">");
                        break;
                    case 'street':
                        $("#street").replaceWith(resultHTML);
                        $("#house").replaceWith("<select name=\"house\" id=\"house\" disabled=\"disabled\">");
                        break;
                    case 'house':
                        $("#house").replaceWith(resultHTML);
                        break;
                }
            }
        },
        error: function () {
            cleanAndAppend($('#info_result'), "Troubles: smth happens with address");
        }
    });

}

