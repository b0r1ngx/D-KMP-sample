package eu.baroncelli.dkmpsample.shared.viewmodel

import eu.baroncelli.dkmpsample.shared.datalayer.objects.CountryExtraData
import eu.baroncelli.dkmpsample.shared.datalayer.objects.CountryListData
import eu.baroncelli.dkmpsample.shared.datalayer.sources.localdb.countries.setCountriesList
import eu.baroncelli.dkmpsample.shared.getTestRepository
import eu.baroncelli.dkmpsample.shared.viewmodel.screens.Screen.*
import eu.baroncelli.dkmpsample.shared.viewmodel.screens.countrydetail.CountryDetailState
import eu.baroncelli.dkmpsample.shared.viewmodel.screens.countrieslist.*
import eu.baroncelli.dkmpsample.shared.viewmodel.screens.countrydetail.CountryDetailParams
import eu.baroncelli.dkmpsample.shared.viewmodel.screens.countrydetail.CountryInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTests {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    val vm: DKMPViewModel = DKMPViewModel(getTestRepository())
    val navigation: Navigation
        get() = vm.navigation
    val stateProvider: StateProvider
        get() = navigation.stateProvider
    val stateManager: StateManager
        get() = navigation.stateManager



    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCountriesListStateUpdate() = runTest {
        val screenIdentifier = ScreenIdentifier.get(CountriesList, CountriesListParams(CountriesListType.ALL))
        navigation.addScreenToBackstack(screenIdentifier)
        stateManager.updateScreen(CountriesListState::class) {
            it.copy(favoriteCountries = mapOf("Italy" to true))
        }
        val screenState = stateProvider.getScreenStateFlow(screenIdentifier).value as CountriesListState
        assertTrue(screenState.favoriteCountries.containsKey("Italy"))
    }

    @Test
    fun testCountryDetailStateUpdate() = runTest {
        stateManager.dataRepository.localDb.setCountriesList(
            listOf(CountryListData(name = "Germany"))
        )
        val screenIdentifier = ScreenIdentifier.get(CountryDetail, CountryDetailParams("Germany"))
        navigation.addScreenToBackstack(screenIdentifier)
        stateManager.updateScreen(CountryDetailState::class) {
            it.copy(countryInfo = CountryInfo(_extraData = CountryExtraData(vaccines = "Pfizer, Moderna, AstraZeneca")))
        }
        val screenState = stateProvider.getScreenStateFlow(screenIdentifier).value as CountryDetailState
        assertTrue(screenState.countryInfo.vaccinesList!!.contains("Pfizer"))
    }

}
