# Service Template

Стандартный шаблон проекта на SpringBoot

# Использованные технологии

* [Spring Boot](https://spring.io/projects/spring-boot) – как основной фрэймворк
* [PostgreSQL](https://www.postgresql.org/) – как основная реляционная база данных
* [Redis](https://redis.io/) – как кэш и очередь сообщений через pub/sub
* [testcontainers](https://testcontainers.com/) – для изолированного тестирования с базой данных
* [Liquibase](https://www.liquibase.org/) – для ведения миграций схемы БД
* [Gradle](https://gradle.org/) – как система сборки приложения
* [Lombok](https://projectlombok.org/) – для удобной работы с POJO классами
* [MapStruct](https://mapstruct.org/) – для удобного маппинга между POJO классами

# База данных

* База поднимается в отдельном сервисе [infra](../infra)
* Redis поднимается в единственном инстансе тоже в [infra](../infra)
* Liquibase сам накатывает нужные миграции на голый PostgreSql при старте приложения
* В тестах используется [testcontainers](https://testcontainers.com/), в котором тоже запускается отдельный инстанс
  postgres
* В коде продемонстрирована работа как с JPA (Hibernate)

# Analytics Service - сбор аналитики о работе сервисов и фичей 
[Сервис](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/service/AnalyticsEventService.java) выполняет работу по сохранению аналитики в общем виде.

[Абстрактный класс](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/listener/AbstractListener.java) для событий определённого типа и [слушатель](https://github.com/Ikhsanov-Nail-95/analytics_service/blob/main/src/main/java/faang/school/analytics/listener/FollowerEventListener.java) ивентов ловит событие и сохраняет сущность в БД
