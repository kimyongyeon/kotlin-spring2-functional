package com.example.kt.demokt

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.onErrorResume
import java.net.URI

@Component
class CustomerHandler(var customerService: CustomerService) {
//    fun get(serverRequest: ServerRequest): Mono<ServerResponse> {
//        return ok().body(Customer(1, "functional web").toMono(), Customer::class.java)
//    }
    // 타입추론 이용
    fun get(serverRequest: ServerRequest) =
        // Customer 객체가 없으면 빈 Mono를 반환하도록 구현을 수정함.
        // nullable 객체를 반환하지 않으므로 body 함수에서 클래스를 지정할 필요가 없기 때문에, 핸들러를 변경할 수 있다.
        ok().body(customerService.getCustomer(serverRequest.pathVariable("id").toInt()))
                // fromObject 메소드를 사용해 새 Mono<Customer>의 값을 변환한다.
                // Mono<Customer>로 Mono<ServerReponse>를 만든다.
                .flatMap { ok().body(fromObject(it)) } // it 객체 안에 Customer 객체를 받는다.
//                .switchIfEmpty(notFound().build())
                .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())
//        ok().body(customerService.getCustomer(1), Customer::class.java)
//    fun get(serverRequest: org.springframework.web.reactive.function.server.ServerRequest)
//        = ok().body(Customer(1, "functional web").toMono(), Customer::class.java)

    fun search(serverRequest: ServerRequest) =
            ok().body(customerService.searchFluxCustomers
            (serverRequest.queryParam("nameFilter").orElse("")), Customer::class.java)

    fun create(serverRequest: ServerRequest) =
            customerService.createCustomer(serverRequest.bodyToMono()).flatMap {
                created(URI.create("/functional/customer/${it.id}")).build()
//                status(HttpStatus.CREATED).body(fromObject(it))
            }.onErrorResume(Exception::class) { // 오류가 발생하면 400 BAD 리퀘스트를 보낸다.
//                badRequest().body(fromObject("error"))
                badRequest().body(fromObject(ErrorResponse("error creating customer", it.message ?: "error")))
            }

}