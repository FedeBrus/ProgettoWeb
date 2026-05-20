const form = document.getElementById("passChangeForm");

form.addEventListener("submit", e => {
    e.preventDefault();
    const password = document.getElementById("inputPassword");
    const newPassword = document.getElementById("inputPasswordConfirmation");
    if(!password.checkValidity()){
        return;
    }

    if(password.value !== newPassword.value) {
        alert("Le password non coincidono");
        return;
    }

    e.target.submit();
});
