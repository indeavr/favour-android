package com.example.allfavour.data.model

import com.allfavour.graphql.api.LoginMutation
import com.allfavour.graphql.api.LoginWithGoogleMutation
import com.allfavour.graphql.api.RegisterMutation
import com.allfavour.graphql.api.type.FavourInputType


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class AuthModel(
    val userId: String,
    val token: String,
    val fullName: String,
    val permissions: PermissionsModel,
    val lastAccountSide: String?
) {

    companion object {
        fun fromGraphType(graphType: LoginMutation.Login): AuthModel {
            return AuthModel(
                userId = graphType.userId,
                token = graphType.token,
                fullName = graphType.fullName,
                permissions = PermissionsModel.fromGraphType(graphType.permissions),
                lastAccountSide = graphType.lastAccountSide
            )
        }

        fun fromGraphType(graphType: RegisterMutation.Register): AuthModel {
            return AuthModel(
                userId = graphType.userId,
                token = graphType.token,
                fullName = graphType.fullName,
                permissions = PermissionsModel.fromGraphType(graphType.permissions),
                lastAccountSide = graphType.lastAccountSide
            )
        }

        fun fromGraphType(graphType: LoginWithGoogleMutation.LoginWithGoogle): AuthModel {
            return AuthModel(
                userId = graphType.userId,
                token = graphType.token,
                fullName = graphType.fullName,
                permissions = PermissionsModel.fromGraphType(graphType.permissions),
                lastAccountSide = graphType.lastAccountSide
            )
        }
    }
}
