package com.electrolytej.main.network.api

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol

class AccountApi {
    fun getSSOSignInUrl() = URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = "accounts.moecube.com",
        pathSegments = listOf("signin")
    ).apply {
        parameters.append("return_sso_url", "https://accounts.moecube.com/match")
    }.build().toString()

    fun getSSOSignOutUrl(callbackUrl: String) = URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = "accounts.moecube.com",
        pathSegments = listOf("signout")
    ).apply {
        // 添加查询参数（自动编码特殊字符）
        parameters.append("return_sso_url", "https://accounts.moecube.com/match")
    }.build().toString()
}