package sourabh.pal.findfalcone.common.data.api

import retrofit2.http.GET
import sourabh.pal.findfalcone.common.data.api.model.ApiPlanets
import sourabh.pal.findfalcone.common.data.api.model.ApiVehicles

interface FindFalconeApi {

  @GET(ApiConstants.VEHICLES_ENDPOINT)
  suspend fun getAllVehicles(): ApiVehicles

  @GET(ApiConstants.PLANETS_ENDPOINT)
  suspend fun getAllPlanets(): ApiPlanets
}