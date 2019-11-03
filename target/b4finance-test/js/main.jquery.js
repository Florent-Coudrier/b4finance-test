$(function () {

    var bodyEl = $('html, body');
    var headerEl = $('.header');
    $('body').css('padding-top', headerEl.height());

    $('#registration-identity').val(localStorage.getItem('registration-identity')).change(function () {
        localStorage.setItem('registration-identity', this.value);
    });
    $('#metadata').val(localStorage.getItem('metadata')).change(function () {
        localStorage.setItem('metadata', this.value);
    });
    $('#policy-otp').prop('checked', localStorage.getItem('policy-otp') == 'true').change(function () {
        localStorage.setItem('policy-otp', this.checked);
    });
    $('#pkic').prop('checked', localStorage.getItem('pkic') == 'true').change(function () {
        localStorage.setItem('pkic', this.checked);
    });
    $('#keyMatchReference').val(localStorage.getItem('keyMatchReference')).change(function () {
        localStorage.setItem('keyMatchReference', this.value);
    });
    $('#rootedDevice').prop('checked', localStorage.getItem('rootedDevice') == 'true').change(function () {
        localStorage.setItem('rootedDevice', this.checked);
    });

    $('#get-beneficial-owners').click(function () {
        var companyId = $('#company-id').val();

        info('Get beneficial owners');
        return $.ajax({
            url: "api/entities/" + companyId + "/owners",
            method: "GET",
            data: {
                companyId: companyId
            }

        }).then(function (registrationInitiatorJson) {
            info();
            $('#beneficial-owners-response').text(registrationInitiatorJson);
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });

    function getBeneficialOwners(companyName) {
        //console.log("keyringid " +keyringId);

        info("please wait ..")

        $.ajax({
                url: "appInstances",
                method: "POST",
                data: {
                    keyringId: companyName
                }
            })
            .then(function (value) {
                info();
                if (value != null
                    && value.length > 0){

                    value.forEach(function (obj){
                        dd.append(new Option(obj.friendlyName,obj.id));
                    })
                }


            })
            .fail(function (err) {
                console.error(err);
                info('Error:\n' + (err.responseText || err), 5000);
            });

    }



    /* BODY */
    var timeoutId;

    function info(message, timeout) {
        clearTimeout(timeoutId);
        $('.info.backdrop').toggle(!!message);
        $('.info.message').text(message || '');
        if (timeout) {
            timeoutId = setTimeout(function () {
                info();
            }, timeout);
        }
    }


    info();

});


function updatePolicies() {
    $.ajax({
        url: "listPolicies",
        method: "POST"

    }).then(function (xml) {

        //console.log("xml",xml);
        policiesObject = xml;
        $("#policies").empty();

        $(xml).find("policy").each(function () {
            var id = $('id', this).text();
            $("#policies").append("<option>" + id + "</option>");
        });

        $("#policies").html($("#policies option").sort(function (a, b) {
            return a.text == b.text ? 0 : a.text < b.text ? -1 : 1
        }))

        if ($(xml).find("policy").size() > 0) {
            document.getElementById('create-registration').disabled = false;
        }

    }).fail(function (err) {
        console.error(err);

    });



}










