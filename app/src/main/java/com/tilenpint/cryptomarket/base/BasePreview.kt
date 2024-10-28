package com.tilenpint.cryptomarket.base

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 411,
    heightDp = 891,
    device = "spec:width=411dp,height=891dp,dpi=420",
    name = "Day mode portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 300,
    heightDp = 600,
    device = "spec:width=300dp,height=600dp,dpi=420",
    name = "Small Device day mode portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 411,
    heightDp = 891,
    device = "spec:width=411dp,height=891dp,dpi=420",
    name = "Night mode portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 300,
    heightDp = 600,
    device = "spec:width=300dp,height=600dp,dpi=420",
    name = "Small Device night mode portrait"
)
annotation class FullScreenPreview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Day mode portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Night mode portrait"
)
annotation class ComponentPreview