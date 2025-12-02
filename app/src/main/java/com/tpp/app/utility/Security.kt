package com.tpp.app.utility

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

fun validateUserAuthenticationAndAuthorization(account: GoogleSignInAccount?): Boolean {
    if (account == null) return false
    val requiredScopes = setOf(
        Scope(DriveScopes.DRIVE_FILE),
        Scope(DriveScopes.DRIVE_APPDATA),
    )
    if (account.grantedScopes.containsAll(requiredScopes)) {
        return true
    }
    return false
}
