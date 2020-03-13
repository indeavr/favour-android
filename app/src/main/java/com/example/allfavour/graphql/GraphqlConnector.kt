package com.example.allfavour.graphql

import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import android.provider.Settings.System.DATE_FORMAT
import com.apollographql.apollo.response.CustomTypeValue
import com.apollographql.apollo.response.CustomTypeAdapter
import com.example.allfavour.data.model.LocationModel


object GraphqlConnector {
    private const val baseUrl = "https://10.0.2.2:44334/graphql"
    lateinit var client: ApolloClient

    fun setup(context: Context) {
        val unsafeHttpClient = this.getUnsafeOkHttpClient(context)

        this.client = ApolloClient.builder()
            .serverUrl(baseUrl)
            .okHttpClient(unsafeHttpClient)
            .build()
    }

    private fun getUnsafeOkHttpClient(context: Context): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.getSocketFactory()

            val loggingInterceptor = HttpLoggingInterceptor()

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder().apply {
                this.addInterceptor(AuthenticationInterceptor(context))
                this.addInterceptor(loggingInterceptor)
                this.addInterceptor(AuthenticationInterceptor(context))
                // this.authenticator(ApiAuthenticator(context))
            }

            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}