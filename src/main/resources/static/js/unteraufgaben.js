

const PRIORITAET_TEXTE = { NIEDRIG: "Niedrig", MITTEL: "Mittel", HOCH: "Hoch" };
const STATUS_TEXTE = { OFFEN: "Offen", IN_ARBEIT: "In Arbeit", ERLEDIGT: "Erledigt" };

const aufgabeId = new URLSearchParams(window.location.search).get("id");

document.addEventListener("DOMContentLoaded", () => {
    if (!aufgabeId) {
        zeigeMeldung("Es wurde keine Aufgabe angegeben (Parameter \"id\" fehlt in der URL).", true);
        return;
    }
    document.getElementById("unteraufgabenFormular").addEventListener("submit", speichereUnteraufgabe);
    document.getElementById("abbrechenKnopf").addEventListener("click", brecheBearbeitungAb);
    ladeAufgabe();
    ladeUnteraufgaben();
});

async function ladeAufgabe() {
    try {
        const aufgabe = await apiAufruf(`/api/aufgaben/${aufgabeId}`);
        document.getElementById("aufgabenName").textContent = `Aufgabe: ${aufgabe.name}`;
        const details = [];
        if (aufgabe.kategorie) {
            details.push(`Kategorie: ${aufgabe.kategorie}`);
        }
        details.push(`Erstellt am: ${formatiereDatum(aufgabe.erstelldatum)}`);
        if (aufgabe.beschreibung) {
            details.push(aufgabe.beschreibung);
        }
        document.getElementById("aufgabenDetails").textContent = details.join(" | ");
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}

async function ladeUnteraufgaben() {
    try {
        const unteraufgaben = await apiAufruf(`/api/aufgaben/${aufgabeId}/unteraufgaben`);
        zeichneTabelle(unteraufgaben);
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}

function zeichneTabelle(unteraufgaben) {
    const tabelle = document.getElementById("unteraufgabenTabelle");
    tabelle.replaceChildren();
    document.getElementById("leererHinweis").hidden = unteraufgaben.length > 0;

    for (const unteraufgabe of unteraufgaben) {
        const zeile = document.createElement("tr");
        zeile.appendChild(erzeugeZelle(PRIORITAET_TEXTE[unteraufgabe.prioritaet]));
        zeile.appendChild(erzeugeZelle(STATUS_TEXTE[unteraufgabe.status]));
        zeile.appendChild(erzeugeZelle(formatiereDatum(unteraufgabe.faelligkeit)));
        zeile.appendChild(erzeugeZelle(formatiereDatum(unteraufgabe.erledigtAm)));
        zeile.appendChild(erzeugeAktionsZelle(unteraufgabe));
        tabelle.appendChild(zeile);
    }
}


function erzeugeZelle(text) {
    const zelle = document.createElement("td");
    zelle.textContent = text;
    return zelle;
}


function erzeugeAktionsZelle(unteraufgabe) {
    const zelle = document.createElement("td");

    const bearbeitenKnopf = document.createElement("button");
    bearbeitenKnopf.type = "button";
    bearbeitenKnopf.textContent = "Bearbeiten";
    bearbeitenKnopf.addEventListener("click", () => fuelleFormular(unteraufgabe));
    zelle.appendChild(bearbeitenKnopf);

    const loeschenKnopf = document.createElement("button");
    loeschenKnopf.type = "button";
    loeschenKnopf.textContent = "Löschen";
    loeschenKnopf.addEventListener("click", () => loescheUnteraufgabe(unteraufgabe));
    zelle.appendChild(loeschenKnopf);

    return zelle;
}


function fuelleFormular(unteraufgabe) {
    document.getElementById("bearbeiteId").value = unteraufgabe.todoId;
    document.getElementById("eingabePrioritaet").value = unteraufgabe.prioritaet;
    document.getElementById("eingabeStatus").value = unteraufgabe.status;
    document.getElementById("eingabeFaelligkeit").value = unteraufgabe.faelligkeit;
    document.getElementById("formularUeberschrift").textContent = "Unteraufgabe bearbeiten";
    document.getElementById("speichernKnopf").textContent = "Speichern";
    document.getElementById("abbrechenKnopf").hidden = false;
}


function brecheBearbeitungAb() {
    document.getElementById("unteraufgabenFormular").reset();
    document.getElementById("bearbeiteId").value = "";
    document.getElementById("formularUeberschrift").textContent = "Neue Unteraufgabe anlegen";
    document.getElementById("speichernKnopf").textContent = "Anlegen";
    document.getElementById("abbrechenKnopf").hidden = true;
}


async function speichereUnteraufgabe(ereignis) {
    ereignis.preventDefault();

    const faelligkeit = document.getElementById("eingabeFaelligkeit").value;
    if (!faelligkeit) {
        zeigeMeldung("Bitte ein Fälligkeitsdatum auswählen.", true);
        return;
    }

    const daten = {
        prioritaet: document.getElementById("eingabePrioritaet").value,
        status: document.getElementById("eingabeStatus").value,
        faelligkeit: faelligkeit
    };
    const bearbeiteId = document.getElementById("bearbeiteId").value;
    const url = bearbeiteId
        ? `/api/unteraufgaben/${bearbeiteId}`
        : `/api/aufgaben/${aufgabeId}/unteraufgaben`;
    const methode = bearbeiteId ? "PUT" : "POST";

    try {
        await apiAufruf(url, {
            method: methode,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(daten)
        });
        zeigeMeldung(bearbeiteId ? "Unteraufgabe wurde gespeichert." : "Unteraufgabe wurde angelegt.", false);
        brecheBearbeitungAb();
        ladeUnteraufgaben();
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}


async function loescheUnteraufgabe(unteraufgabe) {
    const frage = `Unteraufgabe (fällig am ${formatiereDatum(unteraufgabe.faelligkeit)}) wirklich löschen?`;
    if (!confirm(frage)) {
        return;
    }
    try {
        await apiAufruf(`/api/unteraufgaben/${unteraufgabe.todoId}`, { method: "DELETE" });
        zeigeMeldung("Unteraufgabe wurde gelöscht.", false);
        brecheBearbeitungAb();
        ladeUnteraufgaben();
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}
