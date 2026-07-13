// Übergreifende Hilfsfunktionen für alle Seiten der ToDo-Verwaltung.

function formatiereDatum(isoDatum) {
    if (!isoDatum) {
        return "–";
    }
    const [jahr, monat, tag] = isoDatum.split("-");
    return `${tag}.${monat}.${jahr}`;
}

function zeigeMeldung(text, istFehler) {
    const element = document.getElementById("meldung");
    if (!element) {
        return;
    }
    element.textContent = text;
    element.className = istFehler ? "meldung fehler" : "meldung erfolg";
    if (!istFehler) {
        setTimeout(() => {
            element.className = "meldung";
            element.textContent = "";
        }, 4000);
    }
}

async function apiAufruf(url, optionen) {
    const antwort = await fetch(url, optionen);
    if (!antwort.ok) {
        let nachricht = `Fehler beim Aufruf des Servers (HTTP-Status ${antwort.status}).`;
        try {
            const fehler = await antwort.json();
            if (fehler && fehler.nachricht) {
                nachricht = fehler.nachricht;
            }
        } catch (parseFehler) {
            console.warn("Fehlerantwort konnte nicht gelesen werden:", parseFehler);
        }
        throw new Error(nachricht);
    }
    if (antwort.status === 204) {
        return null;
    }
    return antwort.json();
}
