# Использованные технологии

* [Spring Boot](https://spring.io/projects/spring-boot) – как основной фрэймворк
* [PostgreSQL](https://www.postgresql.org/) – как основная реляционная база данных
* [Redis](https://redis.io/) – как кэш и очередь сообщений через pub/sub
* [testcontainers](https://testcontainers.com/) – для изолированного тестирования с базой данных
* [Liquibase](https://www.liquibase.org/) – для ведения миграций схемы БД
* [Gradle](https://gradle.org/) – как система сборки приложения
* [Lombok](https://projectlombok.org/) – для удобной работы с POJO классами
* [MapStruct](https://mapstruct.org/) – для удобного маппинга между POJO классами

# Моя работа
[Сервис](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/service/AnalyticsEventService.java) выполняет работу по сохранению аналитики в общем виде.

[Абстрактный класс](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/listener/AbstractListener.java) для событий определённого типа и [слушатель](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/listener/FollowerEventListener.java) ивентов ловит событие и сохраняет сущность в БД
