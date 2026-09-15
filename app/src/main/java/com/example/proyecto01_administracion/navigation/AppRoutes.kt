package com.example.proyecto01_administracion.navigation

object AppRoutes {
    const val ARG_PLATE = "plate"
    const val ARG_USER_ID = "userId"

    const val LOGIN = "login"
    const val PASSWORD_RECOVERY = "password_recovery"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"

    const val DRIVER_DASHBOARD = "driver_dashboard"
    const val VEHICLE_DETAILS = "vehicle_details"
    const val REGISTER_MILEAGE = "register_mileage"
    const val MAINTENANCE_HISTORY = "maintenance_history"
    const val MILEAGE_HISTORY = "mileage_history"
    const val VEHICLE_DOCUMENTS = "vehicle_documents"
    const val ALERTS = "alerts"

    const val MECHANIC_DASHBOARD = "mechanic_dashboard"
    const val MECHANIC_VEHICLE_SELECTION = "mechanic_vehicle_selection"
    const val REGISTER_MAINTENANCE = "register_maintenance/{plate}"
    const val MAINTENANCE_DETAIL = "maintenance_detail"

    const val FLEET_MANAGER_DASHBOARD = "fleet_manager_dashboard"
    const val FLEET_MANAGEMENT = "fleet_management"
    const val FLEET_VEHICLE_DETAIL = "fleet_vehicle_detail/{plate}"
    const val REASSIGN_DRIVER = "reassign_driver/{plate}"
    const val VEHICLE_FORM = "vehicle_form"
    const val VEHICLE_FORM_WITH_PLATE = "vehicle_form?plate={plate}"
    const val USER_MANAGEMENT = "user_management"
    const val CREATE_USER = "create_user"
    const val USER_DETAIL = "user_detail/{userId}"
    const val EDIT_USER = "edit_user/{userId}"
    const val REASSIGN_VEHICLE = "reassign_vehicle/{userId}"
    const val REPORTS = "reports"
    const val FLEET_ALERTS = "fleet_alerts"

    fun registerMaintenance(plate: String) = "register_maintenance/$plate"
    fun fleetVehicleDetail(plate: String) = "fleet_vehicle_detail/$plate"
    fun reassignDriver(plate: String) = "reassign_driver/$plate"
    fun vehicleForm(plate: String) = "vehicle_form?$ARG_PLATE=$plate"
    fun userDetail(userId: String) = "user_detail/$userId"
    fun editUser(userId: String) = "edit_user/$userId"
    fun reassignVehicle(userId: String) = "reassign_vehicle/$userId"
}
