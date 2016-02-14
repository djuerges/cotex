package de.uniluebeck.collaboratex.jackson;

import com.google.appengine.api.datastore.Key;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class KeySerializer extends JsonSerializer<Key> {

	@Override
	public void serialize(Key value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {
            jgen.writeString(value.toString());
    }
}
