package com.shalenmathew.quotesapp.presentation.screens.bottom_nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shalenmathew.quotesapp.presentation.theme.bottomNavItem


@Composable
fun BottomNavAnimation(
     navigator:NavHostController
) {

    var selectedScreen by remember { mutableStateOf(0) }
    val haptic = LocalHapticFeedback.current

    val tabItem = listOf(
         BottomNav.Home,
        BottomNav.Fav,
        BottomNav.Settings
    )

    val navBackStackEntry by navigator.currentBackStackEntryAsState()

    // for scenarios when user clicks back press
    LaunchedEffect(navBackStackEntry) {
        val currentDes = navBackStackEntry?.destination?.route
        selectedScreen = tabItem.indexOfFirst { currentDes==it.route }
        if (selectedScreen<0) selectedScreen=0
    }

    Box(
        modifier= Modifier
            .navigationBarsPadding()
            .shadow(5.dp)
            .background(color = Color.Black)  // customize
            .height(80.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (screen in tabItem) {
                val isSelected = screen == tabItem[selectedScreen]
                val animatedWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                Box(
                    modifier = Modifier.weight(animatedWeight),
                    contentAlignment = Alignment.Center,
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    BottomNavItem(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                            selectedScreen = screens.indexOf(screen)
                            navigator.navigate(screen.route){
                                popUpTo(Screen.Home.route){
                                    inclusive=false
                                }
                                launchSingleTop=true
                            }
                        },
                        item = screen,
                        isSelected = isSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    modifier: Modifier = Modifier,
    item: BottomNav,
    isSelected: Boolean,
) {

    val animatedHeight by animateDpAsState(targetValue = if (isSelected) 50.dp else 26.dp)
    val animatedElevation by animateDpAsState(targetValue = if (isSelected) 15.dp else 0.dp)
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(animatedHeight)
                .shadow(
                    elevation = animatedElevation,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.White,
                    spotColor = Color.White
                )
                .background(
                    color = if(isSelected) bottomNavItem else Color.Black,  // customize
                    shape = RoundedCornerShape(20.dp),
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            FlipIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .padding(start = 11.dp)
                    .alpha(animatedAlpha)  // <-------
                    .size(animatedIconSize),  // <-------
                isActive = isSelected,
                activeIcon = item.activeIcon,
                inactiveIcon = item.inactiveIcon,
                contentDescription = "Null")

            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = item.route,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp, end = 10.dp),
                    maxLines = 1,
                )
            }

        }
    }
}

@Composable
fun FlipIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String,
)
{
    val animationRotation by animateFloatAsState(
        targetValue = if (isActive) 180f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    Box(
        modifier = modifier
            .graphicsLayer { rotationY = animationRotation },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

sealed class Screen(
    val route: String,
    val needBottomNav:Boolean
)
{
    object Home: Screen("Home",true)
    object Fav: Screen("Favourites",true)
    object Splash: Screen("Splash",false)
    object Share: Screen("Share",false)
    object Settings: Screen("Settings",true)

    companion object{
        val values:List<Screen> = listOf(Home,Fav,Splash,Share, Settings)
    }

}

sealed class BottomNav(
    val route: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)
{
    object Home: BottomNav(Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home)
    object Fav: BottomNav(Screen.Fav.route, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Settings: BottomNav(Screen.Settings.route, Icons.Filled.Person,Icons.Outlined.Person)
}



