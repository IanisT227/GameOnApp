package com.example.gameonapp.presentation.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.toUri
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import com.example.gameonapp.R
import com.example.gameonapp.presentation.components.AboutBody
import com.example.gameonapp.presentation.components.AboutCard
import com.example.gameonapp.presentation.components.AboutLabel
import com.example.gameonapp.presentation.components.AboutSectionTitle
import com.example.gameonapp.presentation.theme.backgroundGradient
import java.util.Calendar

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    val packageInfo = remember {
        context.packageManager.getPackageInfo(context.packageName, 0)
    }
    val versionName = remember { packageInfo.versionName ?: "—" }
    val versionCode = remember { PackageInfoCompat.getLongVersionCode(packageInfo) }
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    val appName = stringResource(R.string.app_name)
    val publisherName = stringResource(R.string.publisher_name)
    val supportEmail = stringResource(R.string.support_email)

    ScreenScaffold(
        scrollState = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── App identity ──────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutLabel(text = stringResource(id = R.string.version))
                    AboutBody("$versionName (build $versionCode)")
                    Spacer(modifier = Modifier.height(6.dp))
                    AboutLabel(text = stringResource(id = R.string.publisher_name))
                    AboutBody(publisherName)
                    Spacer(modifier = Modifier.height(6.dp))
                    AboutLabel(text = stringResource(id = R.string.copyright))
                    AboutBody("© $currentYear $publisherName. All rights reserved.")
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Scoring ───────────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = stringResource(id = R.string.scoring),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutSectionTitle(text = stringResource(id = R.string.tennis))
                    AboutBody(
                        text = stringResource(id = R.string.tennis_scoring)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutSectionTitle(text = stringResource(id = R.string.padel))
                    AboutBody(
                        text = stringResource(id = R.string.padel_scoring)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Fitness data ──────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = stringResource(id = R.string.fitness_data),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutSectionTitle(text = stringResource(id = R.string.accuracy_notice))
                    AboutBody(text = stringResource(id = R.string.accuracy_disclaimer_text))
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Privacy ───────────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = "Privacy", style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutSectionTitle(text = stringResource(id = R.string.data_stays_here))
                    AboutBody(
                        text = stringResource(id = R.string.data_disclaimer)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Disclaimer ────────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = stringResource(id = R.string.disclaimer),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                AboutCard(transformation = SurfaceTransformation(transformationSpec)) {
                    AboutBody(
                        stringResource(id = R.string.disclaimer_text, appName)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Support ───────────────────────────────────────────────────
            item {
                ListHeader {
                    Text(
                        text = stringResource(id = R.string.support),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                Card(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:$supportEmail".toUri()
                            putExtra(Intent.EXTRA_SUBJECT, "$appName — Support Request")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    AboutSectionTitle("✉  Contact Us")
                    AboutBody(
                        text = supportEmail, textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tap to open email",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}
