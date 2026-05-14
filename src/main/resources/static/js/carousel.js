const innerCarousel = document.getElementById("carousel").children[0];

const refreshCarousel = ((review) => {
    fetch("/reviews").then(d => {
        d.json().then(t => {
            innerCarousel.innerHTML = "";
            if(t.length === 0) {
                const noReviews = document.createElement("div");
                noReviews.classList = "carousel-item text-center active";
                noReviews.textContent = "Non ci sono recensioni al momento :(";
                innerCarousel.appendChild(noReviews);
                return;
            }

            t = t.map(value => ({ value, sort: Math.random()}))
                .sort((a, b) => a.sort - b.sort)
                .map(({ value }) => value);

            t.forEach(e => {
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
                    innerCarousel.insertBefore(card, innerCarousel.children[0])
                }else {
                    innerCarousel.appendChild(card);
                }
            });

            innerCarousel.children[0].classList += " active";
        });
    })
});

refreshCarousel(null);