package eu.baroncelli.dkmpsample.shared.viewmodel.screens

import eu.baroncelli.dkmpsample.shared.viewmodel.ScreenIdentifier
import eu.baroncelli.dkmpsample.shared.viewmodel.ScreenState
import eu.baroncelli.dkmpsample.shared.viewmodel.StateManager


/* INITALIZATION BEHAVIOUR (two UI recompositions):
when a screen is first navigated to, using "dkmpNav.navigate(screen,params)", this is what happens in sequence:
1. The navigation function causes the local navigation state to change, causing the FIRST recomposition
2. In the first recomposition, the screen is shown in its "initState" (which typically includes "isLoading=true", showing a "loading..." message)
3. After the first recomposition, the function defined in "callOnInit" is called, which typically loads the data from the Repository
4. The "callOnInit" function, after retrieving the data, typically calls "stateManager.updateScreen()", which updates the state and hence triggers the SECOND recomposition */

class ScreenInitSettings(
    val title: String,
    val initState: (ScreenIdentifier) -> ScreenState,
    val callOnInit: suspend (StateManager) -> Unit,
    val callOnInitAtEachNavigation: Boolean = false,
    /* use cases for callOnInitAtEachNavigation = true:
        By default, screen states are cached, and don't get reinitialized if the screen becomes active again.
        However if you want to refresh it each time it becomes active, you want to call the "callOnInit" function again. */
    val callOnInitAlsoAfterBackground: Boolean = false,
    /* use cases for callOnInitAlsoAfterBackground = true:
        By default, the "callOnInit" function is not called again when the app comes back from the background.
        However in use cases when data might have changed in the meantime, you might want to call "callOnInit" again. */
    val clearStateCacheWhenScreenIsRemovedFromBackstack: Boolean = false,
    /* use cases for clearStateCacheWhenScreenIsRemovedFromBackstack = true:
        By default, screen states are cached, i.e. if the data has already been retrieved, the repository won't be called again.
        In case the state of a specific screen is particularly heavy, you might want to clear its state cache when it's removed from the backstack */
)