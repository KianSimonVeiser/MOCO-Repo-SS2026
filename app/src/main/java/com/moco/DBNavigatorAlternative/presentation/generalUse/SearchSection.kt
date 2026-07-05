package com.moco.DBNavigatorAlternative.presentation.generalUse

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.moco.DBNavigatorAlternative.presentation.generalUse.Location.checkLocationPermission
import android.provider.Settings
import android.net.Uri
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton


/**
 * DIE SUCHE-KARTE
 * Diese Komponente nutzt eine ElevatedCard, um die Eingabefelder
 * für Start und Ziel optisch vom Hintergrund abzuheben.
 */
@Composable
fun SearchSection(
    fromValue: String,
    toValue: String,
    locationNeeded: Boolean,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    onLocationDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as Activity
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Reiseplanung",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Eingabefeld für den Startpunkt mit Location-Button rechts daneben
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fromValue,
                    onValueChange = onFromChange,
                    label = { Text("Von") },
                    placeholder = { Text("Startbahnhof") },
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
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Aktuellen Standort verwenden"
                    )
                }
            }

            // Eingabefeld für das Ziel
            OutlinedTextField(
                value = toValue,
                onValueChange = onToChange,
                label = { Text("Bis") },
                placeholder = { Text("Zielbahnhof") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                shape = MaterialTheme.shapes.medium
            )
        }
    }
    //Overlay Location Dialog
    if(locationNeeded) {
        if (checkLocationPermission(context)) {
            //TODO: LOcation Event
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog(
                onDismissRequest = {
                    onLocationDismissed()
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                onLocationDismissed()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dialog schließen"
                            )
                        }
                    }
                },
                text = {
                    Text(
                        text = "Um Stationen in deiner Nähe anzuzeigen, " +
                                "musst du den Standort freigeben."
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
                        }
                    ) {
                        Text("Zu den Einstellungen")
                    }
                }
            )
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}