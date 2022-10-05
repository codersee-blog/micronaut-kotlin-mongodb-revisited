package com.codersee.controller

import com.codersee.dto.AppUserRequest
import com.codersee.dto.SearchRequest
import com.codersee.model.AppUser
import com.codersee.service.AppUserService
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.HttpStatus.NO_CONTENT
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@Controller("/users")
@ExecuteOn(TaskExecutors.IO)
class AppUserController(
    private val appUserService: AppUserService
) {

    @Get
    fun findAllUsers(): List<AppUser> =
        appUserService.findAll()

    @Get("/{id}")
    fun findById(@PathVariable id: String): AppUser =
        appUserService.findById(id)

    @Post
    @Status(CREATED)
    fun createUser(@Body request: AppUserRequest): AppUser =
        appUserService.create(request)

    @Post("/search")
    fun searchUsers(@Body searchRequest: SearchRequest): List<AppUser> =
        appUserService.findByNameLike(
            name = searchRequest.name
        )

    @Put("/{id}")
    fun updateById(
        @PathVariable id: String,
        @Body request: AppUserRequest
    ): AppUser =
        appUserService.update(id, request)

    @Delete("/{id}")
    @Status(NO_CONTENT)
    fun deleteById(@PathVariable id: String) =
        appUserService.deleteById(id)
}