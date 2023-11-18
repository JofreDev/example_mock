# Utiliza una imagen base adecuada
FROM amazoncorretto:17-alpine


USER root
# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR al directorio de trabajo
COPY --chown=appuser *.jar TestE2EOrquestadorStiNube.jar

#COPY *.jar TestE2EOrquestadorStiNube.jar

# Cambia los permisos y propietario del archivo JAR
RUN chmod 777 /app && \
    chmod 777 /app/TestE2EOrquestadorStiNube.jar



# Crea un volumen para almacenar datos (en este caso, /tmp)
VOLUME /tmp

# Configura las opciones de Java y la memoria máxima
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

#ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Crea un usuario no root y asigna permisos adecuados
RUN adduser -D appuser && chown -R appuser /app

USER appuser

# Define el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar TestE2EOrquestadorStiNube.jar"]
