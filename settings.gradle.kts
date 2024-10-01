plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "sno-rest-api-microservice"

include("eureka-server")
include("eureka-server-2")
include("eureka-server-3")
include("gateway")
include("auth-service-1")
include("auth-service-2")
include("auth-service-3")
include("auth-service-1")
include("main-service")
include("predict-service")
include("predict-service:src:main:predict")
findProject(":predict-service:src:main:predict")?.name = "predict"
