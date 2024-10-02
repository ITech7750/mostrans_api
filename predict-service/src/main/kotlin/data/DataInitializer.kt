package ru.itech.datapackage
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.itech.entity.*
import ru.itech.repository.*
import java.io.File

@Component
class DataInitializer(
    @Autowired val userRepository: UserRepository,
    @Autowired val stationRepository: StationRepository,
    @Autowired val stationPassengerFlowRepository: StationPassengerFlowRepository,
    @Autowired val stationConnectionRepository: StationConnectionRepository
) {

    @PostConstruct
    fun init() {
        // Чтение SQL-файла
        val sqlFile = File("predict-service/src/main/resources/mock_data.sql")
        val sqlContent = sqlFile.readText()

        // Разделение на отдельные SQL-команды по символу ';'
        val sqlCommands = sqlContent.split(";")

        sqlCommands.forEach { sqlCommand ->
            val sql = sqlCommand.trim()

            // Игнорируем строки комментариев и пустые строки
            if (sql.isNotBlank() && !sql.startsWith("--")) {
                when {
                    sql.startsWith("INSERT INTO user_table", ignoreCase = true) -> insertUser(sql)
                    sql.startsWith("INSERT INTO stations", ignoreCase = true) -> insertStation(sql)
                    sql.startsWith("INSERT INTO station_passenger_flow", ignoreCase = true) -> insertPassengerFlow(sql)
                    sql.startsWith("INSERT INTO station_connections", ignoreCase = true) -> insertStationConnection(sql)
                    else -> println("Неизвестная SQL команда: $sql")
                }
            }
        }

        println("Моковые данные успешно загружены в базу данных.")
    }


    private fun insertUser(sql: String) {
        val (_, columns, valuesList) = parseInsertStatement(sql)

        valuesList.forEach { values ->
            val columnValues = columns.zip(values).toMap()

            val user = UserEntity.create(
                login = columnValues["login"] ?: "",
                password = columnValues["password"] ?: "",
                email = columnValues["email"] ?: "",
                firstName = columnValues["first_name"],
                lastName = columnValues["last_name"],
                middleName = columnValues["middle_name"],
                role = columnValues["role"]
            )
            userRepository.save(user)
        }
    }

    private fun insertStation(sql: String) {
        val (_, columns, valuesList) = parseInsertStatement(sql)

        valuesList.forEach { values ->
            val columnValues = columns.zip(values).toMap()

            val station = Station(
                name = columnValues["name"] ?: "",
                line = columnValues["line"] ?: "",
                distanceFromCenter = columnValues["distance_from_center"]?.toDoubleOrNull() ?: 0.0
            )
            stationRepository.save(station)
        }
    }

    private fun insertPassengerFlow(sql: String) {
        val (_, columns, valuesList) = parseInsertStatement(sql)

        valuesList.forEach { values ->
            val columnValues = columns.zip(values).toMap()

            val stationId = columnValues["station_id"]?.toLongOrNull()
            val datetime = columnValues["datetime"] ?: ""
            val passengerFlow = columnValues["passenger_flow"]?.toIntOrNull() ?: 0

            if (stationId != null) {
                val station = stationRepository.findById(stationId).orElse(null)
                if (station != null) {
                    val passengerFlowEntity = StationPassengerFlow(
                        station = station,
                        datetime = datetime,
                        passengerFlow = passengerFlow
                    )
                    stationPassengerFlowRepository.save(passengerFlowEntity)
                } else {
                    println("Станция с ID $stationId не найдена.")
                }
            } else {
                println("Некорректный station_id: ${columnValues["station_id"]}")
            }
        }
    }

    private fun insertStationConnection(sql: String) {
        val (_, columns, valuesList) = parseInsertStatement(sql)

        valuesList.forEach { values ->
            val columnValues = columns.zip(values).toMap()

            val station1Id = columnValues["station1_id"]?.toLongOrNull()
            val station2Id = columnValues["station2_id"]?.toLongOrNull()

            if (station1Id != null && station2Id != null) {
                val connection = StationConnection(
                    station1Id = station1Id,
                    station2Id = station2Id
                )
                stationConnectionRepository.save(connection)
            } else {
                println("Некорректные IDs станций: station1_id=${columnValues["station1_id"]}, station2_id=${columnValues["station2_id"]}")
            }
        }
    }

    // Парсинг INSERT-запроса
    private fun parseInsertStatement(sql: String): Triple<String, List<String>, List<List<String>>> {
        val insertRegex = Regex(
            """INSERT INTO (\w+) \(([^)]+)\) VALUES\s*(.+)""",
            RegexOption.IGNORE_CASE
        )

        val matchResult = insertRegex.find(sql)
        if (matchResult != null) {
            val tableName = matchResult.groupValues[1]
            val columns = matchResult.groupValues[2]
                .split(",")
                .map { it.trim() }

            val valuesPart = matchResult.groupValues[3].trim()

            // Парсим несколько наборов значений
            val valuesList = mutableListOf<List<String>>()
            val valueRegex = Regex("""\(([^)]+)\)""")
            val valueMatches = valueRegex.findAll(valuesPart)

            valueMatches.forEach { valueMatch ->
                val values = valueMatch.groupValues[1]
                    .split(",(?=(?:[^']*'[^']*')*[^']*$)".toRegex()) // Учитываем запятые внутри строк
                    .map { it.trim().removeSurrounding("'") }
                valuesList.add(values)
            }

            return Triple(tableName, columns, valuesList)
        } else {
            throw IllegalArgumentException("Некорректный INSERT-запрос: $sql")
        }
    }

}
