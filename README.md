# Lab1

## Описание проекта

REST API на Spring Boot для управления пользователями и постами.  
Проект использует PostgreSQL, Spring Data JPA, Spring Security, JWT-аутентификацию, Argon2 для хеширования паролей.  
В проект также включены SAST/SCA-проверки: SpotBugs и OWASP Dependency-Check.

### Стек

- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- JJWT 0.13.0
- Argon2
- Lombok
- SpotBugs Maven Plugin
- OWASP Dependency-Check Maven Plugin

## Запуск

### Требования

- JDK 17+
- Maven 3.8+
- PostgreSQL

### Сборка и запуск

```bash
mvn spring-boot:run
```

По умолчанию приложение доступно по адресу:

```text
http://localhost:8080
```

---

## API

Все запросы и ответы — в формате JSON.  
Для защищённых эндпоинтов требуется заголовок `Authorization: Bearer <token>`.

### 1. Создание пользователя

**Метод:** `POST`  
**Путь:** `/api/create_user`  
**Доступ:** публичный  
**Заголовки:** `Content-Type: application/json`

**Тело запроса:**
```json
{
  "login": "alice",
  "password": "StrongPass123!"
}
```

**Успешный ответ:**
```json
{
  "id": 1,
  "login": "alice"
}
```

**Пример вызова:**
```bash
curl -X POST http://localhost:8080/api/create_user \
  -H "Content-Type: application/json" \
  -d '{"login":"alice","password":"StrongPass123!"}'
```

---

### 2. Аутентификация

**Метод:** `POST`  
**Путь:** `/auth/login`  
**Доступ:** публичный  
**Заголовки:** `Content-Type: application/json`

**Тело запроса:**
```json
{
  "login": "alice",
  "password": "StrongPass123!"
}
```

**Успешный ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Пример вызова:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"alice","password":"StrongPass123!"}'
```

Полученный токен нужно передавать в заголовке:

```http
Authorization: Bearer <token>
```

---

### 3. Получение списка постов

**Метод:** `GET`  
**Путь:** `/api/data`  
**Доступ:** требует JWT  
**Заголовки:** `Authorization: Bearer <token>`

**Успешный ответ:**
```json
[
  {
    "id": 1,
    "title": "Hello",
    "text": "World",
    "createdAt": "2025-01-01T12:00:00",
    "owner": {
      "id": 1,
      "login": "alice"
    }
  }
]
```

**Пример вызова:**
```bash
curl http://localhost:8080/api/data \
  -H "Authorization: Bearer $TOKEN"
```

---

### 4. Добавление поста

**Метод:** `POST`  
**Путь:** `/api/add_post`  
**Доступ:** требует JWT  
**Заголовки:**  
`Authorization: Bearer <token>`  
`Content-Type: application/json`

**Тело запроса:**
```json
{
  "title": "Заголовок",
  "text": "Текст поста"
}
```

**Успешный ответ:**
```json
{
  "id": 2,
  "title": "Заголовок",
  "text": "Текст поста",
  "createdAt": "2025-01-01T12:30:00",
  "owner": {
    "id": 1,
    "login": "alice"
  }
}
```

**Пример вызова:**
```bash
curl -X POST http://localhost:8080/api/add_post \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Заголовок","text":"Текст поста"}'
```

---

## Реализованные меры защиты

### Защита от SQL-инъекций (SQLi)

- Доступ к базе данных выполняется через Spring Data JPA.
- Используются репозитории `AccountRepository` и `PostRepository`.
- Метод `findByLogin` — это derived query, Hibernate строит запрос с параметрами.
- Нет конкатенации SQL-строк и нет нативных запросов, собираемых из пользовательского ввода.
- Параметры передаются через bind variables / PreparedStatement, поэтому классические SQLi-векторы не применимы.

### Защита от XSS

- В DTO `ResponseAccount` и `ResponsePost` применяется `HtmlUtils.htmlEscape()`.
- Экранируются:
  - `login` в `ResponseAccount`;
  - `title` и `text` в `ResponsePost`.
- Это предотвращает исполнение HTML/JavaScript, если ответ API будет вставлен в HTML-контекст.
- API отдаёт JSON, поэтому дополнительно рекомендуется на клиенте не использовать `innerHTML` для вывода данных без необходимости.

### Аутентификация и авторизация

- Пароли не хранятся в открытом виде.
- Используется `Argon2PasswordEncoder` со следующими параметрами:
  - salt length = 16;
  - hash length = 32;
  - parallelism = 1;
  - memory = 65536;
  - iterations = 3.
- При создании пользователя `AccountService.saveAccount()` хеширует пароль перед сохранением.
- При логине:
  - `AuthenticationManager` вызывает `DaoAuthenticationProvider`;
  - `DaoAuthenticationProvider` использует `UserDetailsService` и `PasswordEncoder`;
  - `AccountService.loadUserByUsername()` загружает пользователя из БД;
  - Spring Security сравнивает введённый пароль с Argon2-хешем.
- JWT:
  - `JwtService` генерирует токен с алгоритмом HS256;
  - секрет берётся из `jwt.secret`;
  - время жизни — из `jwt.expiration`;
  - в токене хранятся `subject` (login), `issuedAt`, `expiration`.
- `JwtAuthenticationFilter`:
  - читает заголовок `Authorization: Bearer <token>`;
  - проверяет подпись и срок действия;
  - извлекает login;
  - загружает `UserDetails`;
  - проверяет соответствие login и валидность токена;
  - устанавливает `SecurityContext`.
- `SecurityConfig`:
  - `SessionCreationPolicy.STATELESS`;
  - CSRF отключён;
  - `/auth/**` и `/api/create_user` — `permitAll`;
  - остальные запросы — `authenticated`;
  - включена `@EnableMethodSecurity` для возможного использования `@PreAuthorize`.
- Пользователи создаются с ролью `USER`.

---

## SAST/SCA

В `pom.xml` добавлен профиль `security`, который активен по умолчанию.

### SAST: SpotBugs

- Плагин: `spotbugs-maven-plugin` 4.10.4.1
- `effort=Max`
- `threshold=Low`
- `failOnError=true`
- `xmlOutput=true`
- Привязан к фазе `verify`, цель `check`.

Запуск:

```bash
mvn clean verify
```

Отчёт обычно находится в:

```text
target/spotbugsXml.xml
```

При необходимости можно настроить HTML-отчёт.

### SCA: OWASP Dependency-Check

- Плагин: `dependency-check-maven` 13.0.0
- `failBuildOnCVSS=7`
- Форматы: `HTML`, `JSON`
- Использует переменную окружения `NVD_API_KEY`.

Запуск:

```bash
export NVD_API_KEY=your_nvd_api_key
mvn clean verify
```

Отчёты:

```text
target/dependency-check-report.html
target/dependency-check-report.json
```

### CI/CD

В CI/CD pipeline рекомендуется:

1. Добавить секрет `NVD_API_KEY`.
2. Выполнять:

```bash
mvn -B clean verify
```

3. Сохранять отчёты как артефакты:
   - `target/spotbugsXml.xml`
   - `target/dependency-check-report.html`
   - `target/dependency-check-report.json`

Пример для GitHub Actions:

```yaml
name: CI

on:
  push:
  pull_request:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - name: Build and verify
        env:
          NVD_API_KEY: ${{ secrets.NVD_API_KEY }}
        run: mvn -B clean verify
      - name: Upload SpotBugs report
        uses: actions/upload-artifact@v4
        with:
          name: spotbugs-report
          path: target/spotbugsXml.xml
      - name: Upload Dependency-Check report
        uses: actions/upload-artifact@v4
        with:
          name: dependency-check-report
          path: |
            target/dependency-check-report.html
            target/dependency-check-report.json
```

---

## Скриншоты отчётов SAST/SCA


### SpotBugs (SAST)

<img width="904" height="909" alt="image" src="https://github.com/user-attachments/assets/00744f43-5a8d-4c60-9dfa-4805746eced0" />


### OWASP Dependency-Check (SCA)

<img width="977" height="764" alt="image" src="https://github.com/user-attachments/assets/ce9726ae-c4c8-4a63-a196-2892cb7d6dbf" />
