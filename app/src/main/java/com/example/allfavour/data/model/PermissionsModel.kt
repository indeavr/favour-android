package com.example.allfavour.data.model

import com.allfavour.graphql.api.LoginMutation
import com.allfavour.graphql.api.LoginWithGoogleMutation
import com.allfavour.graphql.api.RegisterMutation

class PermissionsModel(
    val hasSufficientInfoConsumer: Boolean,
    val hasSufficientInfoProvider: Boolean,
    val sideChosen: Boolean
) {
    companion object {
        fun fromGraphType(graphType: RegisterMutation.Permission): PermissionsModel {
            return PermissionsModel(
                hasSufficientInfoConsumer = graphType.hasSufficientInfoConsumer,
                hasSufficientInfoProvider = graphType.hasSufficientInfoProvider,
                sideChosen = graphType.sideChosen
            )
        }

        fun fromGraphType(graphType: LoginWithGoogleMutation.Permission): PermissionsModel {
            return PermissionsModel(
                hasSufficientInfoConsumer = graphType.hasSufficientInfoConsumer,
                hasSufficientInfoProvider = graphType.hasSufficientInfoProvider,
                sideChosen = graphType.sideChosen
            )
        }

        fun fromGraphType(graphType: LoginMutation.Permission): PermissionsModel {
            return PermissionsModel(
                hasSufficientInfoConsumer = graphType.hasSufficientInfoConsumer,
                hasSufficientInfoProvider = graphType.hasSufficientInfoProvider,
                sideChosen = graphType.sideChosen
            )
        }

    }
}