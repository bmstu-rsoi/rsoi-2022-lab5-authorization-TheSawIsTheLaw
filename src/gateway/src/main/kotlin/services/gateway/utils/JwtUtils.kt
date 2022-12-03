package services.gateway.utils

import io.jsonwebtoken.Jwts

object JwtUtils {

    private val parser = Jwts.parserBuilder().build()

    @JvmStatic
    fun getUser(jwt: String?): String?
        = try {
            parser.parseClaimsJwt(jwt?.substring(0, jwt.lastIndexOf('.') + 1)).body["name"] as String?
        } catch (ex: Exception) {
            null
        }
}