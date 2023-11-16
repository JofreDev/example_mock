# Utiliza una imagen base adecuada
FROM amazoncorretto:17-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR al directorio de trabajo
COPY *.jar TestE2EOrquestadorStiNube.jar

# Configura las opciones de Java y la memoria máxima
ENV JAVA_OPTS="-Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Crea un usuario no root y asigna permisos adecuados
RUN adduser -D appuser && \
    chown appuser:appuser /app/TestE2EOrquestadorStiNube.jar && \
    chmod +x /app/TestE2EOrquestadorStiNube.jar \

USER appuser

## Se expone el endpoint por el 8080
EXPOSE 8080

# Define el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar TestE2EOrquestadorStiNube.jar"]
