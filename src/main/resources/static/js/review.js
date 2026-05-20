const form = document.getElementById("reviewForm");
const reviewTextArea = document.getElementById("review");
const container = document.getElementById("container");
const leaveAReviewForm = document.getElementById("leaveAReview");
const toggleForm = document.getElementById("toggleReviewForm");

let alertElement = null;

form.addEventListener("submit", e => {
    e.preventDefault();
    if(!reviewTextArea.checkValidity()) {
        return;
    }

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
        if(d.ok) {
            alertElement.classList += "alert-success";
            alertElement.textContent = "Recensione aggiunta con successo!";
            refreshCarousel(reviewTextArea.value); // Dal file [carousel.js]

            reviewTextArea.value = "";
        } else {
            alertElement.classList += "alert-danger";
            alertElement.textContent = "Errore durante l'aggiunta della recensione: " + d.status
        }
        container.appendChild(alertElement);
    })
});

// Closure
(() => {
    let hidden = true;

    toggleForm.addEventListener("click", _ => {
        if(hidden) {
            leaveAReviewForm.classList.remove("d-none");
        } else {
            leaveAReviewForm.classList.add("d-none");
        }

        hidden = !hidden;
    });
})();