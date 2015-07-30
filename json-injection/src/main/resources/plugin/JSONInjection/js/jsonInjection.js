function getFlights(url) {
    var fromField = document.getElementById('travelFrom');
    if (fromField.value.length < 3 || fromField.value != 'BOS') {
        return;
    }
    var toField = document.getElementById('travelTo');
    if (toField.value.length < 3 || toField.value != 'SEA') {
        return;
    }
    url = url + '&from=ajax&TRAVEL_FROM=' + encodeURIComponent(fromField.value) + '&TRAVEL_TO=' + encodeURIComponent(toField.value);

    if (typeof XMLHttpRequest != 'undefined') {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        req = new ActiveXObject('Microsoft.XMLHTTP');
    }
    req.open('GET', url, true);
    req.onreadystatechange = callback;
    req.send(null);
}

function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var card = eval('(' + req.responseText + ')');
            var flightsDiv = document.getElementById('flightsDiv');
            flightsDiv.innerHTML = '';
            var strHTML = '';
            strHTML = '<tr><td>&nbsp;</td><td>No of Stops</td>';
            strHTML = strHTML + '<td>Stops</td><td>Prices</td></tr>';
            for (var i = 0; i < card.flights.length; i++) {
                var node = card.flights[i];
                strHTML = strHTML + '<tr><td><input name=\"radio' + i + '\" type=\"radio\" id=\"radio' + i + '\"></td><td>';
                strHTML = strHTML + card.flights[i].stops + '</td><td>';
                strHTML = strHTML + card.flights[i].transit + '</td><td>';
                strHTML = strHTML + '<div name=\"priceID' + i + '\" id=\"priceID' + i + '\">' + card.flights[i].price + '</div></td></tr>';
            }
            strHTML = '<table border=\"1\">' + strHTML + '</table>';
            flightsDiv.innerHTML = strHTML;
        }
    }
}

function check() {
    if (document.getElementById('radio0') && document.getElementById('radio0').checked) {
        document.getElementById('price2Submit').value = document.getElementById('priceID0').innerHTML;
        return true;
    }
    else if (document.getElementById('radio1') && document.getElementById('radio1').checked) {
        document.getElementById('price2Submit').value = document.getElementById('priceID1').innerHTML;
        return true;
    }
    else {
        alert('Please choose one flight');
        return false;
    }
}