# Wikipedia Web Crawler

Веб-краулер на Java Spring Boot. Приложение автоматически определяет язык запроса, парсит страницы используя алгоритм BFS (поиск в ширину) и кэширует результаты в Redis для мгновенной выдачи при повторных запросах.

## Функциональность

*   **Умный поиск:** Автоматически определяет язык запроса.
    *   Кириллица -> ищет на ru.ruwiki.ru
    *   Латиница -> ищет на en.wikipedia.org
*   **Высокая производительность:** Использует Redis для кэширования результатов. Повторный поиск занимает <20 мс.
*   **Алгоритм BFS:** Обходит связанные статьи с ограничением глубины и количества результатов.
*   **Статистика:** Отображает время выполнения запроса и источник данных (Интернет или Кэш).
*   **Полная Докеризация:** Легкий запуск одной командой через Docker Compose.

## Стек технологий

*   **Backend:** Java 21, Spring Boot, Spring Data Redis, Spring Security
*   **Parsing:** Jsoup (HTML парсинг)
*   **Database:** PostgreSQL (пользователи/логи), Redis (кэш)
*   **Frontend:** Thymeleaf, JavaScript (Fetch API), CSS
*   **DevOps:** Docker, Docker Compose

---

## Запуск проекта

### Вариант 1: Через Docker (Рекомендуется)

Убедитесь, что у вас установлен Docker Desktop.

1. Клонируйте репозиторий:
`   git clone https://github.com/ArtVlk/WebCrawler.git
   cd crawler`

2. Запустите приложение:
`   docker-compose up --build`

3. Откройте в браузере:
   http://localhost:8080

### Вариант 2: Локальный запуск (IntelliJ IDEA)

Если вы хотите разрабатывать код локально:

1. Запустите инфраструктуру (Redis + DB) через Docker:
`   docker-compose up -d redis db`

2. Запустите класс CrawlerApplication.

---

## Конфигурация

Основные настройки находятся в `src/main/resources/application.properties` и `docker-compose.yml`.

*   DB_HOST: Адрес базы данных
*   SPRING_DATA_REDIS_HOST: Адрес Redis
*   MAX_DEPTH: Глубина поиска BFS (в коде)
*   MAX_RESULTS: Лимит статей (в коде)

## API Endpoints

Приложение работает через Web-интерфейс, но имеет открытый JSON API.

`POST /api/search
Тело запроса (JSON):
{
"query": "Java Spring"
}`

Ответ (JSON):
`[
{
"url": "https://en.wikipedia.org/wiki/Spring_Framework",
"title": "Spring Framework",
"snippet": "Описание статьи..."
}
]`