//package com.content.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//
//@Service
//public class UserService {
//
//    private static final String USERS_SERVICE_URL = "http://localhost:8082"; // URL для вашего сервиса пользователей
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public UserEntity getUserById(Long userId) {
//        // Определяем заголовки для запроса
//        HttpHeaders headers = new HttpHeaders();
//        // Добавляем токен в заголовок, если необходимо
//        // headers.add("Authorization", "Bearer " + yourToken);
//
//        // Создаем объект HttpEntity с заголовками
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        // Отправляем GET запрос на сервис пользователей для получения информации о пользователе по его ID
//        ResponseEntity<User> responseEntity = restTemplate.exchange(USERS_SERVICE_URL + "/users/{userId}", HttpMethod.GET, requestEntity, User.class, userId);
//
//        // Получаем тело ответа
//        User user = responseEntity.getBody();
//
//        return user;
//    }
//
//    // Дополнительные методы для взаимодействия с сервисом пользователей
//}
