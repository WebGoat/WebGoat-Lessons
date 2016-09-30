function getFlights(url) {
    var fromField = document.getElementById('travelFrom');
    var toField = document.getElementById('travelTo');
    var xml = '<?xml version="1.0"?>' +
        '<searchForm>' +
        '  <from>' + fromField.value + '</from>' +
        '</searchForm>';

    if (typeof XMLHttpRequest != 'undefined') {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        req = new ActiveXObject('Microsoft.XMLHTTP');
    }
    req.open('POST', url, true);
    req.onreadystatechange = callback;
    req.send(xml);
}

function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var result = eval('(' + req.responseText + ')');
            var flightsDiv = document.getElementById('flightsDiv');
            flightsDiv.innerHTML = '';

            if (result.successful) {
                //workaround to get the success message displayed. Will be part of WebGoat 8 to fix this
                document.getElementById('lesson-progress').innerHTML = 'Congratulations. You have successfully completed this lesson.';
            }
            var strHTML = '<p>Search results from destination: ' + result.searchCriteria + '</p>';

            if (result.flights.length == 0) {
                strHTML = strHTML + '<p>No results found</p>';
            } else {
                strHTML = strHTML + '<tr><td>Destination</td>';
                strHTML = strHTML + '<td>Departure date</td><td>Arrival date</td><td>Price</td></tr>';

                for (var i = 0; i < result.flights.length; i++) {
                    var node = result.flights[i];
                    strHTML = strHTML + '<tr><td>' + node.destination + '</td><td>';
                    strHTML = strHTML + node.departureDate + '</td><td>';
                    strHTML = strHTML + node.arrivalDate + '</td><td>';
                    strHTML = strHTML + node.price + '</td></tr>';
                }
                strHTML = '<table border=\"1\">' + strHTML + '</table>';

            }
            flightsDiv.innerHTML = strHTML;
        }
    }
}