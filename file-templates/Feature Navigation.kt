package ${PACKAGE_NAME}

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.navigation.pushComposable
import kotlinx.serialization.Serializable

@Serializable
data object ${NAME}Route

fun NavGraphBuilder.${NAME.substring(0,1).toLowerCase()}${NAME.substring(1)}Screen(
    onBack: () -> Unit,
) {
    pushComposable<${NAME}Route> {
        ${NAME}Screen(
            onBack = onBack,
        )
    }
}
