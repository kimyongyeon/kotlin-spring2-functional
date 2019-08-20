package com.example.kt.demokt

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.router

/**
 * 라우터: 리액티브 서비스가 응답하는 경로와 메소드를 처리
 * 핸들러: 구체적인 요청을 응답으로 변환하는 로직을 수행
 * 서비스: 도메인의 비지니스 로직을 캡슐화
 */


// 라우터는 서비스를 호출할 핸들러에게 요청을 보낼 것이다.
// 이 서비스는 이전과 마찬가지로 응답의 body에 들어갈 고객의 플럭스를 반환할 것이다.

@Component
class CustomerRouter(private val customerHandler: CustomerHandler) {

//    @Autowired
//    lateinit var customerHandler: CustomerHandler

    @Bean
    fun customerRoutes(): RouterFunction<*> = router {
        "/functional".nest {
            "/customer".nest {
//                GET("/") {
////                    ServerResponse.ok().body("hello world".toMono(), String::class.java)
////                    ok().body("hello world 단순화".toMono(), String::class.java) // 타입추론때문에 ServerResponse 생략 가능
////                    ok().body(Customer(1, "functional web").toMono(), Customer::class.java) // 값 변환
////                    it: ServerRequest -> ok().body(Customer(1, "functional web").toMono(), Customer::class.java)
////                    it: ServerRequest -> customerHandler.get(it)
//
//                }
                GET("/{id}", customerHandler::get) // 람다가 새 핸들러 함수에 매개 변수를 보내기 때문에, 메서드 참조로 간략화
                POST("/", customerHandler::create)
            }
            "customers".nest {
                GET("/", customerHandler::search)
            }
        }
    }
}