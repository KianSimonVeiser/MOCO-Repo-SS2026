package com.moco.DBNavigatorAlternative.presentation.generalUse

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.R
import com.moco.DBNavigatorAlternative.presentation.generalUse.Location.checkLocationPermission

/**
 * DIE SUCHE-KARTE
 * Komponente für die Auswahl von Start- und Zielbahnhof.
 */
@Composable
fun SearchSection(
    fromTextFieldState: TextFieldState,
    toTextFieldState: TextFieldState,
    locationNeeded: Boolean,
    fromSearchResultValue: List<NearbyLocationDto>,
    toSearchResultValue: List<NearbyLocationDto>,
    onFromChanged: (TextFieldState) -> Unit,
    onToChange: (TextFieldState) -> Unit,
    onFromItemSelected: (NearbyLocationDto) -> Unit = {},
    onToItemSelected: (NearbyLocationDto) -> Unit = {},
    onLocationClick: () -> Unit,
    onLocationDismissed: () -> Unit,
    onLocationAccepted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onLocationAccepted()
            }
        }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.travel_planning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SimpleSearchBar(
                    textFieldState = fromTextFieldState,
                    onSearch = { onFromChanged(fromTextFieldState) },
                    searchResults = fromSearchResultValue,
                    onItemSelected = onFromItemSelected,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onLocationClick) {
                OutlinedTextField(
                    value = fromValue,
                    onValueChange = onFromChange,
                    label = { Text(stringResource(id = R.string.from_label)) },
                    placeholder = { Text(stringResource(id = R.string.from_placeholder)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    shape = MaterialTheme.shapes.medium
                )

                FilledIconButton(
                    onClick = onLocationClick,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Standort für Start verwenden"
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(id = R.string.location_permission_rationale)
                    )
                }
            }

            // Zu
            Row(
            OutlinedTextField(
                value = toValue,
                onValueChange = onToChange,
                label = { Text(stringResource(id = R.string.to_label)) },
                placeholder = { Text(stringResource(id = R.string.to_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SimpleSearchBar(
                    textFieldState = toTextFieldState,
                    onSearch = { onToChange(toTextFieldState) },
                    searchResults = toSearchResultValue,
                    onItemSelected = onToItemSelected,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (locationNeeded) {
            val hasPermission = checkLocationPermission(context)
            if (hasPermission) {
                LaunchedEffect(Unit) { onLocationAccepted() }
            } else {
                val showRationale = activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.ACCESS_FINE_LOCATION
                )

                if (showRationale) {
                    AlertDialog(
                        onDismissRequest = onLocationDismissed,
                        title = {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = onLocationDismissed) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Schließen")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(id = R.string.close_dialog)
                                )
                            }
                        },
                        text = { Text("Um Stationen in deiner Nähe anzuzeigen, musst du den Standort freigeben.") },
                        confirmButton = {
                            TextButton(onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.location_permission_rationale)
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                ).apply {
                                    data = Uri.fromParts(
                                        "package",
                                        context.packageName,
                                        null
                                    )
                                }
                                context.startActivity(intent)
                            }) { Text(stringResource(id = R.string.go_to_settings)) }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }
}
