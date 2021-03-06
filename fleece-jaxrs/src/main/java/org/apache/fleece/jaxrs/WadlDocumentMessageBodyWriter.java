/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fleece.jaxrs;

import org.apache.fleece.jaxrs.xml.WadlDocumentToJson;
import org.w3c.dom.Document;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static org.apache.fleece.jaxrs.Jsons.isJson;

public class WadlDocumentMessageBodyWriter implements MessageBodyWriter<Document> {
    private final WadlDocumentToJson converter = new WadlDocumentToJson();

    @Override
    public boolean isWriteable(final Class<?> aClass, final Type type,
                               final Annotation[] annotations, final MediaType mediaType) {
        return isJson(mediaType) && Document.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(final Document document, final Class<?> aClass,
                        final Type type, final Annotation[] annotations,
                        final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final Document document, final Class<?> aClass,
                        final Type type, final Annotation[] annotations,
                        final MediaType mediaType, final MultivaluedMap<String, Object> stringObjectMultivaluedMap,
                        final OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            outputStream.write(converter.convert(document).getBytes());
        } catch (final XMLStreamException e) {
            throw new IllegalStateException(e);
        }
    }
}
