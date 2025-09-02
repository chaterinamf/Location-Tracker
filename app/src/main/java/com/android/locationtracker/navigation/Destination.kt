package com.android.locationtracker.navigation

enum class Destination(val route: String, val label: String, val description: String) {
    MAP("map_screen", "Map", "Show the map view"),
    LIST("list_screen", "List", "Show the list of the collected pings")
}