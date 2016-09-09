function processData() {
    var accountNo = document.getElementById('newAccount').value;
    var amount = document.getElementById('amount').value;
    if (accountNo == '') {
        alert('Please enter a valid account number to transfer to.')
        return;
    }
    else if (amount == '') {
        alert('Please enter a valid amount to transfer.')
        return;
    }
    var balanceValue = document.getElementById('balanceID').innerHTML;
    balanceValue = balanceValue.replace(new RegExp('$'), '');
    if (parseFloat(amount) > parseFloat(balanceValue)) {
        alert('You can not transfer more funds than what is available in your balance.')
        return;
    }
    document.getElementById('confirm').value = 'Transferring'
    submitData(accountNo, amount, url);
    document.getElementById('confirm').value = 'Confirm'
    balanceValue = parseFloat(balanceValue) - parseFloat(amount);
    balanceValue = balanceValue.toFixed(2);
    document.getElementById('balanceID').innerHTML = balanceValue + '$';
}

function submitData(accountNo, balance) {
    var url = document.getElementById("url").value;
    url = url + '&from=ajax&newAccount=' + encodeURIComponent(accountNo) + '&amount=' + balance + '&confirm=' + document.getElementById('confirm').value;
    //var url = '#attack/24/400&from=ajax&newAccount=' + accountNo + '&amount=' + balance + '&confirm=' + document.getElementById('confirm').value;
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
            var result = req.responseText;
            var resultsDiv = document.getElementById('resultsDiv');
            resultsDiv.innerHTML = '';
            resultsDiv.innerHTML = result;
        }
    }
}
