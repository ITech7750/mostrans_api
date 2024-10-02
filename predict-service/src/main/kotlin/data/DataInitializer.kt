package ru.itech.datapackage

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.io.File

@Component
class DataInitializer(
    @Autowired val entityManager: EntityManager
) {

    @EventListener(ContextRefreshedEvent::class)
    @Transactional
    fun init() {
        try {
            // Чтение SQL-файла
            val sqlFile = File("predict-service/src/main/resources/mock_data.sql")
            val sqlContent = sqlFile.readText()

            // Разделение SQL-команд по символу ';'
            val sqlCommands = sqlContent.split(";")

            sqlCommands.forEach { sqlCommand ->
                val sql = sqlCommand.trim()
                if (sql.isNotBlank() && !sql.startsWith("--")) {
                    try {
                        // Выполнение SQL команды
                        entityManager.createNativeQuery(sql).executeUpdate()
                    } catch (e: ConstraintViolationException) {
                        // Обработка ошибки дублирования
                        println("Данные уже содержатся в базе данных: ${e.message}")
                    } catch (e: DataIntegrityViolationException) {
                        // Обработка других ошибок целостности данных
                        println("Данные уже содержатся в базе данных: ${e.message}")
                    } catch (e: Exception) {
                        // Обработка всех остальных ошибок
                        println("Ошибка при выполнении SQL команды: $sql")
                        e.printStackTrace()

                        // Выбрасываем исключение, чтобы транзакция была откатана корректно
                        throw e
                    }
                }
            }

            println("Моковые данные успешно загружены в базу данных.")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Ошибка при выполнении SQL команд: ${e.message}")
            throw e // Откатываем транзакцию при критических ошибках
        }
    }
}
