package com.example.kt.demokt

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Component
class CustomerServiceImpl: CustomerService {
    override fun createCustomer(customerMono: Mono<Customer>)=
    customerMono.flatMap {
        if (customers[it.id] == null) {
            customers[it.id] = it
            it.toMono()
        } else {
            Mono.error(CustomerExistException("Customer ${it.id} already exist"))
        }
    }
//    {
//        return customerMono.subscribe {
//            customers[it.id] = it
//        }.toMono()
//    }

    override fun searchFluxCustomers(nameFilter: String) = customers.filter {
        it.value.name.contains(nameFilter, true)
    }.map(Map.Entry<Int, Customer>::value).toFlux()

    companion object {
        val initialCustomers = arrayOf(Customer(1, "Kotolin"),
                Customer(2, "Spring"),
                Customer(3, "Microservice", Customer.Telephone("+44", "7122341233")))
    }
    val customers = ConcurrentHashMap<Int, Customer>(initialCustomers.associateBy(Customer::id))

    override fun searchCustomers(nameFilter: String) = customers.filter {
        it.value.name.contains(nameFilter, true)
    }.map(Map.Entry<Int, Customer>::value).toList()

    override fun getCustomer(id: Int) = customers[id]?.toMono() ?:Mono.empty()

}