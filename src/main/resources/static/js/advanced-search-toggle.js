const btn = document.getElementById('advanced-search')

window.onload = ()=> {
    const checkBoxes = document.getElementsByClassName('additional-check-box')

    if(expand()){
        for (let i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].style.display = 'block'
        }
    } else {
        for (let i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].style.display = 'none'
        }
    }
}

function toggle(){
    const checkBoxes = document.getElementsByClassName('additional-check-box')

    for (let i = 0; i < checkBoxes.length; i++) {
        if (checkBoxes[i].style.display === 'none') {
            checkBoxes[i].style.display = 'block'

            // btn.textContent="#{search_simple_btn}"
        } else if (checkBoxes[i].style.display === 'block') {
            checkBoxes[i].style.display = 'none'
            // btn.textContent="#{search_advances_btn}"
        } else {
            checkBoxes[i].style.display = 'none'
        }
    }
}

function expand(){
    const checkBoxesInput = document.getElementsByClassName('custom-control-input')

    for (let i = 0; i < checkBoxesInput.length; i++) {
        if(checkBoxesInput[i].checked){
            return true;
        }
    }

    return false;
}

btn.addEventListener('click', (e) => {
    e.preventDefault()
    toggle()
})

