package ru.itech.sno_api.core.context
/*
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.stereotype.Component
import java.util.*

@Component
class RequestContextFilter(
    private val requestContextHolder: RequestContextHolder,
) : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        val requestId = UUID.randomUUID().toString()
        requestContextHolder.setRequestId(requestId)
        try {
            chain.doFilter(request, response)
        } finally {
            requestContextHolder.clear() // Очищаем контекст после обработки запроса
        }
    }

    override fun init(filterConfig: FilterConfig?) {}

    override fun destroy() {}
}

 */
