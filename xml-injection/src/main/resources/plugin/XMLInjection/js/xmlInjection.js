function getRewards(url) {

    var accountIDField = document.getElementById('accountID');
    if (accountIDField.value.length < 6) {
        return;
    }
    var url = url + '&from=ajax&accountID=' + encodeURIComponent(accountIDField.value);
    if (typeof XMLHttpRequest != 'undefined') {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        req = new ActiveXObject('Microsoft.XMLHTTP');
    }
    req.open('GET', url, false);
    req.onreadystatechange = callback;
    req.send(null);
}

function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var rewards = req.responseXML.getElementsByTagName('reward');
            var rewardsDiv = document.getElementById('rewardsDiv');
            rewardsDiv.innerHTML = '';
            var strHTML = '';
            strHTML = '<tr><td>&nbsp;</td><td><b>Rewards</b></td></tr>';
            for (var i = 0; i < rewards.length; i++) {
                strHTML = strHTML + '<tr><td><input name=\"check' + (i + 1001) + '\" type=\"checkbox\"></td><td>';
                strHTML = strHTML + rewards[i].firstChild.nodeValue + '</td></tr>';
            }
            strHTML = '<table>' + strHTML + '</table>';
            strHTML = 'Your account balance is now 100 points<br><br>' + strHTML;
            rewardsDiv.innerHTML = strHTML;
        }
    }
}