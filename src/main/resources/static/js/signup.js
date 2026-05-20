document.getElementById('signupForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const name = document.getElementById("inputName").value;
    const surname = document.getElementById("inputSurname").value;
    const email = document.getElementById("inputEmail").value;
    const username = document.getElementById("inputUsername").value;

    if (name.trim() === "") {
        alert("Nessun nome inserito");
        return;
    }

    if (surname.trim() === "") {
        alert("Nessun cognome inserito");
        return;
    }

    if (email.trim() === "") {
        alert("Nessun indirizzo email inserito");
        return;
    }

    if (username.trim() === "") {
        alert("Nessun username inserito");
        return;
    }

    const dobInput = document.getElementById("inputDOB").value;

    if (dobInput === "") {
        alert("Nessuna data di nascita inserita");
        return;
    }

    const dob = new Date(dobInput);
    const today = new Date();
    const earliestAcceptableDate= new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());

    if (dob > earliestAcceptableDate) {
        alert("Bisogna essere almeno maggiorenni");
        return;
    }

    const password = document.getElementById("inputPassword").value;
    const confirmation = document.getElementById("inputPasswordConfirmation").value;

    if (password.length !== 8 || !password.includes("id_10")) {
        alert("La password non rispetta i vincoli di sicurezza");
        return;
    }

    if (password !== confirmation) {
        alert("Le password non coincidono");
        return;
    }

    const radioButtons = Array.from(document.getElementsByName("role"));
    if (radioButtons.filter(rb => rb.checked).length !== 1) {
        alert("Nessun piano selezionato");
        return;
    }

    e.target.submit();
});