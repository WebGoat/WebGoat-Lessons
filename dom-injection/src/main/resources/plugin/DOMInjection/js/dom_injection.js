function validate(url) {
    var keyField = document.getElementById('key');
    var url = url + "&from=ajax&key=' + encodeURIComponent(keyField.value)";
    if (typeof XMLHttpRequest != 'undefined') {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        req = new ActiveXObject('Microsoft.XMLHTTP');
    }
    req.open('GET', url, true);
    req.onreadystatechange = callback;
    req.send(null);

    function callback() {
        if (req.readyState == 4) {
            if (req.status == 200) {
                var message = req.responseText;
                var messageDiv = document.getElementById('MessageDiv');
                try {
                    eval(message);
                    messageDiv.innerHTML = 'Correct licence Key.'
                } catch (err) {
                    messageDiv.innerHTML = 'Wrong license key.'
                }
            }
        }
    }
}