# Documents Android client

Минимальное Android-приложение (Jetpack Compose + Retrofit) для работы с текущим backend.

## Что умеет
- Авторизация через HTTP Basic (`username` + `password`).
- Получение первой страницы документов (`GET /documents/all?page=0`).
- Получение типов документов (`GET /documents/types`).
- Создание документа (`POST /documents`).

## Запуск
1. Откройте папку `documents-android` в Android Studio.
2. Запустите backend на `http://localhost:8080`.
3. Для Android Emulator используйте URL `http://10.0.2.2:8080/`.
4. Введите логин/пароль пользователя backend с правом `ROLE_USER`.

## Ограничения
- Это стартовый клиент, без локальной БД и без полной навигации по всем endpoint'ам.
- При создании документа приоритет пока фиксирован как `LOW`.
