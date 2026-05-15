const exerciseDiv = document.getElementById("exercises");

class Exercise {
    constructor(name, sets, reps, kcal) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.kcal = kcal;
    }
}

const addExercise = (() => {
   let added = 0;
   return () => {
       const exercise = document.createElement("div");
        exercise.classList = "border border-light shadow-sm mb-4"

       const header = document.createElement("h4");
       header.textContent = "Esercizio " + (added + 1);

       // Name
       const name = document.createElement("div");
       name.classList = "mb-3";

       const nameLabel = document.createElement("label");
       nameLabel.textContent = "Nome esercizio";
       nameLabel.classList = "form-label";
       nameLabel.for = "nameE" + added;

       const nameInput = document.createElement("input");
       nameInput.type = "text";
       nameInput.classList = "form-control"
       nameInput.id = "nameE" + added;
       nameInput.name = "nameE" + added;
       nameInput.required = true;

       name.appendChild(nameLabel);
       name.appendChild(nameInput);

       // Sets
       const sets = document.createElement("div");
       sets.classList = "mb-3";

       const setsLabel = document.createElement("label");
       setsLabel.textContent = "Numero serie";
       setsLabel.classList = "form-label";
       setsLabel.for = "seriesE" + added;

       const setsInput = document.createElement("input");
       setsInput.type = "number";
       setsInput.classList = "form-control"
       setsInput.id = "seriesE" + added;
       setsInput.name = "seriesE" + added;
       setsInput.required = true;

       sets.appendChild(setsLabel);
       sets.appendChild(setsInput);

       // Ripetizioni
       const reps = document.createElement("div");
       reps.classList = "mb-3";

       const repsLabel = document.createElement("label");
       repsLabel.textContent = "Numero ripetizioni";
       repsLabel.classList = "form-label";
       repsLabel.for = "repsE" + added;

       const repsInput = document.createElement("input");
       repsInput.type = "number";
       repsInput.classList = "form-control"
       repsInput.id = "repsE" + added;
       repsInput.name = "repsE" + added;
       repsInput.required = true;

       reps.appendChild(repsLabel);
       reps.appendChild(repsInput);

       // KCal
       const kcal = document.createElement("div");
       kcal.classList = "mb-3";

       const kcalLabel = document.createElement("label");
       kcalLabel.textContent = "Numero kCal";
       kcalLabel.classList = "form-label";
       kcalLabel.for = "kcalE" + added;

       const kcalInput = document.createElement("input");
       kcalInput.type = "number";
       kcalInput.classList = "form-control"
       kcalInput.id = "kcalE" + added;
       kcalInput.name = "kcalE" + added;
       kcalInput.required = true;


       kcal.appendChild(kcalLabel);
       kcal.appendChild(kcalInput);

       exercise.appendChild(header);
       exercise.appendChild(name);
       exercise.appendChild(sets);
       exercise.appendChild(reps);
       exercise.appendChild(kcal);

       exerciseDiv.appendChild(exercise);
       added++;
   }
})();

document.getElementById("addExercise").addEventListener("click", e => {
    e.preventDefault();
    addExercise();
});

document.getElementById("addProgramForm").addEventListener("formdata", e => {
    e.preventDefault();

    const listOfExercises = [];
    let i = 0;
    while(document.getElementById("nameE" + i)) {
        let e = new Exercise(
            document.getElementById("nameE" + i).value,
            document.getElementById("seriesE" + i).value,
            document.getElementById("repsE" + i).value,
            document.getElementById("kcalE" + i).value,
        );
        listOfExercises.push(e);
        i++;
    }

    e.formData.append("exercises", JSON.stringify(listOfExercises));
});

// Aggiunta automatica del primo esercizio quando si entra nella pagina
addExercise();