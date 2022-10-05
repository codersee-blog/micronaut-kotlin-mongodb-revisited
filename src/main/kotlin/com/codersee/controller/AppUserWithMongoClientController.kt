package com.codersee.controller

import com.codersee.dto.AppUserRequest
import com.codersee.dto.SearchRequest
import com.codersee.model.AppUser
import com.codersee.service.AppUserWithMongoClientService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.HttpStatus.NO_CONTENT
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.net.URI

@Controller("/users-client")
@ExecuteOn(TaskExecutors.IO)
class AppUserWithMongoClientController(
    private val appUserWithMongoClientService: AppUserWithMongoClientService
) {

    @Get
    fun findAllUsers(): List<AppUser> =
        appUserWithMongoClientService.findAll()

    @Get("/{id}")
    fun findById(@PathVariable id: String): AppUser =
        appUserWithMongoClientService.findById(id)

    @Post
    @Status(CREATED)
    fun createUser(@Body request: AppUserRequest): HttpResponse<Void> {
        val createdId = appUserWithMongoClientService.create(request)

        return HttpResponse.created(
            URI.create(
                createdId.asObjectId().value.toHexString()
            )
        )
    }

    @Post("/search")
    @Status(CREATED)
    fun searchUser(@Body searchRequest: SearchRequest): List<AppUser> =
        appUserWithMongoClientService.findByNameLike(
            name = searchRequest.name
        )

    @Put("/{id}")
    fun updateById(
        @PathVariable id: String,
        @Body request: AppUserRequest
    ): AppUser =
        appUserWithMongoClientService.update(id, request)

    @Delete("/{id}")
    @Status(NO_CONTENT)
    fun deleteById(@PathVariable id: String) =
        appUserWithMongoClientService.deleteById(id)
}