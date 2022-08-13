package sourabh.pal.findfalcone.common.data.api

import retrofit2.http.GET
import sourabh.pal.findfalcone.common.data.api.model.ApiPlanet
import sourabh.pal.findfalcone.common.data.api.model.ApiVehicle

interface FindFalconeApi {

  @GET(ApiConstants.VEHICLES_ENDPOINT)
  suspend fun getAllVehicles(): List<ApiVehicle>

  @GET(ApiConstants.PLANETS_ENDPOINT)
  suspend fun getAllPlanets(): List<ApiPlanet>
}