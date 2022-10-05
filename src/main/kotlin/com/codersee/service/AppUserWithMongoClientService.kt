package com.codersee.service

import com.codersee.dto.AppUserRequest
import com.codersee.model.Address
import com.codersee.model.AppUser
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import org.bson.BsonValue
import org.bson.types.ObjectId

@Singleton
class AppUserWithMongoClientService(
    private val mongoClient: MongoClient
) {

    fun create(userRequest: AppUserRequest): BsonValue {
        val createResult = getCollection()
            .insertOne(userRequest.toAppUserEntity())

        return createResult.insertedId
            ?: throw HttpStatusException(HttpStatus.BAD_REQUEST, "User was not created.")
    }

    fun findAll(): List<AppUser> =
        getCollection()
            .find()
            .toList()

    fun findById(id: String): AppUser =
        getCollection()
            .find(
                Filters.eq("_id", ObjectId(id))
            )
            .toList()
            .firstOrNull()
            ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "User with id: $id was not found.")

    fun update(id: String, updateRequest: AppUserRequest): AppUser {
        val updateResult = getCollection()
            .replaceOne(
                Filters.eq("_id", ObjectId(id)),
                updateRequest.toAppUserEntity()
            )

        if (updateResult.modifiedCount == 0L)
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "User with id: $id was not updated.")

        return findById(id)
    }

    fun deleteById(id: String) {
        val deleteResult = getCollection()
            .deleteOne(
                Filters.eq("_id", ObjectId(id))
            )

        if (deleteResult.deletedCount == 0L)
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "User with id: $id was not deleted.")
    }

    fun findByNameLike(name: String): List<AppUser> =
        getCollection()
            .find(
                Filters.regex("firstName", name)
            )
            .toList()


    private fun getCollection(): MongoCollection<AppUser> =
        mongoClient
            .getDatabase("example")
            .getCollection("app_user", AppUser::class.java)

    private fun AppUserRequest.toAppUserEntity(): AppUser {
        val address = Address(
            street = this.street,
            city = this.city,
            code = this.code
        )

        return AppUser(
            id = null,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            address = address
        )
    }

}