package com.dengjian.chestnutshell.activity

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dengjian.chestnutshell.ui.theme.ChestnutShellTheme


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChestnutShellTheme {

            }
        }
    }
}