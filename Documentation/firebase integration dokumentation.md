# Dokumentation: Cloud- & Daten-Infrastruktur

Diese Dokumentation beschreibt die Implementierung der Daten-Pipeline zwischen dem lokalen API-Server (Docker) und der Firebase Cloud-Datenbank.

## 1. Lokale API-Anbindung (Retrofit)
Die Kommunikation erfolgt über Retrofit mit einem lokal gehosteten `db-vendo-client` Server.
- **Speicherort:** `app/src/main/java/com/moco/DBNavigatorAlternative/data/api/DBApiService.kt`
- **Base URL:** `http://10.0.2.2:3000/` (Emulator-Bridge zum Host-PC).
- **Konfiguration:** 
    - `android:usesCleartextTraffic="true"` in der `AndroidManifest.xml` erlaubt unverschlüsselten HTTP-Verkehr.
    - Timeouts von 30 Sekunden gewährleisten Stabilität innerhalb der Emulator-Umgebung.

## 2. Lokale Datenbank (Room)
Sämtliche für die lokale Persistenz relevanten Datenstrukturen sind im Paket `room_entities` gebündelt.
- **Speicherort:** `app/src/main/java/com/moco/DBNavigatorAlternative/domain/model/room_entities/`
- **Entität:** `Station.kt` definiert das Schema für die lokale Speicherung von Bahnhofsinformationen.

## 3. Cloud-Integration (Firebase)
Die App nutzt Firebase Firestore zur Archivierung und Analyse von Pünktlichkeitsdaten.
- **Modelle:** Befinden sich im Paket `domain/model/cloud/`.
- **`CloudTrip`**: Dient der Erfassung von Verspätungsstatistiken (`delayInSeconds`).

## 4. Speicherstrategien
- **Stammdaten (Stations):** Verwendung von `.document(id).set()` zur Vermeidung von Redundanz.
- **Historische Daten (Trips):** Verwendung von `.add()` zur Erzeugung einer Zeitreihe für die Berechnung der historischen Pünktlichkeit (Estimated Delay).

## 5. Implementierungsstatus
Die gesamte Infrastruktur ist implementiert und funktionsgeprüft. Die automatisierten Synchronisationsroutinen im `HomeViewModel` sind aktuell auskommentiert, um einen stabilen App-Zustand zu gewährleisten, können jedoch für Analysezwecke jederzeit reaktiviert werden.
