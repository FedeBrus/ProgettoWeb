const form = document.getElementById("reviewForm");
const reviewTextArea = document.getElementById("review");
const container = document.getElementById("container");

let alertElement = null;

form.addEventListener("submit", e => {
    e.preventDefault();

    const data = new FormData();
    data.set("review", reviewTextArea.value);

    fetch('/dashboard/review', {
        method: "post",
        body: data
    }).then(d => {
        if(alertElement !== null) {
            container.removeChild(alertElement);
        }
        alertElement = document.createElement("div");
        alertElement.classList += "alert my-2 ";
        if(d.status === 200) {
            alertElement.classList += "alert-success";
            alertElement.textContent = "Recensione aggiunta con successo!";
            reviewTextArea.value = "";
        } else {
            alertElement.classList += "alert-danger";
            alertElement.textContent = "Errore durante l'aggiunta della recensione: " + d.status
        }
        container.appendChild(alertElement);
    })
})