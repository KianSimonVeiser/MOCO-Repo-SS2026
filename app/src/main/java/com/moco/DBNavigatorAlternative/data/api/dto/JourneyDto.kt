package com.moco.DBNavigatorAlternative.data.api.dto

import kotlinx.serialization.Serializable

/**
 * Request DTO für die Fahrplansuche (Mobile V9 API)
 */
@Serializable
data class JourneyRequestDto(
    val autonomeReservierung: Boolean = false,
    val einstiegsTypList: List<String> = listOf("STANDARD"),
    val fahrverguenstigungen: FahrverguenstigungenDto? = null,
    val klasse: String = "KLASSE_2",
    val reiseHin: ReiseHinRequestDto,
    val reisendenProfil: ReisendenProfilDto = ReisendenProfilDto(),
    val reservierungsKontingenteVorhanden: Boolean = false
)

@Serializable
data class FahrverguenstigungenDto(
    val deutschlandTicketVorhanden: Boolean = false,
    val nurDeutschlandTicketVerbindungen: Boolean = false
)

@Serializable
data class ReiseHinRequestDto(
    val wunsch: WunschDto
)

@Serializable
data class WunschDto(
    val abgangsLocationId: String,
    val zielLocationId: String,
    val verkehrsmittel: List<String> = listOf("ALL"),
    val alternativeHalteBerechnung: Boolean = true,
    val zeitWunsch: ZeitWunschDto,
    val maxUmstiege: Int? = null,
    val minUmstiegsdauer: Int? = null,
    val fahrradmitnahme: Boolean = false
)

@Serializable
data class ZeitWunschDto(
    val reiseDatum: String, // ISO 8601
    val zeitPunktArt: String = "ABFAHRT"
)

@Serializable
data class ReisendenProfilDto(
    val reisende: List<ReisenderDto> = listOf(ReisenderDto())
)

@Serializable
data class ReisenderDto(
    val ermaessigungen: List<String> = listOf("KEINE_ERMAESSIGUNG KLASSENLOS"),
    val reisendenTyp: String = "ERWACHSENER",
    val anzahl: Int = 1
)

/**
 * Response DTOs für die Fahrplansuche
 */
@Serializable
data class JourneyResponseDto(
    val verbindungen: List<VerbindungContainerDto> = emptyList()
)

@Serializable
data class VerbindungContainerDto(
    val verbindung: VerbindungDto
)

@Serializable
data class VerbindungDto(
    val reiseDauer: Long, // in Sekunden
    val umstiegeAnzahl: Int,
    val checksum: String? = null,
    val verbindungsAbschnitte: List<AbschnittDto> = emptyList()
)

@Serializable
data class AbschnittDto(
    val typ: String, // "FAHRZEUG", "FUSSWEG"
    val abschnittsDauer: Long? = null,
    val abgangsOrt: OrtDto,
    val abgangsDatum: String,
    val ankunftsOrt: OrtDto,
    val ankunftsDatum: String,
    val mitteltext: String? = null, // z.B. "ICE 941"
    val langtext: String? = null,
    val zugNummer: String? = null,
    val produktGattung: String? = null, // z.B. "ICE", "RB", "BUS"
    val plattform: String? = null
)

@Serializable
data class OrtDto(
    val name: String,
    val locationId: String? = null,
    val evaNr: String? = null,
    val plattform: String? = null
)
