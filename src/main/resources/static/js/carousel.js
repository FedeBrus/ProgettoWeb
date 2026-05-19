const innerCarousel = document.getElementById("carousel").children[0];

const refreshCarousel = ((review) => {
    fetch("/reviews").then(d => {
        if(!d.ok) {
            throw new Error("Error retrieving reviews");
        }

        d.json().then(data => {
            // Cancella il carosello vecchio
            innerCarousel.innerHTML = "";

            if(data.length === 0) {
                const noReviews = document.createElement("div");
                noReviews.classList = "carousel-item text-center active";
                noReviews.textContent = "Non ci sono recensioni al momento :(";
                innerCarousel.appendChild(noReviews);
                return;
            }

            // Shuffle in O(n * log(n))
            data = data.map(value => ({ value, sort: Math.random()}))
                .sort((a, b) => a.sort - b.sort)
                .map(({ value }) => value);

            data.forEach(e => {
                const card = document.createElement("div");
                card.classList = "card carousel-item text-center";
                const header = document.createElement("div");
                header.classList = "card-header";
                header.textContent = "Utente: " + e.username;

                const body = document.createElement("div");
                body.classList = "card-body";

                const text = document.createElement("p");
                text.classList = "card-text";
                text.textContent = e.review;

                body.appendChild(text);
                card.appendChild(header);
                card.appendChild(body);
                if(review != null && review === e.review) {
                    // Se inserisco una review, allora la imposto come primo elemento del carosello
                    innerCarousel.insertBefore(card, innerCarousel.children[0])
                }else {
                    innerCarousel.appendChild(card);
                }
            });

            innerCarousel.children[0].classList += " active";
        });
    }).catch(_ => {
        const error = document.createElement("div");
        error.classList = "carousel-item text-center active";
        error.textContent = "Si è verificato un errore durante il recupero delle recensioni :(";
        innerCarousel.appendChild(error);
    });
});

refreshCarousel(null);