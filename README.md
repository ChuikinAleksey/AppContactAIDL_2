Выполнил: Чуйкин Алексей Михайлович

Приложение для удаления дубликатов контактов через AIDL.

- aidl — AIDL интерфейсы (PhoneSort, AsyncCallback, AidlResult, AidlException)
- app_server — сервис удаления контактов
- app_client — клиент (не завершен)
Работает: 
- AIDL интерфейсы и Parcelable классы
- ContactUtil — читает контакты, находит дубликаты
- PhoneRemoveService — реализация AIDL
Главная проблема:
app_server не собирается из-за ошибки ресурсов:

