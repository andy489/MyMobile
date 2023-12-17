function filterOffers(currElement) {

    let currHref = window.location.href.match(/.*offers/)

    currHref += "?"

    let elementsByTagName = document.forms["offer-search-form"].getElementsByClassName("form-control")

    for (let i = 0; i < elementsByTagName.length; i++) {

        let currElem = elementsByTagName[i];

        let searched = currElem.type === "search" && currElem.value !== ""
        let selected = currElem.selectedIndex > 0
        let checked = currElem.type === "checkbox" && currElem.checked

        if (searched || selected || checked) {
            let currName = elementsByTagName[i].name;
            let currValue = elementsByTagName[i].value;

            currHref += currName + "=" + currValue + "&"
        }
    }

    if (currHref.endsWith("&")) {
        currHref = currHref.slice(0, currHref.length - 1)
    }

    currElement.href = currHref

    return currHref
}

function replaceQueryParam(paramName, newValue) {

    let url = getCurrentURL()
    let endPoint = "offers"

    let strRegExPattern = '\\b' + paramName + '\\b=([^&#]*)';

    if (url.match(paramName)) {
        url = url.replace(new RegExp(strRegExPattern), `${paramName}=${newValue}`)
    } else {
        if (url.endsWith(endPoint)) {
            url += "?"
        } else if (url.endsWith("?")) {

        } else {
            url += "&"
        }

        url += `${paramName}=${newValue}`
    }

    return url
}

function gotoPage(currElement, newValue) {
    currElement.href = replaceQueryParam("page", newValue)
}

function getCurrentURL() {
    return window.location.href
}


