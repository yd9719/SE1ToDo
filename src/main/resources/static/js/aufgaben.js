

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("aufgabenFormular").addEventListener("submit", speichereAufgabe);
    document.getElementById("abbrechenKnopf").addEventListener("click", brecheBearbeitungAb);
    ladeAufgaben();
});


async function ladeAufgaben() {
    try {
        const aufgaben = await apiAufruf("/api/aufgaben");
        zeichneTabelle(aufgaben);
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}


function zeichneTabelle(aufgaben) {
    const tabelle = document.getElementById("aufgabenTabelle");
    tabelle.replaceChildren();
    document.getElementById("leererHinweis").hidden = aufgaben.length > 0;

    for (const aufgabe of aufgaben) {
        const zeile = document.createElement("tr");
        zeile.appendChild(erzeugeZelle(aufgabe.name));
        zeile.appendChild(erzeugeZelle(aufgabe.kategorie || "–"));
        zeile.appendChild(erzeugeZelle(aufgabe.beschreibung || "–"));
        zeile.appendChild(erzeugeZelle(formatiereDatum(aufgabe.erstelldatum)));

        const unteraufgabenZelle = document.createElement("td");
        const link = document.createElement("a");
        link.href = `aufgabe.html?id=${aufgabe.aufgabeId}`;
        link.textContent = `${aufgabe.anzahlUnteraufgaben} Unteraufgabe(n) anzeigen`;
        unteraufgabenZelle.appendChild(link);
        zeile.appendChild(unteraufgabenZelle);

        zeile.appendChild(erzeugeAktionsZelle(aufgabe));
        tabelle.appendChild(zeile);
    }
}


function erzeugeZelle(text) {
    const zelle = document.createElement("td");
    zelle.textContent = text;
    return zelle;
}


function erzeugeAktionsZelle(aufgabe) {
    const zelle = document.createElement("td");

    const bearbeitenKnopf = document.createElement("button");
    bearbeitenKnopf.type = "button";
    bearbeitenKnopf.textContent = "Bearbeiten";
    bearbeitenKnopf.addEventListener("click", () => fuelleFormular(aufgabe));
    zelle.appendChild(bearbeitenKnopf);

    const loeschenKnopf = document.createElement("button");
    loeschenKnopf.type = "button";
    loeschenKnopf.textContent = "Löschen";
    loeschenKnopf.addEventListener("click", () => loescheAufgabe(aufgabe));
    zelle.appendChild(loeschenKnopf);

    return zelle;
}


function fuelleFormular(aufgabe) {
    document.getElementById("bearbeiteId").value = aufgabe.aufgabeId;
    document.getElementById("eingabeName").value = aufgabe.name;
    document.getElementById("eingabeKategorie").value = aufgabe.kategorie || "";
    document.getElementById("eingabeBeschreibung").value = aufgabe.beschreibung || "";
    document.getElementById("formularUeberschrift").textContent =
        `Aufgabe "${aufgabe.name}" bearbeiten`;
    document.getElementById("speichernKnopf").textContent = "Speichern";
    document.getElementById("abbrechenKnopf").hidden = false;
    document.getElementById("eingabeName").focus();
}


function brecheBearbeitungAb() {
    document.getElementById("aufgabenFormular").reset();
    document.getElementById("bearbeiteId").value = "";
    document.getElementById("formularUeberschrift").textContent = "Neue Aufgabe anlegen";
    document.getElementById("speichernKnopf").textContent = "Anlegen";
    document.getElementById("abbrechenKnopf").hidden = true;
}


async function speichereAufgabe(ereignis) {
    ereignis.preventDefault();

    const name = document.getElementById("eingabeName").value.trim();
    if (name === "") {
        zeigeMeldung("Bitte einen Namen für die Aufgabe eingeben.", true);
        return;
    }

    const daten = {
        name: name,
        kategorie: document.getElementById("eingabeKategorie").value.trim(),
        beschreibung: document.getElementById("eingabeBeschreibung").value.trim()
    };
    const bearbeiteId = document.getElementById("bearbeiteId").value;
    const url = bearbeiteId ? `/api/aufgaben/${bearbeiteId}` : "/api/aufgaben";
    const methode = bearbeiteId ? "PUT" : "POST";

    try {
        await apiAufruf(url, {
            method: methode,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(daten)
        });
        zeigeMeldung(bearbeiteId ? "Aufgabe wurde gespeichert." : "Aufgabe wurde angelegt.", false);
        brecheBearbeitungAb();
        ladeAufgaben();
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}


async function loescheAufgabe(aufgabe) {
    const frage = `Aufgabe "${aufgabe.name}" wirklich löschen?\n` +
        `Alle ${aufgabe.anzahlUnteraufgaben} zugehörigen Unteraufgaben werden mit gelöscht.`;
    if (!confirm(frage)) {
        return;
    }
    try {
        await apiAufruf(`/api/aufgaben/${aufgabe.aufgabeId}`, { method: "DELETE" });
        zeigeMeldung("Aufgabe wurde gelöscht.", false);
        brecheBearbeitungAb();
        ladeAufgaben();
    } catch (fehler) {
        zeigeMeldung(fehler.message, true);
    }
}
