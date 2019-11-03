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
            method: "GET"
        }).then(function (getBeneficialOwnersJson) {
            info();
            var getBeneficialOwnersString = JSON.stringify(getBeneficialOwnersJson, null, '\t');
            $('#beneficial-owners-response').text(getBeneficialOwnersString);
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });
    
    $('#get-graph').click(function () {
        var companyId = $('#get-graph-company-id').val();

        info('Get graph');
        return $.ajax({
            url: "api/entities/" + companyId + "/graph",
            method: "GET"
        }).then(function (getGraphResponseJson) {
            info();
            var getGraphString = JSON.stringify(getGraphResponseJson, null, '\t');
            $('#get-graph-response').text(getGraphString);
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });
    
    $('#update-ownership').click(function () {
        var childCompanyId = $('#update-ownership-child-company-id').val();
        var parentCompanyId = $('#update-ownership-parent-company-id').val();
        var ownershipAmount = $('#update-ownership-ownership-amount-id').val();
        
        info('Get graph');
        return $.ajax({
            url: "api/entities/ownership",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ "childName" : childCompanyId,"parentName" : parentCompanyId, "percentage" : ownershipAmount})

        }).then(function (getGraphResponseJson) {
            info();
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });
    
    $('#create-entity').click(function () {
        var entityName = $('#create-entity-name-id').val();
        var category = $('#create-entity-category-id').val();
        var childCompanyId = $('#create-entity-ownership-child-company-id').val();
        var parentCompanyId = $('#create-entity-ownership-parent-company-id').val();
        var ownershipAmount = $('#create-entity-ownership-amount-id').val();
        
        info('Create entity');
        return $.ajax({
            url: "api/entities",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ "name" : entityName,"category" : category, "childName" : childCompanyId,"parentName" : parentCompanyId, 
                "percentage" : ownershipAmount})

        }).then(function (getGraphResponseJson) {
            info();
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });
    
    $('#delete-entity').click(function () {
        var entityName = $('#delete-entity-name-id').val();
        
        info('Delete entity');
        return $.ajax({
            url: "api/entities/" + entityName,
            method: "DELETE",
        }).fail(function (err) {
            $('#progress .bar').css(
                'width',
                0 + '%'
            );
            console.error(err);
            info('Error:\n' + (err.responseText || err), 5000);
        });
    });

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










