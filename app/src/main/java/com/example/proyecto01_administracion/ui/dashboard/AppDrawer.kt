package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@Composable
fun AppDrawer(
    userName: String,
    userRole: String,
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContainerColor = BackgroundBlack,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Drawer Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = TextGrayLight,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Column {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = userRole,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrayLight
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Drawer Items
            DrawerItem(
                label = "Perfil",
                icon = Icons.Default.Person,
                onClick = onProfileClick
            )
            DrawerItem(
                label = "Configuración",
                icon = Icons.Default.Settings,
                onClick = onSettingsClick
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Logout
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            
            NavigationDrawerItem(
                label = { Text("Cerrar sesión", fontWeight = FontWeight.SemiBold) },
                selected = false,
                onClick = onLogout,
                icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedIconColor = Color(0xFFF44336),
                    unselectedTextColor = Color(0xFFF44336)
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label, fontWeight = FontWeight.Medium) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = TextGrayLight,
            unselectedTextColor = TextWhite
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
