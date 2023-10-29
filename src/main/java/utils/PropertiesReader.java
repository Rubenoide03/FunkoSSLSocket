package utils;

import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
@Log4j
public class PropertiesReader {
    private final String fileName;
    private final Properties properties;
    Logger logger = org.slf4j.LoggerFactory.getLogger(PropertiesReader.class);

    public PropertiesReader(String fileName) {
        this.fileName = fileName;
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(new File(fileName))) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            logger.error("No se encuentra el fichero " + fileName, e);
        } catch (IOException e) {
            logger.error("Error al cargar el fichero de propiedades ", e);

        }finally {
            logger.info("Fichero de propiedades cargado correctamente");
        }
    }
        public String getProperty(String key) throws FileNotFoundException {
            String value = properties.getProperty(key);
            if (value == null) {
                logger.error("No se encuentra la propiedad " + key + " en el fichero " + fileName);
                throw  new FileNotFoundException();

            } else {
                return value;
            }
        }

}


